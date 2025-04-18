import java.io.*;
import java.net.*;

class ParityServer {
    public static void main(String[] args) throws IOException {
        
        ServerSocket serverSocket = new ServerSocket(6789);
        System.out.println("Server is running on port 6789 and waiting for clients...");

        while (true) { // Keep server open
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected from " + clientSocket.getInetAddress());

            // Create a new thread for each client
            Thread clientThread = new Thread(new ClientHandler(clientSocket));
            clientThread.start();
        }
    }
}

// Thread to handle each client connection
class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String receivedData = inFromClient.readLine();

            System.out.println("Received Data: " + receivedData);

            // Parity check
            if (checkParity(receivedData)) {
                System.out.println("Data is UNCORRUPTED ");
            } else {
                System.out.println("Data is CORRUPTED ");
            }

            // Close connection for this client
            inFromClient.close();
            clientSocket.close();
            System.out.println("Client disconnected.\n");
        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        }
    }

    // Method to check if parity is correct (Even Parity)
    public static boolean checkParity(String data) {
        int countOnes = 0;
        for (char bit : data.toCharArray()) {
            if (bit == '1') {
                countOnes++;
            }
        }
        return countOnes % 2 == 0; // Even parity check
    }
}
