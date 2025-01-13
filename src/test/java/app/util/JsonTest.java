package app.util;

import app.Todo.Todo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JsonTest {

    @Test
    @DisplayName("Map -> Json 변환 테스트")
    void t1() {

        Map<String, Object> map = Map.of("name", "siu");

        String jsonStr = Json.mapToJson(map);

        assertThat(jsonStr)
                .isEqualTo("""
                        {
                            "name" : "siu"
                        }
                        """.stripIndent().trim());
    }

    @Test
    @DisplayName("Map -> Json 변환 테스트 - 속성 2개")
    void t2() {

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", "siu");
        map.put("home", "real");

        String jsonStr = Json.mapToJson(map);

        assertThat(jsonStr)
                .isEqualTo("""
                        {
                            "name" : "siu",
                            "home" : "real"
                        }
                        """.stripIndent().trim());
    }

    @Test
    @DisplayName("Map -> Json 변환 테스트 - 속성이 3개, 문자와 숫자 혼합")
    void t3() {

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", "홍길동");
        map.put("home", "서울");
        map.put("age", 20);

        String jsonStr = Json.mapToJson(map);

        assertThat(jsonStr)
                .isEqualTo("""
                        {
                            "name" : "홍길동",
                            "home" : "서울",
                            "age" : 20
                        }
                        """.stripIndent().trim());
    }

    @Test
    @DisplayName("Todo를 Map으로 변환 -> Json로 변환 테스트")
    void t4() {

        Todo todo = new Todo(1, "aaa", "bbb", "ccc", "ddd");
        Map<String, Object> todoMap = todo.toMap();

        String jsonStr = Json.mapToJson(todoMap);
        String filePath = "test/test.json";
        Json.writeAsMap(filePath, todoMap);

        assertThat(jsonStr)
                .isEqualTo("""
                        {
                            "id" : 1,
                            "todo" : "aaa",
                            "priority" : "bbb",
                            "status" : "ccc",
                            "notes" : "ddd"
                        }
                        """.stripIndent().trim());
    }

    @Test
    @DisplayName("Map -> Json 파일로 저장 테스트")
    void t5() {

        Todo todo = new Todo(1, "aaa", "bbb", "ccc", "ddd");
        Map<String, Object> todoMap = todo.toMap();

        String jsonStr = Json.mapToJson(todoMap);
        String filePath = "test/%d.json".formatted(todo.getId());
        Json.writeAsMap(filePath, todoMap);

        boolean rst = Files.exists(Path.of(filePath));
        assertThat(rst).isTrue();

        String content = File.readAsString(filePath);
        assertThat(content)
                .isEqualTo("""
                        {
                            "id" : 1,
                            "todo" : "aaa",
                            "priority" : "bbb",
                            "status" : "ccc",
                            "notes" : "ddd"
                        }
                        """.stripIndent().trim()
                );
    }

    @Test
    @DisplayName("Json 문자열을 Map으로 변환 테스트")
    void t6() {

        String jsonStr = """
                {
                    "id" : 1,
                    "todo" : "aaa",
                    "priority" : "bbb",
                    "status" : "ccc",
                    "notes" : "ddd"
                }
                """;

        Map<String, Object> map = Json.jsonToMap(jsonStr);

        assertThat(map)
                .hasSize(5)
                .containsEntry("id", 1)
                .containsEntry("todo", "aaa")
                .containsEntry("priority", "bbb")
                .containsEntry("status", "ccc")
                .containsEntry("notes", "ddd");
    }

    @Test
    @DisplayName("파일명을 넘기면 Map으로 읽어오기")
    void t7() {

        String filePath = "test/%d.json".formatted(1);
        Map<String, Object> map = Json.readAsMap(filePath);

        assertThat(map)
                .hasSize(5)
                .containsEntry("id", 1)
                .containsEntry("todo", "aaa")
                .containsEntry("priority", "bbb")
                .containsEntry("status", "ccc")
                .containsEntry("notes", "ddd");
    }

    @Test
    @DisplayName("Map을 Todo 객체로 변환")
    void t8() {

        String filePath = "test/%d.json".formatted(1);
        Map<String, Object> map = Json.readAsMap(filePath);

        Todo wiseSaying = Todo.fromMap(map);

        assertThat(wiseSaying.getId()).isEqualTo(1);
        assertThat(wiseSaying.getTodo()).isEqualTo("aaa");
        assertThat(wiseSaying.getPriority()).isEqualTo("bbb");
        assertThat(wiseSaying.getStatus()).isEqualTo("ccc");
        assertThat(wiseSaying.getNotes()).isEqualTo("ddd");
    }

    @Test
    @DisplayName("wiseSaying list를 json 문자열로 변환")
    void t9() {

        Todo todo1 = new Todo(1, "aaa", "bbb", "ccc", "ddd");
        Todo todo2 = new Todo(2, "eee", "fff", "ggg", "hhh");

        List<Todo> wiseSayings = List.of(todo1, todo2);

        List<Map<String, Object>> mapList = wiseSayings.stream()
                .map(Todo::toMap)
                .toList();

        String jsonStr = Json.listToJson(mapList);

        assertThat(jsonStr)
                .isEqualTo("""
                        [
                            {
                                "id" : 1,
                                "todo" : "aaa",
                                "priority" : "bbb",
                                "status" : "ccc",
                                "notes" : "ddd"
                            },
                            {
                                "id" : 2,
                                "todo" : "eee",
                                "priority" : "fff",
                                "status" : "ggg",
                                "notes" : "hhh"
                            }
                        ]
                        """.stripIndent().trim());
    }
}