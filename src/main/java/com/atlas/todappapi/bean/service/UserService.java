package com.atlas.todappapi.bean.service;

import com.atlas.todappapi.entity.User;
import com.atlas.todappapi.exception.UserNotFoundException;
import com.atlas.todappapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public UserDetails getUserDetailsByUserId(Long id){
        User user = userRepository.getOne(id);

        if(user == null) throw new UserNotFoundException();

        List<SimpleGrantedAuthority> authorities = user.getRoles()
                .stream().map(role -> new SimpleGrantedAuthority(role.getRole()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

}
