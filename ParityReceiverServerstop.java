import java.io.*;
import java.net.*;

public class ParityReceiverServerstop {
    public static void main(String[] args) throws IOException {
        int port = 6789;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server is running on port " + port);

        while (true) { // Keeps the server running
            
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected...");

            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());

            int expectedSeq = 0; // Expected sequence number

            String receivedData = inFromClient.readLine();
            if (receivedData == null) continue;
            System.out.println("Received Data: " + receivedData);

            int receivedSeq = receivedData.charAt(0) - '0';  // Extract sequence number
            String data = receivedData.substring(1, receivedData.length() - 1); // Extract actual data
            int receivedParity = receivedData.charAt(receivedData.length() - 1) - '0'; // Extract parity bit

            // Compute parity check
            int count = 0;
            for (char bit : data.toCharArray()) {
                if (bit == '1') count++;
            }
            int computedParity = (count % 2 == 0) ? 0 : 1;

            if (receivedSeq == expectedSeq && computedParity == receivedParity) {
                System.out.println("Data is correct. Sending ACK.");
                outToClient.writeBytes(expectedSeq + "ACK\n");
                expectedSeq = 1 - expectedSeq; // Toggle expected sequence number
            } else {
                System.out.println("Incorrect data! Ignoring frame.");
            }
            
            clientSocket.close(); // Close connection after processing the message
        }
    }
}
