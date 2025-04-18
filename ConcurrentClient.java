import java.io.*; 
import java.net.*;  

class ConcurrentClient {
    public static void main(String argv[]) throws Exception {
        String serverIP = "localhost";  // Change to server IP if needed
        int serverPort = 6789;
        
        Socket clientSocket = new Socket(serverIP, serverPort);
        System.out.println("Connected to server on port " + serverPort);

        // Read data from server
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String serverResponse = inFromServer.readLine();
        System.out.println("Server says: " + serverResponse);

        // Close connection
        clientSocket.close();
    }
}