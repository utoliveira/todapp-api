package com.atlas.todappapi.unit.service;

import com.atlas.todappapi.bean.config.SecurityConfig;
import com.atlas.todappapi.bean.service.RegisterService;
import com.atlas.todappapi.entity.User;
import com.atlas.todappapi.exception.EmailAlreadyRegisteredException;
import com.atlas.todappapi.model.request.RegisterRequest;
import com.atlas.todappapi.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {RegisterService.class, SecurityConfig.class})
public class RegisterServiceTests {

    @Autowired
    private RegisterService registerService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void register_shouldRegister() throws Exception{

        doReturn(null).when(userRepository).findByEmail(BDDMockito.anyString());

        RegisterRequest request = RegisterRequest.builder()
                .email("Higor").password("12345").email("Higor@email.com").build();

        registerService.register(request);

    }

    @Test(expected = EmailAlreadyRegisteredException.class)
    public void register_emailAlreadyRegistered() throws Exception{

        doReturn(User.builder().build())
                .when(userRepository).findByEmail(BDDMockito.anyString());

        RegisterRequest request = RegisterRequest.builder()
                .email("Higor").password("12345").email("Higor@email.com").build();

        registerService.register(request);
    }

}
