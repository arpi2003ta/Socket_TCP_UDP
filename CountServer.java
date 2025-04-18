import java.io.*;
import java.net.*;

public class CountServer {
    public static void main(String[] args) throws Exception {
        // Create a server socket listening on port 6789
        ServerSocket serverSocket = new ServerSocket(6789);
        System.out.println("Server up and running, waiting for a client connection...");

        // Accept one client connection
        Socket connectionSocket = serverSocket.accept();
        System.out.println("Client connected.");

        // Create input and output streams for the connection
        BufferedReader inFromClient = new BufferedReader(
                new InputStreamReader(connectionSocket.getInputStream()));
        DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

        // Communication loop
        while (true) {
            System.out.println("Waiting for client input...");
            String clientSentence = inFromClient.readLine();
            
            // If the connection is closed or client sends "No", break out of the loop.
            if (clientSentence == null || clientSentence.equalsIgnoreCase("No")) {
                System.out.println("Client requested to end the session.");
                break;
            }
            
            System.out.println("Received from client: " + clientSentence);
            
            // Process the input: count the frequency of alphabets (letters only)
            // Convert to lowercase so the count is case-insensitive.
            String lowerCaseSentence = clientSentence.toLowerCase();
            int[] frequencies = new int[26]; // for letters a to z
            for (int i = 0; i < lowerCaseSentence.length(); i++) {
                char ch = lowerCaseSentence.charAt(i);
                if (ch >= 'a' && ch <= 'z') {
                    frequencies[ch - 'a']++;
                }
            }
            
            // Find the letter with the maximum frequency
            int maxCount = 0;
            int maxIndex = -1;
            for (int i = 0; i < 26; i++) {
                if (frequencies[i] > maxCount) {
                    maxCount = frequencies[i];
                    maxIndex = i;
                }
            }
            
            String result;
            if (maxCount == 0) {
                result = "No alphabets found in the input.";
            } else {
                char maxLetter = (char) ('a' + maxIndex);
                result = "Most frequent letter: " + maxLetter + " (Occurrences: " + maxCount + ")";
            }
            
            // Send the result to the client
            outToClient.writeBytes(result + "\n");
            System.out.println("Sent result to client: " + result);
            
            // Ask the client if they want to send another string
            String prompt = "Do you want to send another string? If yes, enter a new string; if not, type 'No'";
            outToClient.writeBytes(prompt + "\n");
            System.out.println("Sent prompt to client.");
        }
        
        // Clean up resources and close the connection.
        System.out.println("Closing connection with client.");
        connectionSocket.close();
        serverSocket.close();
    }
}