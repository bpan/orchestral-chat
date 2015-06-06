package com.brianpan.chat;

import com.brianpan.db.MessageDAO;
import com.brianpan.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class MessagingController {
  // Map of userId to the user's message queue
  private static ConcurrentHashMap<Long, Queue<Message>> connectedUsers = new ConcurrentHashMap<>();
  private MessageDAO _messageDAO;

  @Autowired
  public MessagingController(MessageDAO messageDAO) {
    _messageDAO = messageDAO;
  }

  @RequestMapping(value = "/user/{userId}/messages", method = RequestMethod.POST)
  public void sendMessage(@PathVariable("userId") long userId, @RequestBody Message newMessage) {
    Message storedMessage = _messageDAO.storeMessage(userId, newMessage);
    Queue<Message> messageQueue = connectedUsers.get(userId);
    if (null != messageQueue) {
      messageQueue.add(storedMessage);
    }
  }

  @RequestMapping(value = "/user/{userId}/messages", method = RequestMethod.GET)
  public List<Message> getMessagesSinceTimestamp(@PathVariable("userId") long userId,
                                                 @RequestParam("startingTimestamp") Timestamp time) {
    return _messageDAO.getMessagesSinceTimestamp(userId, time);
  }

  @RequestMapping(value = "/user/{userId}/messages/stream", method = RequestMethod.GET)
  public void getEventStreamMessages(@PathVariable("userId") long userId, HttpServletResponse response)
      throws IOException, InterruptedException {
    response.setContentType("text/event-stream");
    response.setCharacterEncoding("UTF-8");
    PrintWriter writer = response.getWriter();

    Queue<Message> messageQueue = new LinkedList<>();
    if (null != connectedUsers.putIfAbsent(userId, messageQueue)) {
      writer.write("{\"Error\": \"You are already logged in somewhere else.\"");
      // todo: better to broadcast messages to all clients connected as userId
      return;
    }

    try {
      Message message;
      while (true) {
        message = messageQueue.poll();
        if (null != message) {
          // Send message as a JSON object
          writer.write("event: newmessage\n");
          writer.write("data: { \"messageId\":");
          writer.write(String.valueOf(message.getMessageId()));
          writer.write(", \"senderId\":");
          writer.write(String.valueOf(message.getSenderId()));
          writer.write(", \"recipientId\":");
          writer.write(String.valueOf(message.getRecipientId()));
          writer.write(", \"messageText\":");
          writer.write(String.valueOf(message.getMessageId()));
          writer.write(", \"time\":\"");
          writer.write(String.valueOf(message.getTime().toString())); // todo: make this ISO8601
          writer.write("\" }\n\n");
          writer.flush();
        } else {
          Thread.sleep(1000);
        }
      }
    } finally {
      connectedUsers.remove(userId);
    }
  }
}
