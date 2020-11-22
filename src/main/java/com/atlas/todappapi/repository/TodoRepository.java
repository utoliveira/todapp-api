package com.atlas.todappapi.repository;

import com.atlas.todappapi.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
