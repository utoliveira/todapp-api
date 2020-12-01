package com.atlas.todappapi.integration.service;


import com.atlas.todappapi.bean.service.RegisterService;
import com.atlas.todappapi.entity.User;
import com.atlas.todappapi.exception.EmailAlreadyRegisteredException;
import com.atlas.todappapi.model.request.RegisterRequest;
import com.atlas.todappapi.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RegisterServiceTests {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String email = "higor@esseemailnaoexistepfvr.com";
    private final  String name = "Higor";
    private final  String password="123456";

    @Before
    public void init(){
        testEntityManager.clear();
    }

    @Test(expected = EmailAlreadyRegisteredException.class)
    public void register_emailAlreadyExists(){

        User user = User.builder()
                .email(email).password(password).name(name).build();

        testEntityManager.persist(user);

        RegisterRequest request = RegisterRequest.builder()
                .email(email).password(password).name(name).build();

        registerService.register(request);

    }

    @Test
    public void register_shouldRegister(){

        userRepository.deleteAll();

        RegisterRequest request = RegisterRequest.builder()
                .name(name).password(password).email(email).build();

        registerService.register(request);

        User user = userRepository.findByEmail(email);

        if(user == null){fail("The user must have been registered");}

        assertAll(()-> assertEquals(user.getName(), name),
                () -> assertTrue(passwordEncoder.matches(password, user.getPassword())),
                () -> assertTrue(user.isActive()));

    }
}
