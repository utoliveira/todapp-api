package com.atlas.todappapi.bean.mapper;

import com.atlas.todappapi.entity.Todo;
import com.atlas.todappapi.model.dto.TodoDTO;
import org.springframework.stereotype.Component;

@Component
public class TodoMapper implements IMapper<Todo, TodoDTO> {

    @Override
    public TodoDTO toDTO(Todo entity) {
        if(entity == null) return null;

        return TodoDTO.builder()
                .id(entity.getId())
                .text(entity.getText())
                .complete(entity.isCompleted())
                .build();
    }
}
