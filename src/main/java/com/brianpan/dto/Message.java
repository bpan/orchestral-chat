package com.brianpan.dto;

import javax.persistence.Column;
import java.sql.Timestamp;

public class Message {
  @Column(name = "MESSAGE_ID")
  private long messageId;
  @Column(name = "SENDER_ID")
  private long senderId;
  @Column(name = "RECIPIENT_ID")
  private long recipientId;
  @Column(name = "MESSAGE_TEXT")
  private String messageText;
  @Column(name = "TIME")
  private Timestamp time;

  public long getMessageId() {
    return messageId;
  }

  public long getSenderId() {
    return senderId;
  }

  public long getRecipientId() {
    return recipientId;
  }

  public String getMessageText() {
    return messageText;
  }

  public Timestamp getTime() {
    return time;
  }
}
