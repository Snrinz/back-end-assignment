package th.co.scb.assignment.svcassignmentproject.assignment.service;

import th.co.scb.assignment.svcassignmentproject.assignment.data.User;
import th.co.scb.assignment.svcassignmentproject.assignment.exception.UserNotFoundException;

import java.util.List;

public interface UserService {

    User createData(User user);
    User findById(int userId) throws UserNotFoundException;
    List<User> getAllData();
    User updateData(int userId, User user);
    User updatePartialData(int userId, User newUser);
    void deleteDataById(int userId);

}
