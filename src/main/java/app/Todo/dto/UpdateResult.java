package app.Todo.dto;

public class UpdateResult {

    private final String field;
    private final String oldValue;
    private final String newValue;

    public UpdateResult(String field, String oldValue, String newValue) {
        this.field = field;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getField() {
        return field;
    }

    public String getOldValue() {
        return oldValue;
    }

    public String getNewValue() {
        return newValue;
    }
}
