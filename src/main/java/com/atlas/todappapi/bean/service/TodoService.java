package com.atlas.todappapi.bean.service;

import com.atlas.todappapi.bean.mapper.IMapper;
import com.atlas.todappapi.entity.Todo;
import com.atlas.todappapi.entity.User;
import com.atlas.todappapi.exception.TodoNotFoundException;
import com.atlas.todappapi.model.dto.TodoDTO;
import com.atlas.todappapi.model.request.TodoAddRequest;
import com.atlas.todappapi.repository.TodoRepository;
import com.atlas.todappapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class TodoService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Transactional
    public List<Todo> getAll(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName());
        return user.getTodos();
    }

    public Todo add(TodoAddRequest request, Authentication authentication) {
        return null;
    }

    @Transactional
    public void delete(Long id, Authentication authentication) {
        Todo todo = findTodo(id, authentication);
        todoRepository.delete(todo);
    }

    public void changeComplete(Long id, Authentication authentication) {
        Todo todo = findTodo(id, authentication);
        todo.setCompleted(!todo.isCompleted());
        todoRepository.saveAndFlush(todo);
    }

    //TODO put it on a util
    private Todo findTodo(Long id, Authentication authentication){
        User user = userRepository.findByEmail(authentication.getName());
        Todo todo = user.getTodos().stream()
                .filter(elemento -> id.equals(elemento.getId()))
                .findFirst().orElse(null);

        if(todo == null)throw new TodoNotFoundException();
        return todo;
    }

}
