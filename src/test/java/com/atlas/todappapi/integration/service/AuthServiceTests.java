package com.atlas.todappapi.integration.service;

import com.atlas.todappapi.bean.service.AuthService;
import com.atlas.todappapi.bean.service.RegisterService;
import com.atlas.todappapi.bean.util.JwtUtil;
import com.atlas.todappapi.model.request.AuthRequest;
import com.atlas.todappapi.model.request.RegisterRequest;
import com.atlas.todappapi.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthServiceTests {

    @Autowired
    private RegisterService registerService;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void auth_shouldAuth(){

        String email = "emailteste@vaiessemeailpfvr.com";
        String password = "12345";
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email(email).password(password).name("Higor").build();

        AuthRequest authRequest = AuthRequest.builder()
                .email(email).password(password).build();

        registerService.register(registerRequest);
        String token = authService.auth(authRequest);
        assertTrue(jwtUtil.validateToken(token, userRepository.findByEmail(email)));
    }

}
