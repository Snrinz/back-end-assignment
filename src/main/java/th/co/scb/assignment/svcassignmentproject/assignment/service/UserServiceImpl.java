package th.co.scb.assignment.svcassignmentproject.assignment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.assignment.svcassignmentproject.assignment.data.User;
import th.co.scb.assignment.svcassignmentproject.assignment.exception.UserNotFoundException;
import th.co.scb.assignment.svcassignmentproject.assignment.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createData(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findById (int userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(userId)));
    }

    @Override
    public List<User> getAllData() {
        return userRepository.findAll();
    }

    @Override
    public User updateData(int userId, User newUser) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setName(newUser.getName());
                    user.setEmail(newUser.getEmail());
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    return userRepository.save(newUser);
                });
    }

    @Override
    public User updatePartialData(int userId, User newUser) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setName(newUser.getName() == null ? user.getName() : newUser.getName());
                    user.setEmail(newUser.getEmail() == null ? user.getEmail() : newUser.getEmail());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(userId)));
    }

    @Override
    public void deleteDataById(int userId) {
        Optional<User> result = userRepository.findById(userId);
        if (result.isPresent()) {
            throw new UserNotFoundException(String.valueOf(userId));
        }

        userRepository.deleteById(userId);
    }
}
