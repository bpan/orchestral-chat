package com.brianpan.chat;

import com.brianpan.db.UserProfileDAO;
import com.brianpan.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserProfileController {
  private UserProfileDAO _userProfileDAO;

  @Autowired
  public UserProfileController(UserProfileDAO userProfileDAO) {
    this._userProfileDAO = userProfileDAO;
  }

  @RequestMapping(value = "/users", method = RequestMethod.POST)
  public void addUser(@RequestBody User newUser) {
    _userProfileDAO.addUser(newUser.getUsername(), newUser.getFirstName(), newUser.getLastName());
  }
  @RequestMapping(value = "/users", method = RequestMethod.GET)
  public List<User> getUsers() {
    return _userProfileDAO.getUsers();
  }

  @RequestMapping(value = "/users/search", method = RequestMethod.GET)
  public User findUser(@RequestParam("username") String username) {
    return _userProfileDAO.findUser(username);
  }

  @RequestMapping(value = "/user/{userId}/contact/{contactId}", method = RequestMethod.PUT)
  public void addContact(@PathVariable("userId") long userId, @PathVariable("contactId") long contactId) {
    _userProfileDAO.addContact(userId, contactId);
  }

  @RequestMapping(value = "/user/{userId}/contacts", method = RequestMethod.GET)
  public List<User> getContacts(@PathVariable("userId") long userId) {
    return _userProfileDAO.getContacts(userId);
  }
}
