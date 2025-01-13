package app.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FileTest {

    @BeforeEach
    void before() {
        File.deleteForce("test");
    }

    @AfterEach
    void after() {
        File.deleteForce("test");
    }

    @Test
    @DisplayName("초기 테스트")
    void t1() {
        File.test();
    }

    @Test
    @DisplayName("파일 생성 테스트")
    void t2() {

        String file = "test/test.txt";

        File.createFile(file);

        assertThat(Files.exists(Paths.get(file)))
                .isTrue();
    }

    @Test
    @DisplayName("파일 삭제 테스트")
    void t3() {

        String file = "test/test.txt";

        File.createFile(file);
        assertThat(Files.exists(Paths.get(file)))
                .isTrue();

        File.delete(file);

        assertThat(Files.exists(Paths.get(file)))
                .isFalse();
    }

    @Test
    @DisplayName("파일 내용 불러오기 테스트")
    void t4() {

        String file = "test/test.txt";

        String testContent = "Hello, World!";
        File.write(file, testContent);

        String content = File.readAsString(file);

        assertThat(content)
                .isEqualTo(testContent);
    }

    @Test
    @DisplayName("파일 내용 수정 테스트")
    void t5() {

        String file = "test/test.txt";
        String writeContent = "siu!";

        File.write(file, writeContent);
        String content = File.readAsString(file);

        assertThat(content)
                .isEqualTo(writeContent);
    }

    @Test
    @DisplayName("폴더 생성 테스트")
    void t6() {

        String dirPath = "test";
        File.createDir(dirPath);

        assertThat(Files.exists(Paths.get(dirPath)))
                .isTrue();

        assertThat(Files.isDirectory(Path.of(dirPath)))
                .isTrue();
    }

    @Test
    @DisplayName("폴더 삭제 테스트")
    void t7() {

        String dirPath = "test";

        File.delete(dirPath);

        assertThat(Files.exists(Paths.get(dirPath)))
                .isFalse();
    }

    @Test
    @DisplayName("폴더가 없을 때 파일 생성 테스트")
    void t8() {

        String path = "test/test1/test.txt";
        File.createFile(path);

        boolean result = Files.exists(Paths.get(path));

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("폴더가 비어있지 않을 때 폴더 내부의 항목까지 삭제 테스트")
    void t9() {

        String path = "test/test1/test.txt";
        File.deleteForce(path);

        boolean result = Files.exists(Paths.get(path));

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("파일 목록 가져오기 테스트")
    void t10() {

        String path1 = "test/test1.txt";
        String path2 = "test/test2.txt";
        String path3 = "test/test3.txt";

        File.write(path1, "test1");
        File.write(path2, "test2");
        File.write(path3, "test3");

        assertThat(Files.exists(Paths.get(path1)))
                .isTrue();

        assertThat(Files.exists(Paths.get(path2)))
                .isTrue();

        assertThat(Files.exists(Paths.get(path3)))
                .isTrue();

        List<Path> paths = File.getPaths("test/");

        assertThat(paths)
                .hasSize(3)
                .contains(Paths.get(path1))
                .contains(Paths.get(path2))
                .contains(Paths.get(path3));
    }
}