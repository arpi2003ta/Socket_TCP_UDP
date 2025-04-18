import java.io.*; 
import java.net.*;  
import java.util.Scanner;

class MultiThreadedClient {
    public static void main(String argv[]) throws Exception {
        String serverIP = "localhost";  // Change to server IP if needed
        int serverPort = 6789;
        
        Socket clientSocket = new Socket(serverIP, serverPort);
        System.out.println("Connected to server on port " + serverPort);

        // Read system time from server
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String serverResponse = inFromServer.readLine();
        System.out.println("Server: " + serverResponse);

        // Send one message to the server
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter message for server: ");
        String message = scanner.nextLine();
        outToServer.writeBytes(message + "\n");

        // Close all resources after sending one message
        scanner.close();
        outToServer.close();
        inFromServer.close();
        clientSocket.close();
        
        System.out.println("Disconnected from server.");
    }
}
