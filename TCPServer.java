import java.io.*;
import java.net.*;

public class TCPServer {

    public static void main(String[] args) {

        try {

            ServerSocket server =
                    new ServerSocket(8080);

            System.out.println("==============================");
            System.out.println("         TCP SERVER");
            System.out.println("==============================");
            System.out.println("Server started.");
            System.out.println("Listening on port 8080...");
            System.out.println("Waiting for client...");

            Socket socket = server.accept();

            System.out.println("\nClient connected!");
            System.out.println("Client IP: "
                    + socket.getInetAddress().getHostAddress());

            DataInputStream in =
                    new DataInputStream(socket.getInputStream());

            DataOutputStream out =
                    new DataOutputStream(socket.getOutputStream());

            while (true) {

                // Receive command
                String command = in.readUTF();

                System.out.println("\nReceived command: "
                        + command);

                // ======================================
                // HELLO
                // ======================================

                if (command.startsWith("HELLO|")) {

                    String name =
                            command.substring(6);

                    System.out.println(
                            "Client name: " + name);

                    out.writeUTF(
                            "Hello " + name +
                            "! Nice to meet you.");

                    out.flush();
                }

                // ======================================
                // ARITHMETIC
                // ======================================

                else if (command.startsWith("ARITH|")) {

                    String data =
                            command.substring(6);

                    String[] parts =
                            data.split("\\|");

                    double a =
                            Double.parseDouble(parts[0]);

                    char op =
                            parts[1].charAt(0);

                    double b =
                            Double.parseDouble(parts[2]);

                    double result = 0;

                    if (op == '+')
                        result = a + b;

                    else if (op == '-')
                        result = a - b;

                    else if (op == '*')
                        result = a * b;

                    else if (op == '/') {

                        if (b == 0) {

                            out.writeUTF(
                                    "Division by zero!");

                            out.flush();

                            continue;
                        }

                        result = a / b;
                    }

                    else {

                        out.writeUTF(
                                "Invalid operator!");

                        out.flush();

                        continue;
                    }

                    System.out.println(
                            "Expression: "
                                    + a + " "
                                    + op + " "
                                    + b);

                    System.out.println(
                            "Result: " + result);

                    out.writeUTF(
                            "Result = " + result);

                    out.flush();
                }

                // ======================================
                // TRIGONOMETRIC
                // ======================================

                else if (command.startsWith("TRIG|")) {

                    String data =
                            command.substring(5);

                    String[] parts =
                            data.split("\\|");

                    String function =
                            parts[0];

                    double angle =
                            Double.parseDouble(parts[1]);

                    double radians =
                            Math.toRadians(angle);

                    double result;

                    if (function.equals("sin")) {

                        result =
                                Math.sin(radians);
                    }

                    else if (function.equals("cos")) {

                        result =
                                Math.cos(radians);
                    }

                    else if (function.equals("tan")) {

                        result =
                                Math.tan(radians);
                    }

                    else {

                        out.writeUTF(
                                "Invalid function!");

                        out.flush();

                        continue;
                    }

                    System.out.println(
                            "Function: "
                                    + function);

                    System.out.println(
                            "Angle: "
                                    + angle);

                    System.out.println(
                            "Result: "
                                    + result);

                    out.writeUTF(
                            function
                                    + "("
                                    + angle
                                    + ") = "
                                    + result);

                    out.flush();
                }

                // ======================================
                // FILE
                // ======================================

                else if (command.startsWith("FILE|")) {

                    String data =
                            command.substring(5);

                    String[] parts =
                            data.split("\\|");

                    String fileName =
                            parts[0];

                    long fileSize =
                            Long.parseLong(parts[1]);

                    System.out.println(
                            "\nReceiving file: "
                                    + fileName);

                    System.out.println(
                            "File size: "
                                    + fileSize
                                    + " bytes");

                    FileOutputStream fileOut =
                            new FileOutputStream(
                                    "received_" + fileName);

                    byte[] buffer =
                            new byte[4096];

                    long totalReceived = 0;

                    while (totalReceived < fileSize) {

                        int bytesToRead =
                                (int)Math.min(
                                        buffer.length,
                                        fileSize
                                                - totalReceived);

                        int bytesRead =
                                in.read(
                                        buffer,
                                        0,
                                        bytesToRead);

                        if (bytesRead == -1)
                            break;

                        fileOut.write(
                                buffer,
                                0,
                                bytesRead);

                        totalReceived +=
                                bytesRead;
                    }

                    fileOut.close();

                    System.out.println(
                            "File received successfully!");

                    System.out.println(
                            "Saved as: received_"
                                    + fileName);

                    out.writeUTF(
                            "File received successfully!");

                    out.flush();
                }

                // ======================================
                // EXIT
                // ======================================

                else if (command.equals("EXIT")) {

                    System.out.println(
                            "Client disconnected.");

                    break;
                }

                // ======================================
                // UNKNOWN
                // ======================================

                else {

                    System.out.println(
                            "Unknown command: ["
                                    + command + "]");

                    out.writeUTF(
                            "Unknown command.");

                    out.flush();
                }
            }

            socket.close();
            server.close();

        }
        catch (Exception e) {

            System.out.println(
                    "Server Error: "
                            + e.getMessage());
        }
    }
}