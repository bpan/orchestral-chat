CREATE TABLE orchestral_chat.users (
  user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  first_name VARCHAR(50),
  last_name VARCHAR(50),
);

CREATE TABLE orchestral_chat.contacts (
  user_id BIGINT NOT NULL,
  contact_id BIGINT NOT NULL,

  CONSTRAINT fk_t_contacts_user_id FOREIGN KEY (user_id) REFERENCES orchestral_chat.users(user_id),
  CONSTRAINT fk_t_contacts_contact_id FOREIGN KEY (contact_id) REFERENCES orchestral_chat.users(user_id)
);

INSERT INTO orchestral_chat.users (username, first_name, last_name) VALUES ('brianpan', 'Brian', 'Pan');
INSERT INTO orchestral_chat.users (username, first_name, last_name) VALUES ('potus', 'Barack', 'Obama');
INSERT INTO orchestral_chat.users (username, first_name, last_name) VALUES ('vale46', 'Valentino', 'Rossi');
INSERT INTO orchestral_chat.users (username, first_name, last_name) VALUES ('drose1', 'Derrick', 'Rose');
INSERT INTO orchestral_chat.users (username, first_name, last_name) VALUES ('joakim', 'Joakim', 'Noah');
INSERT INTO orchestral_chat.users (username, first_name, last_name) VALUES ('bymyself', 'Loner', 'Joe');


INSERT INTO orchestral_chat.contacts VALUES (
  (SELECT user_id FROM orchestral_chat.users WHERE username = 'brianpan'),
  (SELECT user_id FROM orchestral_chat.users WHERE username = 'potus')
);
INSERT INTO orchestral_chat.contacts VALUES (
  (SELECT user_id FROM orchestral_chat.users WHERE username = 'brianpan'),
  (SELECT user_id FROM orchestral_chat.users WHERE username = 'vale46')
);
INSERT INTO orchestral_chat.contacts VALUES (
  (SELECT user_id FROM orchestral_chat.users WHERE username = 'brianpan'),
  (SELECT user_id FROM orchestral_chat.users WHERE username = 'drose1')
);
INSERT INTO orchestral_chat.contacts VALUES (
  (SELECT user_id FROM orchestral_chat.users WHERE username = 'drose1'),
  (SELECT user_id FROM orchestral_chat.users WHERE username = 'joakim')
);


CREATE TABLE orchestral_chat.messages (
  message_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  sender_id BIGINT NOT NULL,
  recipient_id BIGINT NOT NULL,
  message_text VARCHAR NOT NULL,
  time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  CONSTRAINT fk_t_messages_sender_id FOREIGN KEY (sender_id) REFERENCES orchestral_chat.users(user_id),
  CONSTRAINT fk_t_messages_recipient_id FOREIGN KEY (recipient_id) REFERENCES orchestral_chat.users(user_id)
);
