import java.io.*;
import java.net.*;

public class HammingServer {

    public static void main(String[] args) {

        int port = 5000;

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Hamming Server started...");
            System.out.println("Waiting for client...");

            Socket socket = serverSocket.accept();

            System.out.println("Client connected!");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true
            );

            // Receive 7-bit Hamming code
            String code = in.readLine();

            System.out.println("Received Hamming Code: " + code);

            // Check if input is exactly 7 bits
            if (code.length() != 7 || !code.matches("[01]+")) {
                out.println("Invalid Hamming code. Enter exactly 7 bits.");
                socket.close();
                return;
            }

            // code = D7 D6 D5 P4 D3 P2 P1
            // Hamming positions from right to left:
            // 7 6 5 4 3 2 1

            int[] bit = new int[8];

            // Convert string positions to Hamming positions
            for (int i = 1; i <= 7; i++) {
                bit[i] = code.charAt(7 - i) - '0';
            }

            // Check P1: positions 1, 3, 5, 7
            int p1 = bit[1] ^ bit[3] ^ bit[5] ^ bit[7];

            // Check P2: positions 2, 3, 6, 7
            int p2 = bit[2] ^ bit[3] ^ bit[6] ^ bit[7];

            // Check P4: positions 4, 5, 6, 7
            int p4 = bit[4] ^ bit[5] ^ bit[6] ^ bit[7];

            // Syndrome = P4 P2 P1
            int errorPosition = p4 * 4 + p2 * 2 + p1;

            String result;

            if (errorPosition == 0) {

                System.out.println("No error detected.");

                result = "No error detected.";

            } else {

                System.out.println(
                        "Error detected at position: " + errorPosition
                );

                // Correct the error
                bit[errorPosition] ^= 1;

                System.out.println("Error corrected.");

                result = "Error detected at position: "
                        + errorPosition + ". Error corrected.";
            }

            // Convert back to D7 D6 D5 P4 D3 P2 P1
            StringBuilder correctedCode = new StringBuilder();

            for (int i = 7; i >= 1; i--) {
                correctedCode.append(bit[i]);
            }

            System.out.println(
                    "Corrected code: " + correctedCode
            );

            // Send result back to client
            out.println(result);
            out.println("Corrected code: " + correctedCode);

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}