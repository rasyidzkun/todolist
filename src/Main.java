import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.xml.transform.Source;

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

            redColor("Jika gagal dalam menjalankan beberapa perintah, recompile file Main.java\n");
            // https://bugs.java.com/bugdatabase/view_bug.do?bug_id=6213298
            // https://coderanch.com/t/595269/java/doesn-File-renameTo-work

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
                    showTodoList();
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
            addTodoList();
            return;
        }

        System.out.printf("%n%-30s%-20s%s%n", "TODO LIST", "STATUS", "DATE CREATED");
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
        System.out.printf("%s%n%n", "--------------------------------------------------------------------------");
    }

    static void addTodoList() throws IOException {
        FileWriter fileOutput = new FileWriter("todolist.txt", true);
        BufferedWriter bufferedOutput = new BufferedWriter(fileOutput);

        System.out.println("Apa yang ingin kamu kerjakan?");
        System.out.print("Jawab\t\t\t: ");

        input.skip("[\r\n]+");
        String todo = input.nextLine();

        String date = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").format(new Date());
        String colorDate = ANSI_GREEN + date + ANSI_WHITE;

        System.out.print("Completed (y/n) ?\t: ");
        String completed = input.next();

        String isCompleted = "";
        try {
            while (!completed.equalsIgnoreCase("y") && !completed.equalsIgnoreCase("n")) {
                redColor("Masukkan (y/n) ");
                System.out.print("Completed (y/n) ? : ");
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

    static void editTodoList() throws IOException {
        File todo = new File("todolist.txt");
        FileReader fileInput = new FileReader(todo);
        BufferedReader bufferedInput = new BufferedReader(fileInput);

        File temp = new File("tempTodo.txt");
        FileWriter fileOutput = new FileWriter(temp);
        BufferedWriter bufferedOutput = new BufferedWriter(fileOutput);

        showTodoList();

        System.out.println("Masukkan index yang akan diubah");
        System.out.print("Jawab : ");
        int editIndex = input.nextInt();

        String date = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").format(new Date());
        String colorDate = ANSI_GREEN + date + ANSI_WHITE;

        int index = 0;

        String data = bufferedInput.readLine();

        while (data != null) {
            index++;

            StringTokenizer stringToken = new StringTokenizer(data, ",");

            if (editIndex == index) {
                clearScreen();
                greenColor("\nData yang akan diubah adalah");
                System.out.printf("%n%-30s%-20s%s%n", "TODO LIST", "STATUS", "DATE CREATED");
                System.out.printf("%s%n", "--------------------------------------------------------------------------");
                System.out.printf("[%d] ", index);
                System.out.printf("%-25s", stringToken.nextToken());
                System.out.printf("%-17s", stringToken.nextToken());
                System.out.printf("%s%n", stringToken.nextToken());
                System.out.printf("%s%n", "--------------------------------------------------------------------------");

                String[] fieldData = { "TODO", "STATUS" };
                String[] tempData = new String[2];

                stringToken = new StringTokenizer(data, ",");

                boolean isUpdate = yesOrNo("Apakah anda ingin merubah " + fieldData[0] + " (y/n) ?\t: ");
                String originalData = stringToken.nextToken();

                if (isUpdate) {
                    System.out.print("Masukkan " + fieldData[0] + " baru\t\t\t: ");
                    input.skip("[\r\n]+");
                    tempData[0] = input.nextLine();
                } else {
                    tempData[0] = originalData;
                }

                isUpdate = yesOrNo("Apakah anda ingin merubah " + fieldData[1] + " (y/n) ?: ");
                originalData = stringToken.nextToken();

                if (isUpdate) {
                    System.out.print("Completed (y/n) ?\t\t\t: ");
                    tempData[1] = input.next();

                    while (!tempData[1].equalsIgnoreCase("y") && !tempData[1].equalsIgnoreCase("n")) {
                        redColor("Masukkan (y/n) ");
                        System.out.print("Completed (y/n) ?\t\t\t: ");
                        tempData[1] = input.next();
                    }

                    if (tempData[1].equalsIgnoreCase("y")) {
                        tempData[1] = "completed";
                    } else {
                        tempData[1] = "not completed";
                    }

                } else {
                    tempData[1] = originalData;
                }

                String TODO = tempData[0];
                String STATUS = tempData[1];

                bufferedOutput.write(TODO + "," + STATUS + "," + colorDate);

            } else {
                redColor("Anda memasukkan index yang salah");

                bufferedOutput.write(data);
            }

            bufferedOutput.newLine();

            data = bufferedInput.readLine();
        }
        bufferedOutput.flush();

        bufferedOutput.close();
        fileOutput.close();
        bufferedInput.close();
        fileInput.close();

        System.gc();

        todo.delete();
        temp.renameTo(todo);
    }

    static void deleteTodoList() throws IOException {
        File todo = new File("todolist.txt");
        FileReader fileInput = new FileReader(todo);
        BufferedReader bufferedInput = new BufferedReader(fileInput);

        File temp = new File("tempTodo.txt");
        FileWriter fileOutput = new FileWriter(temp);
        BufferedWriter bufferedOutput = new BufferedWriter(fileOutput);

        showTodoList();

        System.out.println("Masukkan index yang akan dihapus");
        System.out.print("Jawab : ");
        int deleteIndex = input.nextInt();

        boolean isFound = false;
        int index = 0;

        String data = bufferedInput.readLine();

        while (data != null) {
            index++;
            boolean isDelete = false;

            StringTokenizer stringToken = new StringTokenizer(data, ",");

            if (deleteIndex == index) {
                clearScreen();
                greenColor("\nData yang akan dihapus adalah");
                System.out.printf("%n%-30s%-20s%s%n", "TODO LIST", "STATUS", "DATE CREATED");
                System.out.printf("%s%n", "--------------------------------------------------------------------------");
                System.out.printf("[%d] ", index);
                System.out.printf("%-25s", stringToken.nextToken());
                System.out.printf("%-17s", stringToken.nextToken());
                System.out.printf("%s%n", stringToken.nextToken());
                System.out.printf("%s%n", "--------------------------------------------------------------------------");

                isDelete = yesOrNo("Apakah anda yakin ingin menghapus (y/n) ? : ");
                isFound = true;
            }

            if (isDelete) {
                greenColor("Data berhasil dihapus");
            } else {
                bufferedOutput.write(data);
                bufferedOutput.newLine();
            }

            data = bufferedInput.readLine();
        }

        if (!isFound) {
            redColor("Data tidak ditemukan");
        }

        bufferedOutput.flush();

        bufferedOutput.close();
        fileOutput.close();
        bufferedInput.close();
        fileInput.close();

        System.gc();

        todo.delete();
        temp.renameTo(todo);
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

    static void greenColor(String message) {
        System.out.println(ANSI_GREEN + message + ANSI_WHITE);
    }
}