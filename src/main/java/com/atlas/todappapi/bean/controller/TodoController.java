package com.atlas.todappapi.bean.controller;

import com.atlas.todappapi.bean.mapper.IMapper;
import com.atlas.todappapi.bean.service.TodoService;
import com.atlas.todappapi.consts.TodappEndpoint;
import com.atlas.todappapi.entity.Todo;
import com.atlas.todappapi.exception.TodoNotFoundException;
import com.atlas.todappapi.model.dto.TodoDTO;
import com.atlas.todappapi.model.request.TodoAddRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Collection;

import static com.atlas.todappapi.consts.code.OperationCode.DONT_HAVE_PERMISSION_FOR_THAT;
import static com.atlas.todappapi.factory.ResponseFactory.getSimpleCodeResponse;

@RestController
@RequestMapping(TodappEndpoint.TODO)
public class TodoController {

    @Autowired
    private TodoService todoService;

    @Autowired
    private IMapper<Todo, TodoDTO> todoMapper;

    @PostMapping("/add")
    public ResponseEntity<?> addTodo(@Valid @RequestBody TodoAddRequest request, BindingResult validationResults, Authentication authentication){
        //TODO: change to an aspect based on methods with ResponseEntity and Validation Results
        if(validationResults.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(validationResults.getFieldErrors().stream()
                            .map(field -> field.getField() + ": " + field.getDefaultMessage()));
        }

        Todo todo = todoService.add(request, authentication);
        return ResponseEntity.ok(todoMapper.toDTO(todo));
    }

    @PutMapping("/changeComplete/{todoId}")
    public ResponseEntity<?> changeComplete(@PathVariable Long todoId, Authentication authentication){
        todoService.changeComplete(todoId, authentication);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<?> deleteTodo(@PathVariable Long todoId, Authentication authentication){
        todoService.delete(todoId, authentication);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/")
    public ResponseEntity<?> getAll(Authentication authentication){
        Collection todos = todoService.getAll(authentication);
        return ResponseEntity.ok(todoMapper.toCollectionDTO(todos));
    }

    @ExceptionHandler(TodoNotFoundException.class)
    public ResponseEntity todoNotFoundExceptionHandler(){
        return ResponseEntity.notFound().build();
    }
}
