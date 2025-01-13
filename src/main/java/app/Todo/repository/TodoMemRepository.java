//package app.Todo.repository;
//
//import app.Todo.Todo;
//import app.Todo.dto.DeleteResult;
//import app.Todo.dto.UpdateResult;
//import app.Todo.repository.TodoRepository;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//
//public class TodoMemRepository implements TodoRepository {
//
//    private final ArrayList<Todo> todos = new ArrayList<>();
//    private int id;
//
//    public Todo save(Todo todo) {
//
//        int saveId = ++id;
//        todo.setId(saveId);
//        todos.add(todo);
//
//        return todo;
//    }
//
//    public List<Todo> findAll() {
//        return todos;
//    }
//
//    public DeleteResult deleteById(int delId) {
//
//        Optional<Todo> removeTodo = todos.stream()
//                .filter(todo -> todo.getId() == delId)
//                .findFirst();
//
//        boolean result = todos.removeIf(todo -> todo.getId() == delId);
//
//        return new DeleteResult(removeTodo, result);
//    }
//
//    public DeleteResult deleteByName(String delName) {
//
//        Optional<Todo> removeTodo = todos.stream()
//                .filter(todo -> todo.getTodo().equals(delName))
//                .findFirst();
//
//        boolean result = todos.removeIf(todo -> Objects.equals(todo.getTodo(), delName));
//
//        return new DeleteResult(removeTodo, result);
//    }
//
//    public Optional<UpdateResult> update(int editId, String choice, String editInput) {
//
//        Optional<Todo> updateTodo = todos.stream()
//                .filter(todo -> todo.getId() == editId)
//                .findFirst();
//
//        if (updateTodo.isPresent()) {
//
//            Todo todo = updateTodo.get();
//            String oldValue = null;
//
//            switch (choice) {
//
//                case "할일":
//                    oldValue = todo.getTodo();
//                    todo.setTodo(editInput);
//                    return Optional.of(new UpdateResult("할일", oldValue, editInput));
//
//                case "중요도":
//                    oldValue = todo.getPriority();
//                    todo.setPriority(editInput);
//                    return Optional.of(new UpdateResult("중요도", oldValue, editInput));
//
//                case "상태":
//                    oldValue = todo.getStatus();
//                    todo.setStatus(editInput);
//                    return Optional.of(new UpdateResult("상태", oldValue, editInput));
//
//                case "메모":
//                    oldValue = todo.getNotes();
//                    todo.setNotes(editInput);
//                    return Optional.of(new UpdateResult("메모", oldValue, editInput));
//
//                default:
//                    return Optional.empty();
//            }
//        }
//
//        return Optional.empty();
//    }
//}
