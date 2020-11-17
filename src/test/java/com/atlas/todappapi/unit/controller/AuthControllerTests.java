package com.atlas.todappapi.unit.controller;

import com.atlas.todappapi.bean.controller.AuthController;
import com.atlas.todappapi.bean.service.AuthService;
import com.atlas.todappapi.consts.TodappEndpoint;
import com.atlas.todappapi.exception.PasswordDoesntMatchException;
import com.atlas.todappapi.exception.UserNotFoundException;
import com.atlas.todappapi.model.request.AuthRequest;
import com.atlas.todappapi.model.response.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = AuthController.class)
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    private ObjectMapper objectMapper;

    @Before
    public void init(){
        objectMapper = new ObjectMapper();
    }

    @Test
    public void auth_shouldAuth() throws Exception{

        doReturn("token").when(authService).auth(BDDMockito.any(AuthRequest.class));

        AuthRequest authRequest = AuthRequest.builder()
                .email("teste@email.com").password("12345").build();

        mockMvc.perform(post(TodappEndpoint.AUTH+"/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(authRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("token").isNotEmpty());
    }

    @Test
    public void auth_userNotFound() throws Exception{

        doThrow(UserNotFoundException.class).when(authService).auth(BDDMockito.any(AuthRequest.class));

        AuthRequest authRequest = AuthRequest.builder()
                .email("teste@email.com").password("12345").build();

        mockMvc.perform(post(TodappEndpoint.AUTH+"/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void auth_passwordDoestMatch() throws Exception{

        doThrow(PasswordDoesntMatchException.class).when(authService).auth(BDDMockito.any(AuthRequest.class));

        AuthRequest authRequest = AuthRequest.builder()
                .email("teste@email.com").password("12345").build();

        mockMvc.perform(post(TodappEndpoint.AUTH+"/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isPreconditionFailed());
    }

    @Test
    public void auth_passwordNull() throws Exception{
        AuthRequest authRequest = AuthRequest.builder()
                .email("teste@email.com").password(null).build();

        badRequest(authRequest);
    }

    @Test
    public void auth_passwordBlank() throws Exception{
        AuthRequest authRequest = AuthRequest.builder()
                .email("teste@email.com").password(" ").build();

        badRequest(authRequest);
    }

    @Test
    public void auth_passwordEmpty() throws Exception{
        AuthRequest authRequest = AuthRequest.builder()
                .email("teste@email.com").password("").build();

        badRequest(authRequest);
    }

    @Test
    public void auth_passwordBlowLengthLimit() throws Exception{
        AuthRequest authRequest = AuthRequest.builder()
                .email("teste@email.com").password(getStringLongerThan100()).build();

        badRequest(authRequest);
    }

    @Test
    public void auth_emailNull() throws Exception{
        AuthRequest authRequest = AuthRequest.builder()
                .email(null).password("123").build();

        badRequest(authRequest);
    }

    @Test
    public void auth_emailBlank() throws Exception{
        AuthRequest authRequest = AuthRequest.builder()
                .email(" ").password("123").build();

        badRequest(authRequest);
    }

    @Test
    public void auth_emailEmpty() throws Exception{
        AuthRequest authRequest = AuthRequest.builder()
                .email("").password("123").build();

        badRequest(authRequest);
    }

    @Test
    public void auth_emailBlowLengthLimit() throws Exception{
        AuthRequest authRequest = AuthRequest.builder()
                .email(getStringEmailLongerThan100()).password("123").build();

        badRequest(authRequest);
    }

    private void badRequest(AuthRequest authRequest) throws Exception{
        mockMvc.perform(post(TodappEndpoint.AUTH+"/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isBadRequest());
    }

    private String getStringLongerThan100(){
        return "-----------------------------------------------------------------------------------------------------";
    }

    private String getStringEmailLongerThan100() {
        return "-------------------------------------------------------------------------------------------@gmail.com";
    }
}
