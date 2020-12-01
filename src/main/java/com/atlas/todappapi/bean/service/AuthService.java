package com.atlas.todappapi.bean.service;

import com.atlas.todappapi.bean.util.JwtUtil;
import com.atlas.todappapi.entity.User;
import com.atlas.todappapi.exception.PasswordDoesntMatchException;
import com.atlas.todappapi.exception.UserNotFoundException;
import com.atlas.todappapi.model.request.AuthRequest;
import com.atlas.todappapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String auth(AuthRequest request){

        User user = userRepository.findByEmail(request.getEmail());
        if(user == null)
            throw new UserNotFoundException();

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new PasswordDoesntMatchException();

        return jwtUtil.generateToken(user);
    }

}
