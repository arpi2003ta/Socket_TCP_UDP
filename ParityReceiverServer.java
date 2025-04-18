import java.io.*;
import java.net.*;

public class ParityReceiverServer {
    public static void main(String[] args) throws IOException {
        int port = 6789;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server is running on port " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected...");

            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String receivedData = inFromClient.readLine();
            
            if (receivedData == null) continue;

            System.out.println("Received Data with Parity: " + receivedData);

            // Check parity
            int count = 0;
            for (char bit : receivedData.toCharArray()) {
                if (bit == '1') count++;
            }

            if (count % 2 == 0) {
                System.out.println(" Data is UNCORRUPTED");
            } else {
                System.out.println(" Data is CORRUPTED");
            }

            // Close client socket
            clientSocket.close();
        }
    }
}
