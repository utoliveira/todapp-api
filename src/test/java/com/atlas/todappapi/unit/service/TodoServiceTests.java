package com.atlas.todappapi.unit.service;

import com.atlas.todappapi.bean.service.TodoService;
import com.atlas.todappapi.entity.Todo;
import com.atlas.todappapi.entity.User;
import com.atlas.todappapi.exception.TodoNotFoundException;
import com.atlas.todappapi.repository.TodoRepository;
import com.atlas.todappapi.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TodoService.class)
public class TodoServiceTests {

    @Autowired
    private TodoService todoService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TodoRepository todoRepository;

    @MockBean
    private Authentication authentication;

    @Test
    public void getAll_shouldReturn() throws Exception{
        when(authentication.getName()).thenReturn("teste");

        User user = User.builder()
                .todos(Arrays.asList(Todo.builder().build()))
                .build();

        when(userRepository.findByEmail(BDDMockito.anyString())).thenReturn(user);

        Collection todos = todoService.getAll(authentication);
        assertTrue(!CollectionUtils.isEmpty(todos));
    }

    @Test
    public void getAll_todosNotFound() throws Exception{
        given(authentication.getName()).willReturn("teste");

        User user = User.builder()
                .todos(Collections.emptyList())
                .build();

        given(userRepository.findByEmail(BDDMockito.anyString())).willReturn(user);

        Collection todos = todoService.getAll(authentication);
        assertTrue(CollectionUtils.isEmpty(todos));
    }

    @Test(expected = TodoNotFoundException.class)
    public void delete_notFound() throws Exception{
        given(authentication.getName()).willReturn("teste");

        User user = User.builder()
                .todos(Collections.emptyList())
                .build();

        given(userRepository.findByEmail(BDDMockito.anyString())).willReturn(user);
        todoService.delete(1L, authentication);

    }


    @Test
    public void delete_shouldDelete() throws Exception{
        given(authentication.getName()).willReturn("teste");

        User user = User.builder()
                .todos(Arrays.asList(Todo.builder().id(1L).build()))
                .build();

        given(userRepository.findByEmail(BDDMockito.anyString())).willReturn(user);
        todoService.delete(1L, authentication);
    }

    @Test
    public void changeComplete_shouldChange() throws Exception{
        given(authentication.getName()).willReturn("teste");

        User user = User.builder()
                .todos(Arrays.asList(Todo.builder().id(1L).build()))
                .build();

        given(userRepository.findByEmail(BDDMockito.anyString())).willReturn(user);
        todoService.changeComplete(1L, authentication);
    }

    @Test(expected = TodoNotFoundException.class)
    public void changeComplete_notFound() throws Exception{
        given(authentication.getName())
                .willReturn("teste");

        User user = User.builder()
                .todos(Collections.emptyList())
                .build();

        given(userRepository.findByEmail(BDDMockito.anyString())).willReturn(user);
        todoService.changeComplete(1L, authentication);

    }

}
