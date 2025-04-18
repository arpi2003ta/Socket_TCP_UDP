import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ParitySenderC {
    public static void main(String[] args) throws IOException {
        String serverIP = "localhost"; 
        int serverPort = 6789;

        Socket socket = new Socket(serverIP, serverPort);
        System.out.println("Connected to server on port " + serverPort);

        DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter binary data: ");
        String data = scanner.nextLine();

        // Append '1' to force corruption
        String dataWithParity = data + "1";
        System.out.println("Data Sent with Parity: " + dataWithParity);

        // Send data to receiver
        outToServer.writeBytes(dataWithParity + "\n");

        // Close connections
        scanner.close();
        outToServer.close();
        socket.close();
    }
}
