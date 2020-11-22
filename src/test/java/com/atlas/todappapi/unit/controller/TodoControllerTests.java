package com.atlas.todappapi.unit.controller;

import com.atlas.todappapi.bean.controller.TodoController;
import com.atlas.todappapi.bean.service.TodoService;
import com.atlas.todappapi.consts.TodappEndpoint;
import com.atlas.todappapi.entity.Todo;
import com.atlas.todappapi.exception.TodoNotFoundException;
import com.atlas.todappapi.model.request.TodoAddRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = TodoController.class)
@WithMockUser
public class TodoControllerTests {

    private static final String ADDTODO_ENDPOINT = TodappEndpoint.TODO + "/add";
    private static final String CHANGECOMPLETE_ENDPOINT = TodappEndpoint.TODO + "/changeComplete";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    private ObjectMapper objectMapper;

    @Before
    public void init() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void getAll_shouldReturn() throws Exception {
        Collection allTodos = Arrays.asList(
                Todo.builder().build(),
                Todo.builder().build());

        doReturn(allTodos)
                .when(todoService)
                .getAll(BDDMockito.any(Authentication.class));

        mockMvc.perform(get(TodappEndpoint.TODO + "/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)));
    }

    @Test
    public void getAll_todosNotFound() throws Exception{
        doReturn(Collections.emptyList())
                .when(todoService)
                .getAll(BDDMockito.any(Authentication.class));

        mockMvc.perform(get(TodappEndpoint.TODO + "/"))
                .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void addTodo_shouldAdd() throws Exception {

        TodoAddRequest request = TodoAddRequest.builder().text("Teste").build();
        Todo todoReturned = Todo.builder().id(1L).build();

        doReturn(todoReturned)
                .when(todoService)
                .add(any(TodoAddRequest.class), any(Authentication.class));

        mockMvc.perform(post(ADDTODO_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty());

    }

    @Test
    public void addTodo_textNull() throws Exception{
        performPostBadRequest(ADDTODO_ENDPOINT,
                TodoAddRequest.builder().text(null).build());
    }

    @Test
    public void addTodo_textEmpty() throws Exception{
        performPostBadRequest(ADDTODO_ENDPOINT,
                TodoAddRequest.builder().text("").build());
    }

    @Test
    public void addTodo_textBlank() throws Exception{
        performPostBadRequest(ADDTODO_ENDPOINT,
                TodoAddRequest.builder().text("    ").build());
    }

    @Test
    public void addTodo_textBlowLengthLimit() throws Exception{
        performPostBadRequest(ADDTODO_ENDPOINT,
                TodoAddRequest.builder().text(getTextLongerThan255()).build());
    }

    @Test
    public void delete_shouldDelete() throws Exception{
        doNothing().when(todoService).delete(BDDMockito.anyLong(), BDDMockito.any(Authentication.class));
    }

    @Test
    public void delete_todoNotFound() throws Exception{
        doThrow(TodoNotFoundException.class)
                .when(todoService)
                .delete(BDDMockito.anyLong(), BDDMockito.any(Authentication.class));

        mockMvc.perform(delete(TodappEndpoint.TODO + "/1"))
            .andExpect(status().isNotFound());
        
    }

    @Test
    public void changeCompleted_shouldChange() throws Exception{
        doNothing()
                .when(todoService)
                .changeComplete(BDDMockito.anyLong(), BDDMockito.any(Authentication.class));

        mockMvc.perform(put(CHANGECOMPLETE_ENDPOINT + "/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void changeCompleted_TodoNotFound() throws Exception{
        doThrow(TodoNotFoundException.class)
                .when(todoService)
                .changeComplete(BDDMockito.anyLong(), BDDMockito.any(Authentication.class));

        mockMvc.perform(put(CHANGECOMPLETE_ENDPOINT + "/1"))
                .andExpect(status().isNotFound());
    }


    private void performPostBadRequest(String endpoint, Object request) throws Exception{
        mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    private String getTextLongerThan255() {
        return "----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";
    }

}
