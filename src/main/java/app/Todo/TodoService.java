package app.Todo;

import app.Todo.dto.DeleteResult;
import app.Todo.dto.UpdateResult;
import app.Todo.repository.TodoFileRepository;
import app.Todo.repository.TodoRepository;

import java.util.List;
import java.util.Optional;

public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService() {
        todoRepository = new TodoFileRepository();
    }

    public Todo write(String work, String priority, String status, String notes) {

        Todo todo = new Todo(work, priority, status, notes);
        return todoRepository.save(todo);
    }

    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    public Page<Todo> findAll(int itemsPerPage, int page) {
        return todoRepository.findAll(itemsPerPage, page);
    }

    public DeleteResult deleteById(int delId) {
        return todoRepository.deleteById(delId);
    }

    public DeleteResult deleteByName(String delName) {
        return todoRepository.deleteByName(delName);
    }

    public Optional<UpdateResult> update(int editId, String choice, String editInput) {
        return todoRepository.update(editId, choice, editInput);
    }

    public void build() {
        todoRepository.build();
    }

    public Page<Todo> search(String keywordType, String keyword, int itemsPerPage, int page) {
        return todoRepository.findByKeyword(keywordType, keyword, itemsPerPage, page);
    }

    public void makeSampleData(int i) {
        todoRepository.makeSampleData(i);
    }

    public Todo complete(int id) {
        return todoRepository.complete(id);
    }
}
