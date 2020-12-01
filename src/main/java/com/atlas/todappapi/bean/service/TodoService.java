package com.atlas.todappapi.bean.service;

import com.atlas.todappapi.entity.Todo;
import com.atlas.todappapi.entity.User;
import com.atlas.todappapi.exception.TodoNotFoundException;
import com.atlas.todappapi.repository.TodoRepository;
import com.atlas.todappapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class TodoService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TodoRepository todoRepository;


    @Transactional
    public Todo add(String todoText, Authentication authentication){
        User user = userRepository.findByEmail(authentication.getName());
        Todo todo = Todo.builder().text(todoText).user(user).build();
        return todoRepository.saveAndFlush(todo);
    }

    @Transactional
    public List<Todo> getAll(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName());
        return user.getTodos();
    }

    @Transactional
    public void delete(Long id, Authentication authentication) {
        Todo todo = findTodo(id, authentication);
        todoRepository.delete(todo);
    }

    @Transactional
    public void changeComplete(Long id, Authentication authentication) {
        Todo todo = findTodo(id, authentication);
        todo.setCompleted(!todo.isCompleted());
        todoRepository.saveAndFlush(todo);
    }

    //TODO put it on a util
    private Todo findTodo(Long id, Authentication authentication){
        User user = userRepository.findByEmail(authentication.getName());
        Todo todo = null;

        if(!CollectionUtils.isEmpty(user.getTodos())){
            todo = user.getTodos().stream()
                    .filter(elemento -> id.equals(elemento.getId()))
                    .findFirst().orElse(null);
        }

        if(todo == null) throw new TodoNotFoundException();

        return todo;
    }

}
