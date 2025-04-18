import java.io.*; 
import java.net.*;  
import java.util.Scanner;

class ParityClient {
    public static void main(String argv[]) throws Exception {
        

        Socket clientSocket = new Socket("localhost", 6789);
        System.out.println("Connected to server.");

        // Take input from user
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter binary data (only 0s and 1s): ");
        String data = scanner.nextLine();

        // Compute even parity bit and append it
        String dataWithParity = addParityBit(data);
        System.out.println("Data Sent with Parity Bit: " + dataWithParity);

        // Send data to server
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        outToServer.writeBytes(dataWithParity + "\n");

        // Close resources
        scanner.close();
        outToServer.close();
        clientSocket.close();
    }

    // Method to compute even parity bit
    public static String addParityBit(String data) {
        int countOnes = 0;

        for (char bit : data.toCharArray()) {
            if (bit == '1') {
                countOnes++;
            }
        }

        // Even parity: Add '1' if count of 1s is odd, else add '0'
        char parityBit = (countOnes % 2 == 0) ? '0' : '1';

        return data + parityBit;  // Append parity bit
    }
}
