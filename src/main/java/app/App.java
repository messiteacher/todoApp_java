package app;

import app.Todo.Todo;
import app.Todo.TodoController;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

public class App {

    private final Scanner sc;
    private final ArrayList<Todo> todos = new ArrayList<>();
    private int id;
    private final TodoController todoController;

    public App(Scanner sc) {
        this.sc = sc;
        todoController = new TodoController(sc);
        this.id = 0;
    }

    public void run() {

        String work = "";
        String priority = "";
        String status = "";
        String notes = "";

        System.out.println("=== To-Do List ===");
        while (true) {
            System.out.print("선택: ");
            String input = sc.nextLine();

            Command command = new Command(input);
            String actionName = command.getActionName();

            if (input.equals("종료")) {
                System.out.println("종료합니다");
                break;
            } else if (input.equals("등록")) {
                todoController.write();
            } else if (input.startsWith("목록")) {
                todoController.list(command);
            } else if (input.equals("삭제")) {
                todoController.delete();
            } else if (input.equals("수정")) {
                todoController.edit();
            } else if (input.equals("빌드")) {
                todoController.build();
            } else if (input.equals("완료")) {
                todoController.complete();
            } else if (input.equals("메뉴")) {
                showMenu();
            } else {
                System.out.println("잘못된 입력입니다!");
                break;
            }
        }
    }

    private void showMenu() {

        System.out.println("\n=== 메뉴 ===");
        System.out.println("1. 등록: 새로운 할 일을 추가합니다.");
        System.out.println("2. 목록: 현재 저장된 할 일 목록을 표시합니다.");
        System.out.println("3. 삭제: 특정 ID의 할 일을 삭제합니다.");
        System.out.println("4. 수정: 특정 ID의 할 일을 수정합니다.");
        System.out.println("5. 빌드: 데이터들을 하나의 파일로 생성합니다.");
        System.out.println("6. 완료: 특정 ID의 할 일을 완료 상태로 변경합니다.");
        System.out.println("7. 종료: 프로그램을 종료합니다.");
        System.out.println();
    }

    public void makeSampleData(int i) {
        todoController.makeSampleData(i);
    }
}
