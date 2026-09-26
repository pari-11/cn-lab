import java.io.*;
import java.net.*;

public class UDPFileClient {

    static final String SERVER_IP = "127.0.0.1";
    static final int SERVER_PORT = 5000;

    static final int CHUNK_SIZE = 1024;
    static final int TIMEOUT = 2000;

    public static void main(String[] args) {

        String fileName = "test.txt";

        try {

            DatagramSocket socket = new DatagramSocket();

            socket.setSoTimeout(TIMEOUT);

            InetAddress serverAddress =
                    InetAddress.getByName(SERVER_IP);

            File file = new File(fileName);

            if (!file.exists()) {
                System.out.println(
                        "File not found: " + fileName
                );
                socket.close();
                return;
            }

            System.out.println(
                    "Sending file: " + fileName
            );

            // --------------------------------
            // Send filename
            // --------------------------------

            byte[] nameData = fileName.getBytes();

            DatagramPacket namePacket =
                    new DatagramPacket(
                            nameData,
                            nameData.length,
                            serverAddress,
                            SERVER_PORT
                    );

            socket.send(namePacket);

            // Receive filename ACK
            byte[] ackBuffer = new byte[1024];

            DatagramPacket ackPacket =
                    new DatagramPacket(
                            ackBuffer,
                            ackBuffer.length
                    );

            socket.receive(ackPacket);

            System.out.println(
                    "Server: " +
                    new String(
                            ackPacket.getData(),
                            0,
                            ackPacket.getLength()
                    )
            );

            // --------------------------------
            // Send file
            // --------------------------------

            FileInputStream fis =
                    new FileInputStream(file);

            byte[] buffer = new byte[CHUNK_SIZE];

            int bytesRead;
            int sequenceNumber = 0;

            while ((bytesRead = fis.read(buffer)) != -1) {

                boolean acknowledged = false;

                while (!acknowledged) {

                    // Create packet
                    ByteArrayOutputStream baos =
                            new ByteArrayOutputStream();

                    DataOutputStream dos =
                            new DataOutputStream(baos);

                    // Sequence number
                    dos.writeInt(sequenceNumber);

                    // File data
                    dos.write(buffer, 0, bytesRead);

                    byte[] packetData =
                            baos.toByteArray();

                    DatagramPacket packet =
                            new DatagramPacket(
                                    packetData,
                                    packetData.length,
                                    serverAddress,
                                    SERVER_PORT
                            );

                    socket.send(packet);

                    System.out.println(
                            "Sent packet: " + sequenceNumber
                    );

                    // Wait for ACK
                    try {

                        ackBuffer = new byte[1024];

                        ackPacket =
                                new DatagramPacket(
                                        ackBuffer,
                                        ackBuffer.length
                                );

                        socket.receive(ackPacket);

                        String response =
                                new String(
                                        ackPacket.getData(),
                                        0,
                                        ackPacket.getLength()
                                );

                        if (response.equals(
                                "ACK_" + sequenceNumber)) {

                            acknowledged = true;
                        }

                    } catch (SocketTimeoutException e) {

                        System.out.println(
                                "Timeout! Resending packet "
                                + sequenceNumber
                        );
                    }
                }

                sequenceNumber++;
            }

            fis.close();

            // --------------------------------
            // Send EOF
            // --------------------------------

            boolean eofAcknowledged = false;

            while (!eofAcknowledged) {

                ByteArrayOutputStream baos =
                        new ByteArrayOutputStream();

                DataOutputStream dos =
                        new DataOutputStream(baos);

                // -1 means end of file
                dos.writeInt(-1);

                byte[] eofData =
                        baos.toByteArray();

                DatagramPacket eofPacket =
                        new DatagramPacket(
                                eofData,
                                eofData.length,
                                serverAddress,
                                SERVER_PORT
                        );

                socket.send(eofPacket);

                System.out.println("Sent EOF packet.");

                try {

                    ackBuffer = new byte[1024];

                    ackPacket =
                            new DatagramPacket(
                                    ackBuffer,
                                    ackBuffer.length
                            );

                    socket.receive(ackPacket);

                    String response =
                            new String(
                                    ackPacket.getData(),
                                    0,
                                    ackPacket.getLength()
                            );

                    if (response.equals("ACK_EOF")) {
                        eofAcknowledged = true;
                    }

                } catch (SocketTimeoutException e) {

                    System.out.println(
                            "EOF ACK timeout. Resending..."
                    );
                }
            }

            socket.close();

            System.out.println(
                    "File sent successfully!"
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}