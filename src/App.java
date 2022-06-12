import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class App {
    static String filename;
    static ArrayList<String> todolists;
    static boolean isEditing = false;
    static Scanner input;

    public static void main(String[] args) throws Exception {
        todolists = new ArrayList<>();
        input = new Scanner(System.in);

        String filePath = System.console() == null ? "/src/todolist.txt" : "/todolist.txt";
        filename = System.getProperty("user.dir") + filePath; // get home directory

        System.out.println("FILE: " + filename);

        while(true) {
            showMenu();
        }

    }

    static void clearScreen(){
        try {
            final String os = System.getProperty("os.name");
            if(os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls")
                .inheritIO()
                .start()
                .waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    static void showMenu(){
        System.out.println("=== TODO LIST APP ===");
        System.out.println("[1] Lihat Todo List");
        System.out.println("[2] Tambah Todo List");
        System.out.println("[3] Edit Todo List");
        System.out.println("[4] Hapus Todo List");
        System.out.println("[0] Keluar");
        System.out.println("---------------------");
        System.out.print("Pilih menu> ");

        String selectedMenu = input.nextLine();

        if(selectedMenu.equals("1")) {
            showTodoList();
        }else if(selectedMenu.equals("2")) {
            addTodoList();
        } else if(selectedMenu.equals("3")) {
            editTodoList();
        } else if(selectedMenu.equals("4")) {
            deleteTodoList();
        } else if(selectedMenu.equals("0")) {
            System.exit(0);
        } else {
            System.out.println("Salah pilih menu");
            backToMenu();
        }
    }

    static void backToMenu(){
        System.out.println(); 
        System.out.println("Tekan [Enter] untuk kembali..."); 
        input.nextLine();
        clearScreen();
    }

    static void readTodoList(){
        try {
            FileReader file = new FileReader(filename);
            Scanner fileReader = new Scanner(file);

            todolists.clear();
            while(fileReader.hasNextLine()) {
                String data = fileReader.nextLine();
                todolists.add(data);
            }
        }catch(Exception e) {
            System.out.println(e);
        }
    }

    static void showTodoList(){
        clearScreen();
        readTodoList();

        if(todolists.size() > 0) {
            System.out.println("TODO LIST: ");
            int index = 0;
            for(String data : todolists) {
                System.out.println(String.format("[%d] %s", index, data));
                index++;
            }
        }else {
            System.out.println("Tidak ada data!");
        }

        if(!isEditing) {
            backToMenu();
        }
    }

    static void addTodoList(){
        clearScreen();

        System.out.println("Apa yang ingin kamu kerjakan? : ");
        System.out.print("Jawab : ");
        String newTodoList = input.nextLine();

        try {
            FileWriter fileWriter = new FileWriter(filename, true); // true = append the file
            fileWriter.append(String.format("%s%n", newTodoList));
            fileWriter.close();
            System.out.println("Berhasil ditambahkan");
        } catch(Exception e) {
            System.out.println(e);
        } 
        
        backToMenu();
    }

    static void editTodoList(){
        isEditing = true;
        showTodoList();

        try {
            System.out.println("-----------------");
            System.out.print("Pilih index: ");
            int index = Integer.parseInt(input.nextLine());

            if(index > todolists.size()) {
                throw new IndexOutOfBoundsException("Kamu memasukkan data yang salah");
            }else {
                System.out.println("Data baru: ");
                String newData = input.nextLine();

                todolists.set(index, newData);
                System.out.println(todolists.toString());

                try {
                    FileWriter fileWriter = new FileWriter(filename, false); // false == overwrite the file
                    for(String data : todolists) {
                        fileWriter.append(String.format("%s%n", data));
                    }
                    fileWriter.close();
                    System.out.println("Berhasil diubah");

                }catch(Exception e) {
                    System.out.println(e);
                }
            }

        }catch(Exception e) {
            System.out.println(e);
        }

        isEditing = false;
        backToMenu();
    }

    static void deleteTodoList(){
        isEditing = true;
        showTodoList();

        try {
            System.out.println("-----------------");
            System.out.print("Pilih index: ");
            int index = Integer.parseInt(input.nextLine());

            if(index > todolists.size()) {
                throw new IndexOutOfBoundsException("Kamu memasukkan data yang salah");
            }else {
                System.out.println("Kamu akan menghapus: ");
                System.out.println(String.format("[%d] %s", index, todolists.get(index)));
                System.out.println("Apakah kamu yakin?");
                System.out.print("Jawab (y/t): ");
                String jawab = input.nextLine();

                if(jawab.equalsIgnoreCase("y")) {
                    todolists.remove(index);
                }

                try {
                    FileWriter fileWriter = new FileWriter(filename, false); // false = overwrite the file
                    for(String data : todolists) {
                        fileWriter.append(String.format("%s%n", data));
                    }
                    fileWriter.close();

                    System.out.println("Berhasil dihapus");

                }catch(Exception e) {
                    System.out.println(e);
                }
            }

        }catch(Exception e) {
            System.out.println(e);
        }

        isEditing = false;
        backToMenu();
    }

}
