import java.io.*;
import java.net.*;

public class SRServer {
    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(5001);

        System.out.println("Selective Repeat Server started...");
        System.out.println("Waiting for client...");

        Socket socket = serverSocket.accept();

        System.out.println("Client connected!\n");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

        PrintWriter out = new PrintWriter(
                socket.getOutputStream(), true);

        while (true) {

            String message = in.readLine();

            if (message == null || message.equals("END"))
                break;

            int frame = Integer.parseInt(message);

            System.out.println("Received Frame " + frame);
            System.out.println("Frame " + frame + " accepted");

            out.println("ACK " + frame);

            System.out.println();
        }

        System.out.println("Transmission completed.");

        socket.close();
        serverSocket.close();
    }
}