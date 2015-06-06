package com.brianpan.dto;

import javax.persistence.Column;

public class User {
  @Column(name = "USER_ID")
  private long userId;
  @Column(name = "USERNAME")
  private String username;
  @Column(name = "FIRST_NAME")
  private String firstName;
  @Column(name = "LAST_NAME")
  private String lastName;

  public long getUserId() {
    return userId;
  }

  public String getUsername() {
    return username;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }
}
