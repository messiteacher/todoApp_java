package app.Todo;

import app.Command;
import app.Todo.dto.DeleteResult;
import app.Todo.dto.UpdateResult;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class TodoController {

    private final Scanner sc;
    private final TodoService todoService;
    private int itemsPerPage;

    public TodoController(Scanner sc) {
        this.sc = sc;
        todoService = new TodoService();
        itemsPerPage = 5;
    }

    public void write() {

        System.out.print("할일: ");
        String work = sc.nextLine();

        System.out.print("중요도(상 / 중 / 하): ");
        String priority = sc.nextLine();

        System.out.print("상태(진행 전 / 진행 중): ");
        String status = sc.nextLine();

        System.out.print("메모: ");
        String notes = sc.nextLine();

        Todo savedTodo = todoService.write(work, priority, status, notes);

        System.out.println("할일 %d번 %s이(/가) 등록되었습니다.".formatted(savedTodo.getId(), savedTodo.getTodo()));
    }

    public void list(Command command) {

        int page = command.getParamAsInt("page", 1);
        Page<Todo> pageContent;

        if(command.isSearchCommand()) {

            String keywordType = command.getParam("검색타입");
            String keyword = command.getParam("검색어");

            pageContent = todoService.search(keywordType, keyword, itemsPerPage, page);
        } else {
            pageContent = todoService.findAll(itemsPerPage, page);
        }

        printTodo(pageContent, command);
    }

    public void printTodo(Page<Todo> pageContent, Command command) {

        if(pageContent.getContent().isEmpty()) {
            System.out.println("등록된 할일이 없습니다.");
            return;
        }

        if(command.isSearchCommand()) {
            String keywordType = command.getParam("검색타입");
            String keyword = command.getParam("검색어");
            System.out.println("----------------------");
            System.out.println("검색타입 : %s".formatted(keywordType));
            System.out.println("검색어 : %s".formatted(keyword));
            System.out.println("----------------------");
        }
        System.out.println("번호 / 할일 / 중요도 / 상태 / 메모");
        System.out.println("-------------------------------");
        pageContent.getContent().forEach(t -> {
            System.out.printf("%d / %s / %s / %s / %s\n", t.getId(), t.getTodo(), t.getPriority(), t.getStatus(), t.getNotes());
        });

        printPage(pageContent.getPage(), pageContent.getTotalPages());;
    }

    private void printPage(int page, int totalPages) {

        for (int i = 1; i <= totalPages; i++) {

            if (i == page) {
                System.out.print("[%d]".formatted(i));
            } else {
                System.out.print("%d".formatted(i));
            }

            if (i == totalPages) {
                System.out.println();
                break;
            }
            System.out.print(" / ");
        }
    }

    public void delete() {

        System.out.print("삭제할 할일의 기준을 입력하세요 (id/이름): ");
        String delInput = sc.nextLine();

        deleteCheck(delInput);
    }

    private void deleteCheck(String delInput) {
        if (delInput.equals("id")) {

            System.out.print("삭제할 할일의 id를 입력하세요: ");
            int delId = sc.nextInt();
            sc.nextLine();

            DeleteResult deleteResult = todoService.deleteById(delId);

            if (deleteResult.isResult()) {

                Optional<Todo> removedTodo = deleteResult.getRemovedTodo();
                removedTodo.ifPresent(todo -> System.out.println("할일 %d번 %s이(/가) 삭제되었습니다.".formatted(todo.getId(), todo.getTodo())));
            } else {
                System.out.println("삭제할 할 일이 없습니다.");
            }
        } else if (delInput.equals("이름")) {

            System.out.print("삭제할 할일의 이름를 입력하세요: ");
            String delName = sc.nextLine();

            DeleteResult deleteResult = todoService.deleteByName(delName);

            if (deleteResult.isResult()) {

                Optional<Todo> removedTodo = deleteResult.getRemovedTodo();
                removedTodo.ifPresent(todo -> System.out.println("할일 %d번 %s이(/가) 삭제되었습니다.".formatted(todo.getId(), todo.getTodo())));
            } else {
                System.out.println("삭제할 할 일이 없습니다.");
            }
        }
    }

    public void edit() {

        System.out.print("수정하고자 하는 할일의 id를 입력하세요: ");
        int editId = sc.nextInt();
        sc.nextLine();

        System.out.print("수정하고자 하는 항목을 선택하세요(할일, 중요도, 상태, 메모): ");
        String choice = sc.nextLine();

        System.out.print("수정하고자 하는 내용을 입력하세요: ");
        String editInput = sc.nextLine();
        sc.nextLine();

        Optional<UpdateResult> updateResult = todoService.update(editId, choice, editInput);

        if (updateResult.isPresent()) {

            UpdateResult result = updateResult.get();
            System.out.println("%d번의 %s이(/가) '%s'에서 '%s'으로 수정되었습니다.".formatted(
                            editId, result.getField(),
                            result.getOldValue(), result.getNewValue()));
        } else System.out.println("수정에 실패했습니다.");
    }

    public void build() {

        todoService.build();
        System.out.println("data.json 파일의 내용이 갱신되었습니다.");
    }

    public void makeSampleData(int i) {

        todoService.makeSampleData(i);
        System.out.println("샘플 데이터가 생성되었습니다.");
    }

    public void complete() {

        System.out.print("완료 처리할 할일의 id를 입력하세요: ");
        int id = sc.nextInt();
        sc.nextLine();

        Todo completedTodo = todoService.complete(id);
        if (completedTodo != null) {
            System.out.println("완료 처리된 할 일: " + completedTodo);
        } else {
            System.out.println("해당 ID의 할 일이 존재하지 않습니다.");
        }
    }
}
