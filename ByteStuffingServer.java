import java.io.*;
import java.net.*;

class ByteStuffingServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(6789);
            System.out.println("Server is running... Waiting for client...");

            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected!");

            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String stuffedData = inFromClient.readLine();
            System.out.println("Received Stuffed Data: " + stuffedData);

            // Perform byte unstuffing
            String unstuffedData = byteUnstuffing(stuffedData);
            System.out.println("Original Data After Unstuffing: " + unstuffedData);

            inFromClient.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String byteUnstuffing(String stuffedData) {
        StringBuilder result = new StringBuilder();
        boolean escapeNext = false;

        for (char c : stuffedData.toCharArray()) {
            if (escapeNext) {
                result.append(c); // Add the escaped character as it is
                escapeNext = false;
            } else if (c == 'E') {
                escapeNext = true; // Escape detected, next character is stuffed
            } else {
                result.append(c); // Normal character
            }
        }
        return result.toString();
    }
}
