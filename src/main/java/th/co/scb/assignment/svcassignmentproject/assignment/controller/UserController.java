package th.co.scb.assignment.svcassignmentproject.assignment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import th.co.scb.assignment.svcassignmentproject.assignment.data.User;
import th.co.scb.assignment.svcassignmentproject.assignment.exception.UserInformationRequired;
import th.co.scb.assignment.svcassignmentproject.assignment.service.UserServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public User createData(@RequestBody User user) {

        if (user.getName() == null) {
            throw new UserInformationRequired("name");
        }
        else if(user.getEmail() == null) {
            throw new UserInformationRequired("email");
        }

        return userService.createData(user);
    }

    @GetMapping("/users/{userId}")
    public User findById(@PathVariable int userId) {
        if (userId < 0) {
            throw new IllegalArgumentException("Invalid input: " + userId);
        }

        return userService.findById(userId);
    }

    @GetMapping("/users")
    public List<User> getAllData() {
        return userService.getAllData();
    }

    @PutMapping("/users/{userId}")
    public User updateAllDataById(@PathVariable int userId, @RequestBody User user) {
        if (userId < 0) {
            throw new IllegalArgumentException("Invalid input: " + userId);
        }

        if (user.getName() == null) {
            throw new UserInformationRequired("name");
        }
        else if(user.getEmail() == null) {
            throw new UserInformationRequired("email");
        }

        return userService.updateData(userId, user);
    }

    @PatchMapping("/users/{userId}")
    public User updatePartialDataById(@PathVariable int userId, @RequestBody User user) {
        if (userId < 0) {
            throw new IllegalArgumentException("Invalid input: " + userId);
        }

        if (user.getName() == null && user.getEmail() == null) {
            throw new UserInformationRequired("name and email");
        }

        return userService.updatePartialData(userId, user);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteDataById(@PathVariable int userId) {
        if (userId < 0) {
            throw new IllegalArgumentException("Invalid input: " + userId);
        }

        userService.deleteDataById(userId);
    }

}