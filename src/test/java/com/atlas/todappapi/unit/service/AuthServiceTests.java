package com.atlas.todappapi.unit.service;

import com.atlas.todappapi.bean.service.AuthService;
import com.atlas.todappapi.bean.util.JwtUtil;
import com.atlas.todappapi.entity.User;
import com.atlas.todappapi.exception.PasswordDoesntMatchException;
import com.atlas.todappapi.exception.UserNotFoundException;
import com.atlas.todappapi.model.request.AuthRequest;
import com.atlas.todappapi.repository.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.platform.commons.util.StringUtils;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AuthService.class)
public class AuthServiceTests {

    @Autowired
    private AuthService authService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private AuthRequest request = AuthRequest.builder()
            .email("Teste@email.com").password("1234").build();

    @Test
    public void auth_shouldAuth() throws Exception{
        given(userRepository
                .findByEmail(BDDMockito.anyString()))
                .willReturn(User.builder().id(1L).password("1234").build());

        given(passwordEncoder
                .matches(BDDMockito.anyString(), BDDMockito.anyString()))
                .willReturn(true);

        given(jwtUtil
                .generateToken(BDDMockito.any(User.class)))
                .willReturn("token");

        Assertions.assertTrue(StringUtils.isNotBlank(authService.auth(request)));

    }

    @Test(expected = UserNotFoundException.class)
    public void auth_userNotFound() throws Exception{
        doReturn(null)
                .when(userRepository)
                .findByEmail(BDDMockito.anyString());

        authService.auth(request);
    }

    @Test(expected = PasswordDoesntMatchException.class)
    public void auth_passwordDoesntMatch() throws Exception{
        given(userRepository
                .findByEmail(BDDMockito.anyString()))
                .willReturn(User.builder().password("1234").build());

        given(passwordEncoder
                .matches(BDDMockito.anyString(), BDDMockito.anyString()))
                .willReturn(false);

        authService.auth(request);

    }

}
