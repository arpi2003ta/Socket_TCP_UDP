import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ParitySenderClientstop {
    public static void main(String[] args) throws IOException {
        String serverIP = "localhost"; 
        int serverPort = 6789;

        Socket socket = new Socket(serverIP, serverPort);
        System.out.println("Connected to server on port " + serverPort);

        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
        Scanner scanner = new Scanner(System.in);

        int seq = 0; // Sequence number alternates between 0 and 1

        //while (true) {
            System.out.print("Enter binary data: ");
            String data = scanner.nextLine();

            // Compute even parity bit
            int count = 0;
            for (char bit : data.toCharArray()) {
                if (bit == '1') count++;
            }
            String parityBit = (count % 2 == 0) ? "0" : "1";  // Append parity bit

            String dataWithParity = seq + data + parityBit; // Add sequence number and parity
            System.out.println("Sending Data with Parity: " + dataWithParity);

            boolean ackReceived = false;
            
            while (!ackReceived) {
                outToServer.writeBytes(dataWithParity + "\n"); // Send data

                try {
                    socket.setSoTimeout(2000);  // 2 seconds timeout for ACK
                    String response = inFromServer.readLine();
                    
                    if (response != null && response.equals(seq + "ACK")) {
                        System.out.println("ACK received for sequence: " + seq);
                        seq = 1 - seq;  // Toggle sequence number
                        ackReceived = true;
                    }
                } catch (SocketTimeoutException e) {
                    System.out.println("Timeout! Retransmitting...");
                }
            }
        //}
    }
}
