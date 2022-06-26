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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    void testFindByIdSuccessfully() {
        when(userRepository.findById(mockUserWithNameAndEmail.getId())).thenReturn(Optional.ofNullable(mockUserWithNameAndEmail));
        User actualResult = userService.findById(mockUserWithNameAndEmail.getId());
        assertSame(mockUserWithNameAndEmail, actualResult);
    }

    @Test
    void testFindByIdUnsuccessfully() {
        UserNotFoundException expectedException = new UserNotFoundException(String.valueOf(777));
        when(userRepository.findById(mockUserWithNameAndEmail.getId())).thenThrow(expectedException);
        assertThrows(UserNotFoundException.class, () -> userService.findById(mockUserWithNameAndEmail.getId()));
    }

    @Test
    void testGetAllData() {
        ArrayList<User> expectedList = new ArrayList<>() {
            {
                add(mockUserWithNameAndEmail);
                add(mockUserWithName);
            }
        };

        when(userRepository.findAll()).thenReturn(expectedList);
        assertSame(expectedList, userService.getAllData());
    }

    @Test
    void testUpdateDataSuccessfully() {
        User updatedUser = new User(mockUserWithNameAndEmail.getName(), "updatedEmail@gmail.com");
        updatedUser.setId(mockUserWithNameAndEmail.getId());
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        User actualResult = userService.updateData(updatedUser.getId(), updatedUser);
        assertSame(updatedUser, actualResult);
    }

    @Test
    void testUpdatePartialData() {
        User updatedUser = new User(mockUserWithNameAndEmail.getName(), null);
        updatedUser.setId(mockUserWithNameAndEmail.getId());
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        User actualResult = userService.updateData(updatedUser.getId(), updatedUser);
        assertSame(updatedUser, actualResult);
    }

    @Test
    void testDeleteDataByIdSuccessfully() {
        doNothing().when(userRepository).deleteById(mockUserWithNameAndEmail.getId());
        userService.deleteDataById(mockUserWithNameAndEmail.getId());
        verify(userRepository).deleteById(any());
    }

}