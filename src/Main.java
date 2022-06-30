import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {
    static Scanner input = new Scanner(System.in);
    static String userInput;

    static final String ANSI_RED = "\u001B[31m";
    static final String ANSI_GREEN = "\u001B[32m";
    static final String ANSI_WHITE = "\u001B[37m";

    public static void main(String[] args) throws IOException {
        boolean continueLoop = true;

        while (continueLoop) {
            clearScreen();

            System.out.println("=== TODO LIST APP ===");
            System.out.println("[1] Lihat Todo List");
            System.out.println("[2] Tambah Todo List");
            System.out.println("[3] Edit Todo List");
            System.out.println("[4] Hapus Todo List");
            System.out.println("[0] Keluar");
            System.out.println("---------------------");

            System.out.print("Pilih menu> ");
            userInput = input.next();

            switch (userInput) {
                case "1":
                    showTodoList();
                    break;
                case "2":
                    addTodoList();
                    break;
                case "3":
                    editTodoList();
                    break;
                case "4":
                    deleteTodoList();
                    break;
                case "0":
                    System.exit(0);
                    break;
                default:
                    redColor("Salah pilih menu");
            }

            continueLoop = yesOrNo("\nApakah anda ingin kembali ke menu (y/n) ? : ");

        }
    }

    static void showTodoList() throws IOException {
        FileReader fileInput;
        BufferedReader bufferedInput;

        try {
            fileInput = new FileReader("todolist.txt");
            bufferedInput = new BufferedReader(fileInput);
        } catch (Exception e) {
            redColor("Database tidak ditemukan");
            return;
        }

        System.out.printf("%-30s%-20s%s%n", "TODO LIST", "STATUS", "DATE CREATED");
        System.out.printf("%s%n", "--------------------------------------------------------------------------");

        String data = bufferedInput.readLine();
        int index = 0;
        while (data != null) {
            index++;

            StringTokenizer stringToken = new StringTokenizer(data, ",");
            System.out.printf("[%d] ", index);
            System.out.printf("%-25s", stringToken.nextToken());
            System.out.printf("%-17s", stringToken.nextToken());
            System.out.printf("%s", stringToken.nextToken());
            System.out.println();

            data = bufferedInput.readLine();
        }
        System.out.printf("%s%n", "--------------------------------------------------------------------------");
    }

    static void addTodoList() throws IOException {
        FileWriter fileOutput = new FileWriter("todolist.txt", true);
        BufferedWriter bufferedOutput = new BufferedWriter(fileOutput);

        System.out.println("Apa yang ingin kamu kerjakan?");
        System.out.print("Jawab : ");
        String todo = input.next();

        String date = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").format(new Date());
        String colorDate = ANSI_GREEN + date + ANSI_WHITE;

        System.out.print("Completed (y/n) : ");
        String completed = input.next();

        String isCompleted = "";
        try {
            while (!completed.equalsIgnoreCase("y") && !completed.equalsIgnoreCase("n")) {
                redColor("Masukkan (y/n) ");
                System.out.print("Completed (y/n) : ");
                completed = input.next();
            }

            if (completed.equalsIgnoreCase("y")) {
                isCompleted = "completed";
            } else {
                isCompleted = "not completed";
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        bufferedOutput.write(todo + "," + isCompleted + "," + colorDate);
        bufferedOutput.newLine();
        bufferedOutput.flush();
    }

    static void editTodoList() {
    }

    static void deleteTodoList() {
    }

    static boolean yesOrNo(String message) {
        System.out.print(message);
        userInput = input.next();

        while (!userInput.equalsIgnoreCase("y") && !userInput.equalsIgnoreCase("n")) {
            redColor("\nMasukkan y atau n");
            System.out.print("\n" + message);
            userInput = input.next();
        }

        return userInput.equalsIgnoreCase("y");
    }

    static void clearScreen() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls")
                        .inheritIO()
                        .start()
                        .waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    static void redColor(String message) {
        System.out.println(ANSI_RED + message + ANSI_WHITE);
    }
}