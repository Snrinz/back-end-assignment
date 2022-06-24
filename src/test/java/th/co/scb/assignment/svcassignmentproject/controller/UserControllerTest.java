package th.co.scb.assignment.svcassignmentproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import th.co.scb.assignment.svcassignmentproject.assignment.controller.UserController;
import th.co.scb.assignment.svcassignmentproject.assignment.data.User;
import th.co.scb.assignment.svcassignmentproject.assignment.exception.UserInformationRequired;
import th.co.scb.assignment.svcassignmentproject.assignment.exception.UserNotFoundException;
import th.co.scb.assignment.svcassignmentproject.assignment.service.UserServiceImpl;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    private static User mockUserWithNameAndEmail;
    private static User mockUserWithName;
    private static User mockUserWithEmail;
    private final int NEGATIVE_NUMBER_ID = -1;
    private final int NON_EXISTING_ID = 999;
    private final String PREFIX_USERS_API = "/api/users";

    @MockBean
    UserServiceImpl userService;

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

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
    void testCreateDataSuccessfully() throws Exception {
        when(userService.createData(any(User.class))).thenReturn(mockUserWithNameAndEmail);

        mockMvc.perform( MockMvcRequestBuilders
                        .post(PREFIX_USERS_API)
                        .content(mapper.writeValueAsString(mockUserWithNameAndEmail))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(mockUserWithNameAndEmail.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(mockUserWithNameAndEmail.getEmail()));
    }

    @Test
    void testThrowRuntimeExceptionWhenCreateDataUnsuccessfullyWithOnlyName() throws Exception {
        UserInformationRequired expectedException = new UserInformationRequired("email");
        when(userService.createData(any(User.class))).thenThrow(expectedException);

        mockMvc.perform( MockMvcRequestBuilders
                        .post(PREFIX_USERS_API)
                        .content(mapper.writeValueAsString(mockUserWithName))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(expectedException.getMessage()));
    }

    @Test
    void testThrowRuntimeExceptionWhenCreateDataUnsuccessfullyWithOnlyEmail() throws Exception {
        UserInformationRequired expectedException = new UserInformationRequired("name");
        when(userService.createData(any(User.class))).thenThrow(expectedException);

        mockMvc.perform( MockMvcRequestBuilders
                        .post(PREFIX_USERS_API)
                        .content(mapper.writeValueAsString(mockUserWithEmail))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(expectedException.getMessage()));
    }

    @Test
    void testFindByIdSuccessfully() throws Exception {
        when(userService.findById(any(Integer.class))).thenReturn(mockUserWithNameAndEmail);

        mockMvc.perform( MockMvcRequestBuilders
                        .get(PREFIX_USERS_API + "/{id}", mockUserWithNameAndEmail.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(mockUserWithNameAndEmail.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(mockUserWithNameAndEmail.getEmail()));
    }

    @Test
    void testFindByIdUnsuccessfullyWithNonExistingId() throws Exception {
        UserNotFoundException expectedException = new UserNotFoundException(String.valueOf(NON_EXISTING_ID));
        when(userService.findById(any(Integer.class))).thenThrow(expectedException);

        mockMvc.perform( MockMvcRequestBuilders
                        .get(PREFIX_USERS_API + "/{id}", NON_EXISTING_ID))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(expectedException.getMessage()));
    }

    @Test
    void testFindByIdUnsuccessfullyWithIdAsZeroAndNegativeNumber() throws Exception {
        IllegalArgumentException expectedException = new IllegalArgumentException("Invalid input: " + NEGATIVE_NUMBER_ID);
        when(userService.findById(any(Integer.class))).thenThrow(expectedException);

        mockMvc.perform( MockMvcRequestBuilders
                        .get(PREFIX_USERS_API + "/{id}", NEGATIVE_NUMBER_ID))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(expectedException.getMessage()));
    }

    @Test
    void testGetAllData() throws Exception {
        ArrayList<User> expectedList = new ArrayList<>() {
            {
                add(mockUserWithNameAndEmail);
                add(mockUserWithName);
            }
        };

        when(userService.getAllData()).thenReturn(expectedList);

        mockMvc.perform( MockMvcRequestBuilders
                        .get(PREFIX_USERS_API))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id").isNotEmpty());
    }

    @Test
    void testUpdateAllDataByIdWithNonExistingId() throws Exception {
        User expectedUser = new User("nonExistingName", "nonExistingEmail");
        expectedUser.setId(NON_EXISTING_ID);

        when(userService.updateData(any(Integer.class), any(User.class))).thenReturn(expectedUser);

        mockMvc.perform( MockMvcRequestBuilders
                        .put(PREFIX_USERS_API + "/{id}", expectedUser.getId())
                        .content(mapper.writeValueAsString(expectedUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(expectedUser.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(expectedUser.getEmail()));
    }

    @Test
    void testUpdateAllDataByIdWithExistingId() throws Exception {
        userService.createData(mockUserWithNameAndEmail);

        User expectedUpdatedUser = new User("updatedName", "updatedEmail");

        when(userService.updateData(any(Integer.class), any(User.class))).thenReturn(expectedUpdatedUser);

        mockMvc.perform( MockMvcRequestBuilders
                        .put(PREFIX_USERS_API + "/{id}", mockUserWithNameAndEmail.getId())
                        .content(mapper.writeValueAsString(expectedUpdatedUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(expectedUpdatedUser.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(expectedUpdatedUser.getEmail()));
    }

    @Test
    void testThrowRuntimeExceptionWhenUpdateAllDataByIdWithOnlyName() throws Exception{
        userService.createData(mockUserWithName);

        User expectedUpdatedUser = new User("updatedName", "updatedEmail");
        UserInformationRequired expectedException = new UserInformationRequired("email");

        when(userService.updateData(any(Integer.class), any(User.class))).thenThrow(expectedException);

        mockMvc.perform( MockMvcRequestBuilders
                        .put(PREFIX_USERS_API + "/{id}", mockUserWithName.getId())
                        .content(mapper.writeValueAsString(expectedUpdatedUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(expectedException.getMessage()));
    }

    @Test
    void testThrowRuntimeExceptionWhenUpdateAllDataByIdWithOnlyEmail() throws Exception{
        userService.createData(mockUserWithEmail);

        User expectedUpdatedUser = new User("updatedName", "updatedEmail");
        UserInformationRequired expectedException = new UserInformationRequired("name");

        when(userService.updateData(any(Integer.class), any(User.class))).thenThrow(expectedException);

        mockMvc.perform( MockMvcRequestBuilders
                        .put(PREFIX_USERS_API + "/{id}", mockUserWithName.getId())
                        .content(mapper.writeValueAsString(expectedUpdatedUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(expectedException.getMessage()));
    }

    @Test
    void testUpdatePartialDataByIdWithNonExistingId() throws Exception {
        User expectedUpdatedUser = new User("updatedName", "updatedEmail");
        expectedUpdatedUser.setId(NON_EXISTING_ID);

        UserNotFoundException expectedException = new UserNotFoundException(String.valueOf(expectedUpdatedUser.getId()));

        when(userService.updatePartialData(any(Integer.class), any(User.class))).thenThrow(expectedException);

        mockMvc.perform( MockMvcRequestBuilders
                        .patch(PREFIX_USERS_API + "/{id}", expectedUpdatedUser.getId())
                        .content(mapper.writeValueAsString(expectedUpdatedUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(expectedException.getMessage()));
    }

    @Test
    void testUpdatePartialDataByIdWithExistingId() throws Exception{
        userService.createData(mockUserWithEmail);

        User expectedUpdatedUser = new User("updatedName", "updatedEmail");
        expectedUpdatedUser.setId(NON_EXISTING_ID);

        when(userService.updatePartialData(any(Integer.class), any(User.class))).thenReturn(expectedUpdatedUser);

        mockMvc.perform( MockMvcRequestBuilders
                        .patch(PREFIX_USERS_API + "/{id}", expectedUpdatedUser.getId())
                        .content(mapper.writeValueAsString(expectedUpdatedUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(expectedUpdatedUser.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(expectedUpdatedUser.getEmail()));
    }

    @Test
    void testUpdatePartialDataByIdWithIdAsZeroAndNegativeNumber() throws  Exception{
        User expectedUpdatedUser = new User("updatedName", "updatedEmail");
        expectedUpdatedUser.setId(NEGATIVE_NUMBER_ID);

        IllegalArgumentException expectedException = new IllegalArgumentException("Invalid input: " + expectedUpdatedUser.getId());

        when(userService.updatePartialData(any(Integer.class), any(User.class))).thenThrow(expectedException);

        mockMvc.perform( MockMvcRequestBuilders
                        .patch(PREFIX_USERS_API + "/{id}", expectedUpdatedUser.getId())
                        .content(mapper.writeValueAsString(expectedUpdatedUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(expectedException.getMessage()));

    }

    @Test
    void testUpdatePartialDataByIdWithNullAllInformation() throws Exception{
        User expectedUpdatedUser = new User(null, null);
        expectedUpdatedUser.setId(1);

        UserInformationRequired expectedException = new UserInformationRequired("name and email");

        when(userService.updatePartialData(any(Integer.class), any(User.class))).thenThrow(expectedException);

        mockMvc.perform( MockMvcRequestBuilders
                        .patch(PREFIX_USERS_API + "/{id}", expectedUpdatedUser.getId())
                        .content(mapper.writeValueAsString(expectedUpdatedUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(expectedException.getMessage()));
    }

    @Test
    void testDeleteDataByIdWithExistingId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(PREFIX_USERS_API + "/{id}", mockUserWithNameAndEmail.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        verify(userService, times(1)).deleteDataById(mockUserWithNameAndEmail.getId());
    }

    @Test
    void testDeleteDataByIdWithNonExistingId() throws Exception {
        UserNotFoundException expectedException = new UserNotFoundException(String.valueOf(mockUserWithNameAndEmail.getId()));

        doThrow(expectedException).when(userService).deleteDataById(mockUserWithNameAndEmail.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(PREFIX_USERS_API + "/{id}", mockUserWithNameAndEmail.getId()) )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(print());
    }

    @Test
    void testDeleteDataByIdWithIdAsZeroAndNegativeNumber() throws Exception {
        User expectedDeletedUser = new User("updatedName", "updatedEmail");
        expectedDeletedUser.setId(NEGATIVE_NUMBER_ID);

        IllegalArgumentException expectedException = new IllegalArgumentException("Invalid input: " + expectedDeletedUser.getId());

        doThrow(expectedException).when(userService).deleteDataById(expectedDeletedUser.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(PREFIX_USERS_API + "/{id}", expectedDeletedUser.getId()) )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print());
    }

}