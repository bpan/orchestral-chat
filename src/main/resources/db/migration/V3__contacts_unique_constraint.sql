ALTER TABLE orchestral_chat.contacts
  ADD CONSTRAINT fk_t_contacts_unique UNIQUE(user_id, contact_id);