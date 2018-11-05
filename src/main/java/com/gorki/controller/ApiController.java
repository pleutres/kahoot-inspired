package com.gorki.controller;

import com.gorki.dto.Message;
import com.gorki.dto.Question;
import com.gorki.dto.QuestionsList;
import com.gorki.service.EventService;
import com.gorki.service.PlayerService;
import com.gorki.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;


@RestController
@RequestMapping("/api")
public class ApiController {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Inject
  private PlayerService playerService;

  @Inject
  private EventService eventService;

  @Inject
  private QuestionService questionService;

  @RequestMapping(value = "/join/{playerId}", params = {"playerName"}, method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
  @SendTo("/topic/player")
  public ResponseEntity<String> join(
          @PathVariable(value = "playerId") final Long playerId,
          @RequestParam(value = "playerName") final String playerName) {

    Assert.notNull(playerId, "playerId doit etre non null");
    Assert.notNull(playerName, "playerName doit etre non null");

    boolean result = playerService.addPlayer(playerId, playerName);
    if (result) {
      return ResponseEntity.ok("added");
    }
    else {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

  }

  @RequestMapping(value = "/question/start", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> startQuestion(
          @RequestBody final Question question) {

    Assert.notNull(question, "question doit etre non null");

    logger.info("Question received : {}", question);

    questionService.startQuestion(question);

    return ResponseEntity.ok("received");

  }

  @RequestMapping(value = "/question/stop", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> stopQuestion() {

    questionService.stopQuestion();

    return ResponseEntity.ok("stopped");

  }

  @RequestMapping(value = "/questions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<QuestionsList> getQuestions() {
    return new ResponseEntity(questionService.getQuestionsList(), HttpStatus.OK);

  }


  @RequestMapping(value = "/scoring", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> sendScoring() {

    questionService.scoring();

    return ResponseEntity.ok("scoring");

  }

  @RequestMapping(value = "/showconnection", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> showConnection() {

    eventService.showConnection();

    return ResponseEntity.ok("showConnect");

  }

  @RequestMapping(value = "/refreshPlayers", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> refreshPlayers() {

    playerService.refreshPlayer();

    return ResponseEntity.ok("players sent");

  }


  @RequestMapping("/sayHello")
  @SendTo("/topic/player")
  public ResponseEntity<Void>  sayHello() {
    Message message = new Message();
    message.setMessage("hello");

    eventService.sayHello();

    return ResponseEntity.ok(null);
  }


}
