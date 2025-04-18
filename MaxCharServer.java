import java.io.*; 
import java.net.*; 

class MaxCharServer { 
    public static void main(String argv[]) { 
        try (ServerSocket skt = new ServerSocket(6789)) { // Ensures ServerSocket is closed
            System.out.println("Server up and running, listening for connections...");

            while (true) { 
                try (Socket connectionSocket = skt.accept()) { // Ensures client socket is closed
                    System.out.println("Client connected.");

                    BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream())); 
                    DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

                    while (true) {
                        System.out.println("Reading from socket...");
                        String clientSentence = inFromClient.readLine(); 

                        if (clientSentence == null || clientSentence.equalsIgnoreCase("No")) {
                            System.out.println("Client disconnected.");
                            break;
                        }

                        System.out.println("Processing: " + clientSentence); 
                        char maxChar = ' ';
                        int maxCount = 0;

                        // Array to store frequency of each letter (a-z)
                        int[] freq = new int[26];

                        for (char c : clientSentence.toCharArray()) {
                            if (Character.isLetter(c)) {
                                c = Character.toLowerCase(c);
                                freq[c - 'a']++; // Increment count for this letter
                                if (freq[c - 'a'] > maxCount) {
                                    maxCount = freq[c - 'a'];
                                    maxChar = c;
                                }
                            }
                        }

                        String response = maxChar + " " + maxCount + '\n';
                        System.out.println("Sending: " + response);
                        outToClient.writeBytes(response);

                        // Asking client if they want to continue
                        outToClient.writeBytes("Do you want to send another string? (Type No to exit)\n");
                    }
                } catch (IOException e) {
                    System.err.println("Error handling client: " + e.getMessage());
                }
            } 
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    } 
}
