package com.gorki.dto;

public class Message {

  private Object message;
  private int id;
  private String type;
  
  public Message() {
    
  }
  
  public Message(int id, String type, Object message) {
    this.id = id;
    this.type = type;
    this.message = message;
  }

  public Object getMessage() {
    return message;
  }

  public void setMessage(Object message) {
    this.message = message;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "Message{" +
            "message=" + message +
            ", id=" + id +
            ", type='" + type + '\'' +
            '}';
  }
}
