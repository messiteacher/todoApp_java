package app.Todo.dto;

import app.Todo.Todo;

import java.util.Optional;

public class DeleteResult {

    private Optional<Todo> removedTodo;
    private boolean result;

    public DeleteResult(Optional<Todo> removedTodo, boolean result) {
        this.removedTodo = removedTodo;
        this.result = result;
    }

    public Optional<Todo> getRemovedTodo() {
        return removedTodo;
    }

    public boolean isResult() {
        return result;
    }
}
