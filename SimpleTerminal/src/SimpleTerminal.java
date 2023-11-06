import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Scanner;


public class SimpleTerminal {
    private Path currentDirectory;

    public SimpleTerminal(String folderName) {
        this.currentDirectory = Paths.get(folderName).toAbsolutePath();
        if (!Files.exists(currentDirectory)) {
            try {
                Files.createDirectories(currentDirectory);
            } catch (IOException e) {
                throw new RuntimeException("Не удалось создать каталог: " + currentDirectory, e);
            }
        }
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Простой терминал. Введите команды:");

        while (true) {
            System.out.print(currentDirectory + " $ ");
            String command = scanner.nextLine();
            processCommand(command);
        }
    }

    private void processCommand(String command) {
        String[] parts = command.split("\\s+");
        switch (parts[0]) {
            case "cd":
                changeDirectory(parts.length > 1 ? parts[1] : null);
                break;
            case "ls":
                listDirectory(parts.length > 1 ? parts[1] : null);
                break;
            case "mkdir":
                createDirectory(parts.length > 1 ? parts[1] : null);
                break;
            case "nano":
                createFile(parts.length > 1 ? parts[1] : null);
                break;
            case "rm":
                remove(parts.length > 1 ? parts[1] : null);
                break;
            case "pwd":
                printWorkingDirectory();
                break;
            default:
                System.out.println("Команда не распознана.");
                break;
        }
    }


    private void changeDirectory(String directoryName) {
        if (directoryName == null) {
            System.out.println("Имя каталога требуется для команды cd.");
            return;
        }
        if (directoryName.equals("..")) {
            currentDirectory = currentDirectory.getParent();
            if (currentDirectory == null) {
                currentDirectory = Paths.get("").toAbsolutePath();
            }
        } else {
            Path newDir = currentDirectory.resolve(directoryName);
            if (Files.isDirectory(newDir)) {
                currentDirectory = newDir.toAbsolutePath();
            } else {
                System.out.println("Каталог не найден: " + newDir);
            }
        }
    }


    private void listDirectory(String directoryName) {
        Path dir = directoryName != null ? currentDirectory.resolve(directoryName) : currentDirectory;
        if (Files.isDirectory(dir)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
                for (Path file : stream) {
                    System.out.println(file.getFileName());
                }
            } catch (IOException | DirectoryIteratorException x) {
                System.err.println("Ошибка чтения каталога: " + x.getMessage());
            }
        } else {
            System.out.println("Каталог не найден: " + dir);
        }
    }


    private void createDirectory(String directoryName) {
        if (directoryName == null) {
            System.out.println("Имя каталога требуется для команды mkdir.");
            return;
        }
        Path dir = currentDirectory.resolve(directoryName);
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            System.out.println("Не удалось создать каталог: " + dir);
        }
    }


    private void createFile(String fileName) {
        if (fileName == null) {
            System.out.println("Имя файла требуется для команды nano.");
            return;
        }
        Path file = currentDirectory.resolve(fileName);
        try {
            if (!Files.exists(file)) {
                Files.createFile(file);
            }
        } catch (IOException e) {
            System.out.println("Не удалось создать файл: " + file);
        }
    }

    private void remove(String name) {
        if (name == null) {
            System.out.println("Для команды rm требуется имя файла или каталога.");
            return;
        }
        Path path = currentDirectory.resolve(name);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            // ...
            System.out.println("Не удалось удалить файл или каталог: " + path);
        }
    }


    private void printWorkingDirectory() {
        System.out.println(currentDirectory.toAbsolutePath().normalize());
    }

    public static void main(String[] args) {

        String initialFolder = args.length > 0 ? args[0] : ".";
        SimpleTerminal terminal = new SimpleTerminal(initialFolder);
        terminal.start();
    }
}


