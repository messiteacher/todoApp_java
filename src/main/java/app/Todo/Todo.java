package app.Todo;

import lombok.*;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Todo {

    private int id;
    private String todo;
    private String priority;
    private String status;
    private String notes;

    public Todo(String todo, String priority, String status, String notes) {
        this.todo = todo;
        this.priority = priority;
        this.status = status;
        this.notes = notes;
    }

    public static Todo fromMap(Map<String, Object> map) {

        int id = (int)map.get("id");
        String todo = (String)map.get("todo");
        String priority = (String)map.get("priority");
        String status = (String)map.get("status");
        String notes = (String)map.get("notes");

        return new Todo(id, todo, priority, status, notes);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void markAsCompleted() {
        this.status = "완료";
    }

    public Map<String, Object> toMap() {

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("todo", todo);
        map.put("priority", priority);
        map.put("status", status);
        map.put("notes", notes);

        return map;
    }
}
