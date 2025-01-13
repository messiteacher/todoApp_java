package app.Todo.repository;

import app.Todo.Page;
import app.Todo.Todo;
import app.Todo.dto.DeleteResult;
import app.Todo.dto.UpdateResult;

import java.util.List;
import java.util.Optional;

public interface TodoRepository {

    Todo save(Todo todo);

    List<Todo> findAll();

    DeleteResult deleteById(int delId);

    DeleteResult deleteByName(String delName);

    Optional<UpdateResult> update(int editId, String choice, String editInput);

    String getFilePath(int id);

    Optional<Todo> findById(int id);

    int getLastId();

    void build();

    int count();

    Page<Todo> findByKeyword(String keywordType, String kword, int itemsPerPage, int page);

    Page<Todo> findAll(int itemsPerPage, int i);

    void makeSampleData(int i);

    Todo complete(int id);
}
