package app.Todo;

import app.Todo.repository.TodoFileRepository;
import app.util.File;
import app.util.TestBot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TodoTest {

    @BeforeEach
    void beforeEach() {
        File.deleteForce("db");
    }

    @AfterEach
    void afterEach() {
        File.deleteForce("db");
    }

//    @Test
//    @DisplayName("최초 실행")
//    void t1() {
//        app.App app = new app.App();
//        app.run();
//    }

    @Test
    @DisplayName("종료")
    void t2() {

        String input = "";
        String output = TestBot.run(input);

        assertThat(output).contains("종료합니다");
    }

    @Test
    @DisplayName("=== To-Do List === 검증")
    void t3() {

        String input = "";
        String output = TestBot.run(input);

        assertThat(output)
                .containsSubsequence("=== To-Do List ===", "종료합니다");
    }

    @Test
    @DisplayName("입력을 여러번 입력할 수 있다.")
    void t4() {

        String input = """
            등록
            공부
            상
            진행 중
            siu
            등록
            게임
            하
            진행 전
            siu
            """;
        String output = TestBot.run(input);

        // "입력: " 횟수 카운트
        long count = output.lines()
                .filter(line -> line.startsWith("선택:"))
                .count();

        // 검증
        assertThat(count).isEqualTo(3);
    }

    @Test
    @DisplayName("등록 입력 기능 검증")
    void t5() {

        String input = """
                등록
                공부
                상
                진행 중
                siu
                """;
        String output = TestBot.run(input);

        assertThat(output).contains("할일: ");
    }

    @Test
    @DisplayName("등록 완료 검증")
    void t6() {

        String input = """
                등록
                공부
                상
                진행 중
                siu
                """;
        String output = TestBot.run(input);

        assertThat(output).contains("할일 1번 공부이(/가) 등록되었습니다.");
    }

    @Test
    @DisplayName("다수 등록 검증")
    void t7() {

        String input = """
                등록
                공부
                상
                진행 중
                siu
                등록
                게임
                하
                진행 전
                siu
                """;
        String output = TestBot.run(input);

        assertThat(output)
                .contains("할일 1번 공부이(/가) 등록되었습니다.")
                .contains("할일 2번 게임이(/가) 등록되었습니다.");
    }

    @Test
    @DisplayName("1개 목록 검증")
    void t8() {

        String input = """
                등록
                공부
                상
                진행 중
                siu
                목록
                """;
        String output = TestBot.run(input);

        assertThat(output)
                .contains("번호 / 할일 / 중요도 / 상태 / 메모")
                .contains("-------------------------------")
                .contains("1 / 공부 / 상 / 진행 중 / siu");
    }

    @Test
    @DisplayName("여러개 목록 검증")
    void t9() {

        String input = """
                등록
                공부
                상
                진행 중
                siu
                등록
                게임
                하
                진행 전
                siu
                목록
                """;
        String output = TestBot.run(input);

        assertThat(output)
                .contains("번호 / 할일 / 중요도 / 상태 / 메모")
                .contains("-------------------------------")
                .containsSubsequence("1 / 공부 / 상 / 진행 중 / siu",
                        "2 / 게임 / 하 / 진행 전 / siu");
    }

    @Test
    @DisplayName("id를 통한 할일 삭제")
    void t10() {

        String input = """
                등록
                공부
                상
                진행 중
                siu
                삭제
                id
                1
                목록
                """;
        String output = TestBot.run(input);

        assertThat(output).contains("할일 1번 공부이(/가) 삭제되었습니다.");
    }

//    @Test
//    @DisplayName("할일 이름을 통한 할일 삭제")
//    void t11() {
//
//        String input = """
//                등록
//                공부
//                상
//                진행 중
//                siu
//                삭제
//                이름
//                공부
//                목록
//                """;
//        String output = TestBot.run(input);
//
//        assertThat(output).contains("할일 1번 공부이(/가) 삭제되었습니다.");
//    }

    @Test
    @DisplayName("할일 수정")
    void t12() {

        String input = """
                등록
                공부
                상
                진행 중
                siu
                수정
                1
                할일
                자바 공부
                목록
                """;
        String output = TestBot.run(input);

        assertThat(output).contains("1번의 할일이(/가) '공부'에서 '자바 공부'으로 수정되었습니다.");
    }

    @Test
    @DisplayName("목록 비어있을 경우 메시지")
    void t13() {

        String input = """
                목록
                """;
        String output = TestBot.run(input);

        assertThat(output).contains("등록된 할 일이 없습니다.");
    }

    @Test
    @DisplayName("할 일을 등록하지 않고 삭제하려는 경우")
    void t14() {

        String input = """
                삭제
                id
                1
                """;
        String output = TestBot.run(input);

        assertThat(output).contains("삭제할 할 일이 없습니다.");
    }

    @Test
    @DisplayName("빌드 기능 테스트")
    void t15() {

        String out = TestBot.run("""
                등록
                공부
                상
                진행 중
                siu
                등록
                게임
                하
                진행 전
                siu
                빌드
                """);

        boolean result = File.exists(TodoFileRepository.getBuildPath());
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("키워드를 통한 검색 기능 테스트")
    void t16() {

        String out = TestBot.run("""
                등록
                공부
                상
                진행 중
                siu
                등록
                게임
                하
                진행 전
                siu
                목록?keywordType=todo&keyword=공부
                """);

        assertThat(out)
                .contains("1 / 공부 / 상 / 진행 중 / siu")
                .doesNotContain("2 / 게임 / 하 / 진행 전 / siu");
    }

    @Test
    @DisplayName("페이징 - 페이징 UI 출력")
    void t17() {

        TestBot.makeSample(10);

        String out = TestBot.run("""
                목록?page=2
                """);

        assertThat(out)
                .contains("1 / [2]");
    }

    @Test
    @DisplayName("페이징 - 실제 페이지에 있는 데이터 가져오기")
    void t18() {

        TestBot.makeSample(15);

        String out = TestBot.run("""
                목록?keywordType=content&keyword=1
                """);

        assertThat(out)
                .containsSubsequence("15 / 할일15 / 중요도15 / 상태15 / 메모15", "14 / 할일14 / 중요도14 / 상태14 / 메모14")
                .doesNotContain("10 / 할일10 / 중요도10 / 상태10 / 메모10");

        assertThat(out)
                .contains("[1] / 2");

    }

    @Test
    @DisplayName("할일 완료 기능 테스트")
    void t19() {

        TestBot.makeSample(1);

        String out = TestBot.run("""
                완료
                1
                """);

        assertThat(out).contains("완료 처리된 할 일: Todo(id=1, todo=할일1, priority=중요도1, status=완료, notes=메모1)");
    }

    @Test
    @DisplayName("메뉴 기능 테스트")
    void t20() {

        String out = TestBot.run("""
                메뉴
                """);

        assertThat(out)
                .containsSubsequence("1. 등록: 새로운 할 일을 추가합니다.",
                        "2. 목록: 현재 저장된 할 일 목록을 표시합니다.",
                        "3. 삭제: 특정 ID의 할 일을 삭제합니다.",
                        "4. 수정: 특정 ID의 할 일을 수정합니다.",
                        "5. 빌드: 데이터들을 하나의 파일로 생성합니다.",
                        "6. 완료: 특정 ID의 할 일을 완료 상태로 변경합니다.",
                        "7. 종료: 프로그램을 종료합니다."
                        );
    }
}
