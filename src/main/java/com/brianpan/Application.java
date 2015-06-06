package com.brianpan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

  public static void main(String[] args) throws ClassNotFoundException {
    Class.forName("org.h2.Driver");
    SpringApplication.run(Application.class, args);
  }
}
