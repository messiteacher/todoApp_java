package app.Todo.repository;

import app.Todo.Page;
import app.Todo.Todo;
import app.util.File;
import app.Todo.dto.DeleteResult;
import app.Todo.dto.UpdateResult;
import app.util.Json;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TodoFileRepository implements TodoRepository {

    private static final String DB_PATH = "db/test/";
    private static final String ID_FILE_PATH = DB_PATH + "lastId.txt";
    private static final String BUILD_PATH = DB_PATH + "/build/data.json";

    public void Init() {

        if (!File.exists(DB_PATH)) {
            File.createDir(DB_PATH);
        }

        if (!File.exists(ID_FILE_PATH)) {
            File.createFile(ID_FILE_PATH);
        }
    }

    public TodoFileRepository() {
        Init();
    }

    @Override
    public Todo save(Todo todo) {

        boolean isNew = todo.getId() == 0;
        if (isNew) todo.setId(getLastId() + 1);

        Json.writeAsMap(getFilePath(todo.getId()), todo.toMap());
        if (isNew) setLastId(todo.getId());

        return todo;
    }

    private void setLastId(int id) {
        File.write(ID_FILE_PATH, id);
    }

    public int getLastId() {

        if (!File.exists(ID_FILE_PATH)) return 0;

        String idStr = File.readAsString(ID_FILE_PATH);
        if (idStr.isEmpty()) return 0;

        try {
            return Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public void build() {

        List<Map<String, Object>> mapList = findAll().stream()
                .map(Todo::toMap)
                .toList();

        String jsonStr = Json.listToJson(mapList);

        File.write(BUILD_PATH, jsonStr);
    }

    public static String getBuildPath() {
        return BUILD_PATH;
    }

    @Override
    public int count() {
        return findAll().size();
    }

    public Page findByKeyword(String keywordType, String keyword, int itemsPerPage, int page) {

        List<Todo> searchedWiseSayings = findAll().stream()
                .filter(t -> {
                    return switch (keywordType) {
                        case "todo" -> t.getTodo().contains(keyword);
                        case "priority" -> t.getPriority().contains(keyword);
                        case "status" -> t.getStatus().contains(keyword);
                        case "notes" -> t.getNotes().contains(keyword);
                        default -> false;
                    };
                })
                .sorted(Comparator.comparing(Todo::getId).reversed())
                .toList();

        return pageOf(searchedWiseSayings, itemsPerPage, page);
    }

    @Override
    public List<Todo> findAll() {

        return File.getPaths(DB_PATH).stream()
                .map(Path::toString)
                .filter(path -> path.endsWith(".json"))
                .map(Json::readAsMap)
                .map(Todo::fromMap)
                .collect(Collectors.toList());
    }

    public Page<Todo> findAll(int itemsPerPage, int page) {

        List<Todo> sortedTodos = findAll().stream()
                .sorted(Comparator.comparing(Todo::getId).reversed())
                .toList();

        return pageOf(sortedTodos, itemsPerPage, page);
    }

    @Override
    public void makeSampleData(int count) {

        for (int i = 1; i <= count; i++) {
            Todo todo = new Todo(i, "할일" + i, "중요도" + i, "상태" + i, "메모" + i);
            save(todo);
        }
    }

    @Override
    public Todo complete(int id) {

        Optional<Todo> optionalTodo = findById(id);
        if (optionalTodo.isPresent()) {
            Todo todo = optionalTodo.get();
            todo.setStatus("완료");
            save(todo);
            return todo;
        }
        return null;
    }

    private Page<Todo> pageOf(List<Todo> todos, int itemsPerPage, int page) {

        int totalItems = todos.size();

        List<Todo> pageContent = todos.stream()
                .skip((long) (page - 1) * itemsPerPage)
                .limit(itemsPerPage)
                .collect(Collectors.toList());

        return new Page<>(pageContent, totalItems, itemsPerPage, page);
    }

    @Override
    public DeleteResult deleteById(int delId) {

        String filePath = getFilePath(delId);

        if (!Files.exists(Path.of(filePath))) {
            return new DeleteResult(Optional.empty(), false);
        }

        Optional<Todo> todoToDelete = Optional.ofNullable(Json.readAsMap(filePath))
                .map(Todo::fromMap);

        boolean isDelete = File.delete(filePath);

        if (isDelete) {
            return new DeleteResult(todoToDelete, isDelete);
        } else {
            return new DeleteResult(Optional.empty(), isDelete);
        }
    }

    @Override
    public DeleteResult deleteByName(String delName) {

        Optional<Todo> foundTodo = findTodoByName(delName);
        if (foundTodo.isEmpty()) {
            return new DeleteResult(Optional.empty(), false);
        }

        int todoId = foundTodo.get().getId();

        String filePath = getFilePath(todoId);

        if (!Files.exists(Path.of(filePath))) {
            return new DeleteResult(Optional.empty(), false);
        }

        // 파일을 삭제 시도
        boolean isDelete = File.delete(filePath);

        // 삭제 성공 여부에 따라 결과 반환
        if (isDelete) {
            return new DeleteResult(foundTodo, isDelete);
        } else {
            return new DeleteResult(Optional.empty(), isDelete);
        }
    }

    private Optional<Todo> findTodoByName(String delName) {

        List<Todo> todoList = findAll();

        return todoList.stream()
                .filter(todo -> todo.getTodo().equals(delName))
                .findFirst();
    }

    @Override
    public Optional<UpdateResult> update(int editId, String choice, String editInput) {

        String filePath = getFilePath(editId);

        Optional<Todo> todoToEdit = Optional.ofNullable(Json.readAsMap(filePath))
                .map(Todo::fromMap);

        Todo todo = todoToEdit.get();

        String oldValue = null;
        String newValue = editInput;

        switch (choice) {
            case "할일":
                oldValue = todo.getTodo();
                todo.setTodo(editInput);
                break;
            case "상태":
                oldValue = todo.getStatus();
                todo.setStatus(editInput);
                break;
            case "우선순위":
                oldValue = todo.getPriority();
                todo.setPriority(editInput);
                break;
            case "메모":
                oldValue = todo.getNotes();
                todo.setNotes(editInput);
                break;
            default:
                return Optional.empty();
        }

        Json.writeAsMap(filePath, todo.toMap());

        UpdateResult updateResult = new UpdateResult(choice, oldValue, newValue);
        return Optional.of(updateResult);
    }

    @Override
    public String getFilePath(int id) {
        return Paths.get(DB_PATH, id + ".json").toAbsolutePath().toString();
    }

    @Override
    public Optional<Todo> findById(int id) {

        String path = getFilePath(id);
        Map<String, Object> map = Json.readAsMap(path);

        if (map.isEmpty()) return Optional.empty();

        return Optional.of(Todo.fromMap(map));
    }
}
