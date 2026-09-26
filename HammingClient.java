import java.io.*;
import java.net.*;
import java.util.Scanner;

public class HammingClient {

    public static void main(String[] args) {

        String serverAddress = "localhost";
        int port = 5000;

        Scanner sc = new Scanner(System.in);

        try {

            Socket socket = new Socket(serverAddress, port);

            System.out.println("Connected to Hamming Server.");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true
            );

            System.out.print("Enter 7-bit Hamming code: ");

            String code = sc.nextLine();

            // Validate input
            if (code.length() != 7 || !code.matches("[01]+")) {

                System.out.println(
                        "Invalid input. Please enter exactly 7 bits."
                );

                socket.close();
                return;
            }

            // Send Hamming code to server
            out.println(code);

            System.out.println(
                    "Sent Hamming Code: " + code
            );

            // Receive response from server
            String response1 = in.readLine();
            String response2 = in.readLine();

            System.out.println("\nServer Response:");
            System.out.println(response1);
            System.out.println(response2);

            socket.close();
            sc.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}