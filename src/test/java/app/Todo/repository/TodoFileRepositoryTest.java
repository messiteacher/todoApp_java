package app.Todo.repository;

import app.Todo.Page;
import app.Todo.Todo;
import app.util.File;
import app.util.Json;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class TodoFileRepositoryTest {

    private final TodoRepository todoRepository = new TodoFileRepository();

    @BeforeEach
    void beforeEach() {
        File.deleteForce("db");
    }

    @AfterEach
    void afterEach() {
        File.deleteForce("db");
    }

    @Test
    @DisplayName("저장 테스트")
    void t1() {

        Todo todo = new Todo("test", "test", "test", "test");
        todoRepository.save(todo);

        String filePath = todoRepository.getFilePath(todo.getId());

        boolean result = Files.exists(Path.of(filePath));
        assertThat(result).isTrue();

        Map<String, Object> map = Json.readAsMap(filePath);
        Todo savedTodo = Todo.fromMap(map);

        assertThat(todo).isEqualTo(savedTodo);
    }

    @Test
    @DisplayName("삭제 테스트")
    void t2() {

        Todo todo = new Todo(1, "aaa", "bbb", "ccc", "ddd");
        todoRepository.save(todo);

        String filePath = todoRepository.getFilePath(todo.getId());

        boolean delResult = todoRepository.deleteById(1).isResult();

        boolean result = Files.exists(Path.of(filePath));
        assertThat(result).isFalse();
        assertThat(delResult).isTrue();
    }

    @Test
    @DisplayName("아이디로 가져오기 테스트")
    void t3() {

        Todo todo = new Todo(1, "aaa", "bbb", "ccc", "ddd");
        todoRepository.save(todo);

        assertThat(Files.exists(Path.of(todoRepository.getFilePath(todo.getId())))).isTrue();

        Optional<Todo> opTodo = todoRepository.findById(1);
        Todo foundTodo = opTodo.orElse(null);

        assertThat(foundTodo).isNotNull();
        assertThat(foundTodo).isEqualTo(todo);
    }

    @Test
    @DisplayName("모든 명언 가져오기 테스트")
    void t4() {

        Todo todo1 = new Todo(1, "aaa1", "bbb1", "ccc1", "ddd1");
        Todo todo2 = new Todo(2, "aaa2", "bbb2", "ccc2", "ddd2");
        Todo todo3 = new Todo(3, "aaa3", "bbb3", "ccc3", "ddd3");

        todoRepository.save(todo1);
        todoRepository.save(todo2);
        todoRepository.save(todo3);

        List<Todo> todos = todoRepository.findAll();

        assertThat(todos).hasSize(3);
        assertThat(todos).contains(todo1, todo2, todo3);
    }

    @Test
    @DisplayName("lastId 가져오기 테스트")
    void t5() {

        Todo todo1 = new Todo("aaa1", "bbb1", "ccc1", "ddd1");
        todoRepository.save(todo1);

        Todo todo2 = new Todo("aaa2", "bbb2", "ccc2", "ddd2");
        todoRepository.save(todo2);

        int lastId = todoRepository.getLastId();

        assertThat(lastId).isEqualTo(todo2.getId());
    }

    @Test
    @DisplayName("build 기능 테스트")
    void t6() {

        Todo todo1 = new Todo("aaa1", "bbb1", "ccc1", "ddd1");
        todoRepository.save(todo1);

        Todo todo2 = new Todo("aaa2", "bbb2", "ccc2", "ddd2");
        todoRepository.save(todo2);

        todoRepository.build();

        String jsonStr = File.readAsString(TodoFileRepository.getBuildPath());

        assertThat(jsonStr)
                .isEqualTo("""
                        [
                            {
                                "id" : 1,
                                "todo" : "aaa1",
                                "priority" : "bbb1",
                                "status" : "ccc1",
                                "notes" : "ddd1"
                            },
                            {
                                "id" : 2,
                                "todo" : "aaa2",
                                "priority" : "bbb2",
                                "status" : "ccc2",
                                "notes" : "ddd2"
                            }
                        ]
                        """.stripIndent().trim());
    }

    @Test
    @DisplayName("현재 저장된 명언의 개수를 가져오는 count")
    void t7() {

        Todo todo1 = new Todo("aaa1", "bbb1", "ccc1", "ddd1");
        todoRepository.save(todo1);

        Todo todo2 = new Todo("aaa2", "bbb2", "ccc2", "ddd2");
        todoRepository.save(todo2);

        int count = todoRepository.count();

        assertThat(count)
                .isEqualTo(2);
    }

    @Test
    @DisplayName("페이지 기능 테스트")
    void t8() {

        Todo todo1 = new Todo("aaa1", "bbb1", "ccc1", "ddd1");
        todoRepository.save(todo1);

        Todo todo2 = new Todo("aaa2", "bbb2", "ccc2", "ddd2");
        todoRepository.save(todo2);

        Todo todo3 = new Todo("aaa3", "bbb3", "ccc3", "ddd3");
        todoRepository.save(todo3);

        int itemsPerPage = 5;
        Page pageContent = todoRepository.findAll(itemsPerPage, 1);

        List<Todo> todos = pageContent.getContent();
        int totalItems = pageContent.getTotalItems();
        int totalPages = pageContent.getTotalPages();

        assertThat(totalItems)
                .isEqualTo(3);

        assertThat(totalPages)
                .isEqualTo(1);
    }
}