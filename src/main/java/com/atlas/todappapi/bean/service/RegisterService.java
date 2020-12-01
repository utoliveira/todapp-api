package com.atlas.todappapi.bean.service;

import com.atlas.todappapi.entity.User;
import com.atlas.todappapi.exception.EmailAlreadyRegisteredException;
import com.atlas.todappapi.model.request.RegisterRequest;
import com.atlas.todappapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(RegisterRequest request) {

        User user = userRepository.findByEmail(request.getEmail());
        if(user != null){
            throw new EmailAlreadyRegisteredException();
        }

        User newUser = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .active(true)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.saveAndFlush(newUser);
    }
}
