import java.io.*; 
import java.net.*; 
import java.util.Scanner; 

class MaxCharClient { 
    public static void main(String argv[]) throws Exception { 
        Scanner scanner = new Scanner(System.in);

        Socket clientSocket = new Socket("localhost", 6789); 
        System.out.println("Client running, connected to server.");

        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream()); 
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 

        while (true) { 
            System.out.print("Enter a string (or type No to exit): ");
            String sentence = scanner.nextLine();

            System.out.println("Writing to socket...");
            outToServer.writeBytes(sentence + '\n'); 

            if (sentence.equalsIgnoreCase("No")) {
                break;
            }

            System.out.println("Reading from socket...");
            String response = inFromServer.readLine(); 
            System.out.println("Most frequent character: " + response);

            // Receiving the server's continuation message
            String serverMessage = inFromServer.readLine();
            System.out.println(serverMessage);
        }

        clientSocket.close(); 
        scanner.close();
    } 
}
