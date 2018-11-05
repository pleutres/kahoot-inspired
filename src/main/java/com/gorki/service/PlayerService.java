package com.gorki.service;

import com.gorki.dto.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PlayerService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public static ConcurrentHashMap<Long, Player> players = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, Long> playersName = new ConcurrentHashMap<>();

    @Inject
    private EventService eventService;


    /**
     * Add player
     *
     * @param playerId
     * @param playerName
     * @return true if player was not already in the list, false if someone has the same name
     */
    public boolean addPlayer(Long playerId, String playerName) {

        Long existingId = playersName.get(playerName);
        if ((existingId != null) && (!existingId.equals(playerId))) {
            logger.warn("Player already exists for {} with id {}, new player id {}", playerName, existingId, playerId);
            return false;
        }

        Player player = new Player(playerId, playerName);
        Player result = players.putIfAbsent(playerId, player);
        if (result != null) {
            if (!result.getName().equals(playerName)) {
                result.setName(playerName);
                eventService.notifyNewPlayer(player);
            }
        } else {
            eventService.notifyNewPlayer(player);
        }
        playersName.put(playerName, playerId);

        refreshPlayer();

        return true;
    }

    public int getNumberOfPlayer() {
        return players.size();
    }


    public void refreshPlayer() {

        eventService.notifyAllPlayers(players);
    }

    public String getPlayerName(Long playerId) {
        Player player = players.get(playerId);
        if (player != null) {
            return player.getName();
        }
        return null;
    }
}