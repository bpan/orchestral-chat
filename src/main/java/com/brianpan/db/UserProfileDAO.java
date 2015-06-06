package com.brianpan.db;

import com.brianpan.dto.User;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static com.brianpan.db.Tables.*;

@Repository
public class UserProfileDAO {
  private final DSLContext create;

  public UserProfileDAO() throws SQLException {
    create = DSL.using(getConnection(), SQLDialect.H2);
  }

  public User addUser(String username, String firstName, String lastName) {
    return create.insertInto(USERS, USERS.USERNAME, USERS.FIRST_NAME, USERS.LAST_NAME)
        .values(username, firstName, lastName)
        .returning()
        .fetchOne().into(User.class);
  }

  public List<User> getUsers() {
    return create.select()
        .from(USERS)
        .fetch().into(User.class);
  }

  public User findUser(String username) {
    return create.select()
        .from(USERS)
        .where(USERS.USERNAME.equal(username))
        .fetchAny().into(User.class);
  }

  public void addContact(long userId, long contactId) {
    create.mergeInto(CONTACTS)
        .using(create.selectOne())
        .on(CONTACTS.USER_ID.equal(userId))
        .and(CONTACTS.CONTACT_ID.equal(contactId))
        .whenNotMatchedThenInsert(CONTACTS.USER_ID, CONTACTS.CONTACT_ID)
        .values(userId, contactId)
        .execute();
  }

  public List<User> getContacts(long userId) {
    return create.select()
        .from(CONTACTS)
        .join(USERS).on(USERS.USER_ID.equal(CONTACTS.CONTACT_ID))
        .where(CONTACTS.USER_ID.equal(userId))
        .fetchInto(User.class);
  }

  private Connection getConnection() throws SQLException {
    // todo: put in properties file
    String userName = "sa";
    String password = "";
    String url = "jdbc:h2:~/orchestral-chat";

    return DriverManager.getConnection(url, userName, password);
  }
}
