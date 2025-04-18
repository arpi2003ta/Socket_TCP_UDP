import java.io.*;
import java.net.*;

public class CountClient {
    public static void main(String[] args) throws Exception {
        Socket clientSocket = new Socket("localhost", 6789);
        System.out.println("Connected to server.");

        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        
        BufferedReader inFromServer = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.print("Enter a string (or type 'n' to exit): ");
            String sentence = userInput.readLine();
            outToServer.writeBytes(sentence + "\n");

            // If the user types "No", exit the loop (do not wait for a response)
            if (sentence.equals("n")) {
                break;
            }
            String serverResult = inFromServer.readLine();
            System.out.println("FROM SERVER: " + serverResult);
            
            String serverPrompt = inFromServer.readLine();
            System.out.println(serverPrompt);
        }
        
        // Close the connection when done.
        System.out.println("Closing connection with server.");
        clientSocket.close();
    }
}

