import java.io.*; 
import java.net.*; 
import java.util.Scanner;

class MathClient { 
    public static void main(String argv[]) { 
        try (Socket clientSocket = new Socket("localhost", 6789)) { 
            System.out.println("Connected to Math Server.");

            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            Scanner scanner = new Scanner(System.in);

            while (true) {
                // Read menu from server
                System.out.println(inFromServer.readLine());

                // Get user choice
                System.out.print("Enter choice: ");
                String choice = scanner.nextLine();
                outToServer.writeBytes(choice + "\n");

                if (choice.equals("4")) {
                    System.out.println("Exiting...");
                    break;
                }

                // Read next instruction
                System.out.println(inFromServer.readLine());

                // Get operands from user
                // System.out.print("Enter operands (separated by space): ");
                String operands = scanner.nextLine();
                outToServer.writeBytes(operands + "\n");

                // Read result from server
                System.out.println(inFromServer.readLine());
            }

            scanner.close();
        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}
