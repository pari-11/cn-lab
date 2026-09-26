import java.io.*;
import java.net.*;

public class UDPFileServer {

    static final int PORT = 5000;
    static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {

        try {
            DatagramSocket socket = new DatagramSocket(PORT);

            System.out.println("UDP File Server started...");
            System.out.println("Waiting for client...");

            // Receive file name
            byte[] buffer = new byte[BUFFER_SIZE];

            DatagramPacket namePacket =
                    new DatagramPacket(buffer, buffer.length);

            socket.receive(namePacket);

            String fileName = new String(
                    namePacket.getData(),
                    0,
                    namePacket.getLength()
            );

            System.out.println("Receiving file: " + fileName);

            // Send acknowledgement for filename
            byte[] ackData = "ACK_NAME".getBytes();

            DatagramPacket ackPacket =
                    new DatagramPacket(
                            ackData,
                            ackData.length,
                            namePacket.getAddress(),
                            namePacket.getPort()
                    );

            socket.send(ackPacket);

            // Create output file
            FileOutputStream fos =
                    new FileOutputStream("received_" + fileName);

            int expectedSequence = 0;

            // Receive file packets
            while (true) {

                buffer = new byte[BUFFER_SIZE + 4];

                DatagramPacket packet =
                        new DatagramPacket(buffer, buffer.length);

                socket.receive(packet);

                ByteArrayInputStream bais =
                        new ByteArrayInputStream(
                                packet.getData(),
                                0,
                                packet.getLength()
                        );

                DataInputStream dis =
                        new DataInputStream(bais);

                int sequenceNumber = dis.readInt();

                // EOF
                if (sequenceNumber == -1) {

                    System.out.println("End of file received.");

                    byte[] eofAck = "ACK_EOF".getBytes();

                    DatagramPacket eofAckPacket =
                            new DatagramPacket(
                                    eofAck,
                                    eofAck.length,
                                    packet.getAddress(),
                                    packet.getPort()
                            );

                    socket.send(eofAckPacket);

                    break;
                }

                int dataLength = packet.getLength() - 4;

                byte[] fileData = new byte[dataLength];

                dis.readFully(fileData);

                System.out.println(
                        "Received packet: " + sequenceNumber
                );

                if (sequenceNumber == expectedSequence) {

                    // Write file data
                    fos.write(fileData);

                    // Send ACK
                    String ackMessage =
                            "ACK_" + sequenceNumber;

                    byte[] ack = ackMessage.getBytes();

                    DatagramPacket dataAck =
                            new DatagramPacket(
                                    ack,
                                    ack.length,
                                    packet.getAddress(),
                                    packet.getPort()
                            );

                    socket.send(dataAck);

                    expectedSequence++;

                } else {

                    // Out-of-order packet
                    System.out.println(
                            "Unexpected packet. Expected: "
                            + expectedSequence
                    );

                    String ackMessage =
                            "ACK_" + (expectedSequence - 1);

                    byte[] ack = ackMessage.getBytes();

                    DatagramPacket dataAck =
                            new DatagramPacket(
                                    ack,
                                    ack.length,
                                    packet.getAddress(),
                                    packet.getPort()
                            );

                    socket.send(dataAck);
                }
            }

            fos.close();
            socket.close();

            System.out.println("File received successfully!");
            System.out.println(
                    "Saved as: received_" + fileName
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}