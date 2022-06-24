package th.co.scb.assignment.svcassignmentproject.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.scb.assignment.svcassignmentproject.assignment.data.User;
import th.co.scb.assignment.svcassignmentproject.assignment.exception.UserNotFoundException;
import th.co.scb.assignment.svcassignmentproject.assignment.repository.UserRepository;
import th.co.scb.assignment.svcassignmentproject.assignment.service.UserService;
import th.co.scb.assignment.svcassignmentproject.assignment.service.UserServiceImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private static User mockUserWithNameAndEmail;
    private static User mockUserWithName;
    private static User mockUserWithEmail;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserServiceImpl userService;

    @BeforeAll
    static void setUp() {
        mockUserWithNameAndEmail = new User("Pailin", "phornpailin.khonthad@gmail.com");
        mockUserWithName = new User("Mammo", null);
        mockUserWithEmail = new User(null, "unknown@gmail.com");

        mockUserWithNameAndEmail.setId(1);
        mockUserWithName.setId(2);
        mockUserWithEmail.setId(3);
    }

    @Test
    void testCreateDataSuccessfully() {
        when(userRepository.save(any(User.class))).thenReturn(mockUserWithNameAndEmail);
        User actualResult = userService.createData(mockUserWithEmail);
        assertSame(mockUserWithNameAndEmail, actualResult);
    }

    @Test
    void testCreateDataUnsuccessfullyWithNullEmail() {
        UserNotFoundException expectedException = new UserNotFoundException(String.valueOf(mockUserWithName.getId()));
        when(userRepository.save(mockUserWithName)).thenThrow(expectedException);
        assertThrows(UserNotFoundException.class, () -> userService.createData(mockUserWithName));
    }

}