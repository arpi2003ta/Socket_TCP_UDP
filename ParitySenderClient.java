import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ParitySenderClient {
    public static void main(String[] args) throws IOException {
        String serverIP = "localhost"; 
        int serverPort = 6789;

        Socket socket = new Socket(serverIP, serverPort);
        System.out.println("Connected to server on port " + serverPort);

        DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter binary data: ");
        String data = scanner.nextLine();

        // Compute even parity bit
        int count = 0;
        for (char bit : data.toCharArray()) {
            if (bit == '1') count++;
        }
        String parityBit = (count % 2 == 0) ? "0" : "1";  // Append parity bit

        String dataWithParity = data + parityBit;
        System.out.println("Data Sent with Parity: " + dataWithParity);

        // Send data to receiver
        outToServer.writeBytes(dataWithParity + "\n");

        // Close connections
        scanner.close();
        outToServer.close();
        socket.close();
    }
}
