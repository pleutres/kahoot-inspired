package com.gorki.service;

import com.gorki.dto.Message;
import com.gorki.dto.OutputMessage;
import com.gorki.dto.Player;
import com.gorki.dto.Question;
import com.gorki.dto.Rank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EventService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final String TO_HOST = "/topic/host";

    private static final String TO_PLAYERS = "/topic/player";

    private static Integer messageId = 0;

    @Inject
    private SimpMessagingTemplate template;


    public void notifyNewPlayer(Player player) {
        OutputMessage message = getOutputMessage("player", player);
        this.template.convertAndSend(TO_HOST, message);
    }

    public void showConnection() {
        OutputMessage message = getOutputMessage("show-connection", "nothing");
        this.template.convertAndSend(TO_HOST, message);
    }

    public void notifyAllPlayers(ConcurrentHashMap<Long, Player> players) {

        OutputMessage message = getOutputMessage("players", players);
        this.template.convertAndSend(TO_HOST, message);
    }

    public void notifyAllQuestion(Question question) {

        OutputMessage message = getOutputMessage("question", question);
        this.template.convertAndSend(TO_HOST, message);
    }

    public void sayHello() {
        logger.info("sayHello to players");
        OutputMessage message = getOutputMessage("hello", "hello");
        this.template.convertAndSend(TO_PLAYERS, message);
    }

    private OutputMessage getOutputMessage(String type, Object object) {
        OutputMessage message;
        synchronized (messageId) {
            messageId++;
            Message playerMessage = new Message(messageId, type, object);
            message = new OutputMessage(playerMessage);
        }
        return message;
    }


    public void sendToPlayerAndHost(String type, Object o) {
        logger.info("{} to players and host: {}", type, o);
        OutputMessage message = getOutputMessage(type, o);
        this.template.convertAndSend(TO_PLAYERS, message);
        this.template.convertAndSend(TO_HOST, message);
    }

    public void sendRanking(ArrayList<Rank> ranking) {
        OutputMessage message = getOutputMessage("ranking", ranking);
        this.template.convertAndSend(TO_HOST, message);
    }
}