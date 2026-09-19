import java.io.*;
import java.net.*;
import java.util.*;

public class SRClient {
    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        Socket socket = new Socket("localhost", 5001);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

        PrintWriter out = new PrintWriter(
                socket.getOutputStream(), true);

        System.out.println("Connected to Selective Repeat Server!\n");

        System.out.print("Enter number of frames: ");
        int n = sc.nextInt();

        System.out.print("Enter window size: ");
        int windowSize = sc.nextInt();

        System.out.print("Enter frame to simulate loss: ");
        int lostFrame = sc.nextInt();

        int base = 0;

        while (base < n) {

            int end = Math.min(base + windowSize, n);

            System.out.println("\nSending window...");

            for (int i = base; i < end; i++) {

                if (i == lostFrame) {
                    System.out.println("Frame " + i + " LOST!");
                    continue;
                }

                System.out.println("Sending Frame " + i);
                out.println(i);
            }

            for (int i = base; i < end; i++) {

                if (i == lostFrame)
                    continue;

                String response = in.readLine();

                System.out.println("Received: " + response);
            }

            if (lostFrame >= base && lostFrame < end) {

                System.out.println("\nFrame " + lostFrame + " needs retransmission.");

                System.out.println("Selective Repeat: Retransmitting only Frame "
                        + lostFrame);

                out.println(lostFrame);

                String response = in.readLine();

                System.out.println("Received: " + response);

                lostFrame = -1;
            }

            base = end;
        }

        out.println("END");

        System.out.println("\nAll frames transmitted successfully!");

        socket.close();
        sc.close();
    }
}