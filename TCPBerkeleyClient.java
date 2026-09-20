import java.io.*;
import java.net.*;
import java.util.*;

public class TCPClient {

    public static void main(String[] args) {

        try {

            Socket socket =
                    new Socket("127.0.0.1", 8080);

            System.out.println("==============================");
            System.out.println("         TCP CLIENT");
            System.out.println("==============================");

            System.out.println(
                    "Connected to server!");

            System.out.println(
                    "Server IP: 127.0.0.1");

            DataInputStream in =
                    new DataInputStream(
                            socket.getInputStream());

            DataOutputStream out =
                    new DataOutputStream(
                            socket.getOutputStream());

            Scanner sc =
                    new Scanner(System.in);

            while (true) {

                System.out.println(
                        "\n========== CLIENT MENU ==========");

                System.out.println(
                        "1. Say Hello");

                System.out.println(
                        "2. Send File");

                System.out.println(
                        "3. Arithmetic Calculator");

                System.out.println(
                        "4. Trigonometric Calculator");

                System.out.println(
                        "5. Exit");

                System.out.print(
                        "\nEnter choice: ");

                int choice =
                        sc.nextInt();

                // ======================================
                // HELLO
                // ======================================

                if (choice == 1) {

                    System.out.print(
                            "\nEnter your name: ");

                    String name =
                            sc.next();

                    // Send EVERYTHING in one message
                    out.writeUTF(
                            "HELLO|" + name);

                    out.flush();

                    String response =
                            in.readUTF();

                    System.out.println(
                            "Server: "
                                    + response);
                }

                // ======================================
                // FILE
                // ======================================

                else if (choice == 2) {

                    System.out.print(
                            "\nEnter filename: ");

                    String fileName =
                            sc.next();

                    File file =
                            new File(fileName);

                    if (!file.exists()) {

                        System.out.println(
                                "File not found!");

                        continue;
                    }

                    long fileSize =
                            file.length();

                    // Send file information
                    out.writeUTF(
                            "FILE|"
                                    + fileName
                                    + "|"
                                    + fileSize);

                    out.flush();

                    FileInputStream fileIn =
                            new FileInputStream(file);

                    byte[] buffer =
                            new byte[4096];

                    int bytesRead;

                    long totalSent = 0;

                    while ((bytesRead =
                            fileIn.read(buffer))
                            != -1) {

                        out.write(
                                buffer,
                                0,
                                bytesRead);

                        totalSent +=
                                bytesRead;
                    }

                    out.flush();

                    fileIn.close();

                    System.out.println(
                            "File sent successfully!");

                    System.out.println(
                            "Bytes sent: "
                                    + totalSent);

                    String response =
                            in.readUTF();

                    System.out.println(
                            "Server: "
                                    + response);
                }

                // ======================================
                // ARITHMETIC
                // ======================================

                else if (choice == 3) {

                    System.out.print(
                            "\nEnter first number: ");

                    double a =
                            sc.nextDouble();

                    System.out.print(
                            "Enter operator (+ - * /): ");

                    char op =
                            sc.next().charAt(0);

                    System.out.print(
                            "Enter second number: ");

                    double b =
                            sc.nextDouble();

                    // Send everything in one message
                    out.writeUTF(
                            "ARITH|"
                                    + a
                                    + "|"
                                    + op
                                    + "|"
                                    + b);

                    out.flush();

                    String response =
                            in.readUTF();

                    System.out.println(
                            "Server: "
                                    + response);
                }

                // ======================================
                // TRIGONOMETRIC
                // ======================================

                else if (choice == 4) {

                    System.out.println(
                            "\n1. sin");

                    System.out.println(
                            "2. cos");

                    System.out.println(
                            "3. tan");

                    System.out.print(
                            "Enter choice: ");

                    int trigChoice =
                            sc.nextInt();

                    String function;

                    if (trigChoice == 1)
                        function = "sin";

                    else if (trigChoice == 2)
                        function = "cos";

                    else if (trigChoice == 3)
                        function = "tan";

                    else {

                        System.out.println(
                                "Invalid choice!");

                        continue;
                    }

                    System.out.print(
                            "Enter angle in degrees: ");

                    double angle =
                            sc.nextDouble();

                    // Send everything in one message
                    out.writeUTF(
                            "TRIG|"
                                    + function
                                    + "|"
                                    + angle);

                    out.flush();

                    String response =
                            in.readUTF();

                    System.out.println(
                            "Server: "
                                    + response);
                }

                // ======================================
                // EXIT
                // ======================================

                else if (choice == 5) {

                    out.writeUTF("EXIT");

                    out.flush();

                    System.out.println(
                            "Client closed.");

                    break;
                }

                else {

                    System.out.println(
                            "Invalid choice!");
                }
            }

            in.close();
            out.close();
            socket.close();

        }
        catch (Exception e) {

            System.out.println(
                    "Client Error: "
                            + e.getMessage());
        }
    }
}