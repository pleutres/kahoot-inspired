package com.gorki.controller;

import com.gorki.dto.Message;
import com.gorki.dto.PlayerAnswer;
import com.gorki.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import java.util.Map;


@Controller
public class HostController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    private QuestionService questionService;

    @MessageMapping("/host")
    public void receiveMessageFromPlayers(Message message) {
        //ObjectMapper mapper = new ObjectMapper();

        if (message.getType().equals("playerAnswer")) {
            Map<String, String> data = (Map<String, String>) message.getMessage();
            logger.info("data {}", data);
            PlayerAnswer playerAnswer = new PlayerAnswer();
            playerAnswer.setAnswerText(data.get("answer"));
            playerAnswer.setPlayerId(Long.parseLong(data.get("playerId")));

            logger.info("playerAnswer : {}", playerAnswer);
            questionService.playerAnswer(playerAnswer);
        }


    }
}
