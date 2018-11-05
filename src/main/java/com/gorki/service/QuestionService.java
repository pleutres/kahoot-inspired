package com.gorki.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gorki.dto.PlayerAnswer;
import com.gorki.dto.Question;
import com.gorki.dto.QuestionsList;
import com.gorki.dto.Rank;
import com.gorki.dto.Score;
import com.gorki.dto.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

@Service
public class QuestionService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public static QuestionThread questionThread = new QuestionThread();

    private static HashMap<Long, Integer> scoring = new HashMap<>();

    @Value("${question.file.path}")
    private String questionFilePath;

    @Inject
    private EventService eventService;

    @Inject PlayerService playerService;

    public QuestionsList getQuestionsList() {
        File file = new File(new File("."), questionFilePath);
        logger.info("question file is there : {}", file);

        ObjectMapper mapper = new ObjectMapper();
        try {
            QuestionsList questionsList = mapper.readValue(file, QuestionsList.class);
            logger.error("questions found : {}", questionsList);
            return questionsList;
        } catch (IOException e) {
            logger.error("Can not parse", e);
        }
        return null;
    }


    public void startQuestion(Question question) {

        questionThread.interrupt();
        questionThread = new QuestionThread();
        questionThread.setQuestion(question);
        questionThread.setEventService(eventService);
        questionThread.setPlayerService(playerService);
        questionThread.start();
    }

    public void stopQuestion() {

        questionThread.interrupt();
        eventService.sendToPlayerAndHost("game-reset", 0);


    }

    public void playerAnswer(PlayerAnswer playerAnswer) {
        if (questionThread != null) {
            questionThread.playerAnswer(playerAnswer, playerService.getPlayerName(playerAnswer.getPlayerId()));
        }

    }

    public void scoring() {
        TreeSet<Score> scores = new TreeSet<>();
        for (Map.Entry<Long, Integer> entry : scoring.entrySet()) {
            Score score = new Score(entry.getKey(), playerService.getPlayerName(entry.getKey()), entry.getValue());
            scores.add(score);
        }

        eventService.sendToPlayerAndHost("scoring", scores);
    }

    private static class QuestionThread extends Thread {

        private Logger logger = LoggerFactory.getLogger(getClass());

        private Question question;
        private EventService eventService;
        private PlayerService playerService;
        private ArrayList<Rank> ranking = new ArrayList<>();
        private boolean answerTime = false;

        public void setQuestion(Question question) {
            this.question = question;
        }

        public void setEventService(EventService eventService) {
            this.eventService = eventService;
        }
        public void setPlayerService(PlayerService playerService) {
            this.playerService = playerService;
        }

        @Override
        public void run() {
            answerTime = false;
            try {
                for (int i=5; i >= 0; i-- ) {
                    eventService.sendToPlayerAndHost("game-start", i);
                    Thread.sleep(1000);
                }

                eventService.sendToPlayerAndHost("game-question", question);
                Thread.sleep(5000);

                answerTime = true;
                for (int i=question.getWaitTime(); i >= 0; i-- ) {
                    Status status = new Status();
                    status.setQuestion(question);
                    status.setCount(i);
                    eventService.sendToPlayerAndHost("game-wait", status);
                    Thread.sleep(1000);
                }

                eventService.sendToPlayerAndHost("game-end", 0);

                computeScoring();

                answerTime = false;

            } catch (InterruptedException e) {

            }

        }

        private void computeScoring() {
            int numberOfPlayer = playerService.getNumberOfPlayer();
            logger.info("Ranking = {}", ranking);
            for (int i=0; i < ranking.size(); i++) {
                Rank rank = ranking.get(i);
                Integer score = scoring.get(rank.getPlayerId());
                logger.info("{} - player {} - current score = {}", i, rank.getPlayerId(), score);
                if (score == null) {
                    score = 0;
                }
                if (rank.isCorrect()) {
                    score += (numberOfPlayer-i) + 5;
                    logger.info("correct... new score = {}", score);
                }
                else {
                    // Point de participation !
                    score++;
                    logger.info("not correct... new score = {}", score);
                }
                scoring.put(rank.getPlayerId(), score);
            }


        }

        public void playerAnswer(PlayerAnswer playerAnswer, String playerName) {
            if (answerTime) {
                boolean correct = question.isAnswerCorrect(playerAnswer.getAnswerText());
                Rank rank = new Rank(playerAnswer.getPlayerId(), playerName, playerAnswer.getAnswerText(), correct);

                synchronized (ranking) {
                    ranking.remove(rank);
                    ranking.add(rank);
                }

                logger.info("send ranking: {}", ranking);
                eventService.sendRanking(ranking);
            }

        }
    }





}