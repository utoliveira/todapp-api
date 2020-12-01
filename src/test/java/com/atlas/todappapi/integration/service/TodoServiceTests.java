package com.atlas.todappapi.integration.service;

import com.atlas.todappapi.bean.service.TodoService;
import com.atlas.todappapi.entity.Todo;
import com.atlas.todappapi.entity.User;
import com.atlas.todappapi.repository.TodoRepository;
import com.atlas.todappapi.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TodoServiceTests {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private TodoService todoService;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private Authentication authentication;

    private String email = "teste@sememailpfvrteste.com";

    @Before
    public void authConfig(){
        testEntityManager.clear();
        User user = User.builder()
                .email(email)
                .password("123")
                .name("Teste")
                .active(true).build();
        testEntityManager.persistAndFlush(user);

        doReturn(email).when(authentication).getName();
    }

    @Test
    public void add_shouldAdd() throws Exception{

        Todo todoOnBD = todoService.add("Teste", authentication);
        assertNotNull(todoOnBD.getId());
    }

    @Test
    public void delete_shouldDelete(){
        User user = userRepository.findByEmail(authentication.getName());
        Todo todo = Todo.builder().text("test").user(user).build();

        testEntityManager.persistAndFlush(todo);
        testEntityManager.refresh(user);

        todoService.delete(todo.getId(), authentication);
        assertTrue(todoRepository.findById(todo.getId()).isEmpty());

    }

    @Test
    public void changeComplete_shouldChange(){
        User user = userRepository.findByEmail(authentication.getName());
        Todo todo = Todo.builder().text("test").user(user).completed(false).build();

        testEntityManager.persistAndFlush(todo);
        testEntityManager.refresh(user);

        todoService.changeComplete(todo.getId(), authentication);
        assertTrue(todo.isCompleted());
    }


}
