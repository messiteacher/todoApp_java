package app.util;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

public class File {
    public static void test() {
        System.out.println("파일 테스트");
    }

    public static void createFile(String path) {
        write(path, "");
    }

    public static void createDir(String dirPath) {

        try {
            Files.createDirectories(Paths.get(dirPath));
        } catch (IOException e) {
            System.out.println("디렉토리 생성 실패");
            e.printStackTrace();
        }
    }

    public static boolean delete(String file) {

        Path filePath = Paths.get(file);

        if (!Files.exists(filePath)) return false;

        try {
            Files.delete(filePath);
            return true;
        } catch (IOException e) {

            System.out.println("파일 삭제 실패");
            e.printStackTrace();

            return false;
        }
    }

    public static void write(String file, int content) {
        write(file, String.valueOf(content));
    }

    public static void write(String file, String content) {

        Path filePath = Paths.get(file);

        if (filePath.getParent() != null) {
            createDir(filePath.getParent().toString());
        }

        try {
            Files.writeString(filePath, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.out.println("파일 쓰기 실패");
            e.printStackTrace();
        }
    }

    public static String readAsString(String file) {

        Path filePath = Paths.get(file);

        try {
            return Files.readString(filePath);
        } catch (IOException e) {
            System.out.println("파일 읽기 실패");
            e.printStackTrace();
        }

        return "";
    }

    public static void deleteForce(String path) {

        Path folderPath = Paths.get(path).toAbsolutePath();

        if(!Files.exists(folderPath)) return;

        try {

            Files.walkFileTree(folderPath, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                    Files.delete(file);
                    System.out.println("파일 삭제됨: " + file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {

                    Files.delete(dir);
                    System.out.println("디렉토리 삭제됨: " + dir);
                    return FileVisitResult.CONTINUE;
                }
            });

            System.out.println("폴더와 그 안의 내용이 성공적으로 삭제되었습니다.");
        } catch (IOException e) {
            System.err.println("폴더 삭제 중 오류 발생: " + e.getMessage());
        }
    }

    public static List<Path> getPaths(String dirPathStr) {

        Path dirPath = Paths.get(dirPathStr);

        try {

            return Files.walk(dirPath)
                    .filter(Files::isRegularFile)
                    .toList();
        } catch (Exception e) {
            System.out.println("파일 목록 가져오기 실패");
            e.printStackTrace();
        }

        return List.of();
    }

    public static boolean exists(String path) {
        return Files.exists(Paths.get(path));
    }
}
