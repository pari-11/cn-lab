import java.io.*;
import java.net.*;

public class GBNServer {
    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("GBN Server started...");
        System.out.println("Waiting for client...");

        Socket socket = serverSocket.accept();
        System.out.println("Client connected!\n");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        int expectedFrame = 0;

        while (true) {
            String message = in.readLine();

            if (message == null)
                break;

            if (message.equals("END")) {
                break;
            }

            int frame = Integer.parseInt(message);

            System.out.println("Received Frame " + frame);

            if (frame == expectedFrame) {
                System.out.println("Frame " + frame + " accepted");
                out.println("ACK " + frame);
                expectedFrame++;
            } else {
                System.out.println("Frame " + frame + " discarded");
                out.println("ACK " + (expectedFrame - 1));
            }

            System.out.println();
        }

        System.out.println("Transmission completed.");

        socket.close();
        serverSocket.close();
    }
}