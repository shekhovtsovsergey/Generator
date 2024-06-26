package ru.shekhovtsov.generator;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

@Component
public class GenerateAndPush {

    private static final String PROJECT_DIR = "your_github_project_path"; // Замените на путь к вашему проекту
    private static final int NUM_FILES = 10; // Количество файлов для генерации

    @Scheduled(fixedRate = 3600000)
    public void generateAndPush() {
        try {
            // Генерация файлов
            for (int i = 0; i < NUM_FILES; i++) {
                String fileName = "RandomClass_" + i + ".java";
                String filePath = PROJECT_DIR + "/src/main/java/" + fileName;
                File file = new File(filePath);

                // Создание папок, если их нет
                File dir = file.getParentFile();
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                // Создание файла с рандомным содержанием
                FileWriter writer = new FileWriter(file);
                writer.write("public class " + fileName.substring(0, fileName.length() - 5) + " {\n");
                writer.write("    public static void main(String[] args) {\n");
                for (int j = 0; j < new Random().nextInt(5) + 1; j++) {
                    writer.write("        System.out.println(\"Random line!\");\n");
                }
                writer.write("    }\n");
                writer.write("}\n");
                writer.close();
            }

            // Выполнение команд Git
            ProcessBuilder gitAdd = new ProcessBuilder("git", "add", ".");
            gitAdd.directory(new File(PROJECT_DIR));
            Process gitAddProcess = gitAdd.start();
            gitAddProcess.waitFor();

            ProcessBuilder gitCommit = new ProcessBuilder("git", "commit", "-m", "Generated random Java files");
            gitCommit.directory(new File(PROJECT_DIR));
            Process gitCommitProcess = gitCommit.start();
            gitCommitProcess.waitFor();

            ProcessBuilder gitPush = new ProcessBuilder("git", "push");
            gitPush.directory(new File(PROJECT_DIR));
            Process gitPushProcess = gitPush.start();
            gitPushProcess.waitFor();

            System.out.println("Файлы сгенерированы, закомичены и запушены в GitHub!");
            System.out.println("Задача выполнена в " + new Date());
        } catch (IOException | InterruptedException e) {
            System.err.println("Ошибка при выполнении задачи: " + e.getMessage());
        }
    }
}

