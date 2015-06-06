package com.brianpan.db;

import com.brianpan.dto.Message;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static com.brianpan.db.Tables.MESSAGES;

@Repository
public class MessageDAO {
  private final DSLContext create;

  public MessageDAO() throws SQLException, ClassNotFoundException {
    create = DSL.using(getConnection(), SQLDialect.H2);
  }

  public Message storeMessage(long userId, Message message) {
    return create.insertInto(MESSAGES, MESSAGES.SENDER_ID, MESSAGES.RECIPIENT_ID, MESSAGES.MESSAGE_TEXT, MESSAGES.TIME)
        .values(userId, message.getRecipientId(), message.getMessageText(), message.getTime())
        .returning()
        .fetchOne().into(Message.class);
  }

  public List<Message> getMessagesSinceTimestamp(long userId, Timestamp time) {
    return create.select()
        .from(MESSAGES)
//        .where(MESSAGES.RECIPIENT_ID.equal(userId)
//            .and(MESSAGES.TIME.greaterThan(time)))
        .fetchInto(Message.class);
  }

  private Connection getConnection() throws SQLException, ClassNotFoundException {
    // todo: put in properties file
    String userName = "sa";
    String password = "";
    String url = "jdbc:h2:~/orchestral-chat";

    return DriverManager.getConnection(url, userName, password);
  }
}
