import java.io.*;
import java.net.*;

public class GBNServer {
    private static final int PORT = 6789;
    private static int expectedSeqNum = 0;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected...");

            DataInputStream inFromClient = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());

            while (true) {
                try {
                    int receivedSeq = inFromClient.readInt();
                    System.out.println("Received Packet: " + receivedSeq);

                    synchronized (GBNServer.class) {  // Ensure thread safety
                        if (receivedSeq == expectedSeqNum) {
                            System.out.println("Packet " + receivedSeq + " received correctly.");
                            expectedSeqNum++;
                        } else {
                            System.out.println("Out-of-order packet! Expecting: " + expectedSeqNum);
                        }
                        outToClient.writeInt(expectedSeqNum - 1); // Send cumulative ACK
                    }
                } catch (EOFException e) {
                    System.out.println("Client disconnected.");
                    break;
                }
            }
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
