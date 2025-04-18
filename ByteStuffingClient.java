import java.io.*;
import java.net.*;
import java.util.Scanner;

class ByteStuffingClient {
    public static void main(String[] args) {
        try {
            Socket clientSocket = new Socket("localhost", 6789);
            System.out.println("Connected to Server!");

            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter a sequence of D (Data), E (Escape), F (Flag): ");
            String inputData = scanner.nextLine();

            // Perform byte stuffing
            String stuffedData = byteStuffing(inputData);
            System.out.println("Stuffed Data Sent: " + stuffedData);

            // Send stuffed data
            outToServer.writeBytes(stuffedData + "\n");

            scanner.close();
            outToServer.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String byteStuffing(String data) {
        StringBuilder stuffed = new StringBuilder();

        for (char c : data.toCharArray()) {
            if (c == 'F' || c == 'E') {
                stuffed.append("E"); // Add escape before special characters
            }
            stuffed.append(c); // Add actual character
        }

        return stuffed.toString();
    }
}
