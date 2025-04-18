import java.io.*;
import java.net.*;

class ByteStuffingServerUDP {
    public static void main(String argv[]) throws Exception {
        while (true){
            DatagramSocket serverSocket = new DatagramSocket(6789);
            System.out.println("UDP Math Server is running on port 6789...");


            byte[] receiveBuffer = new byte[1024]; // Clear buffer
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            serverSocket.receive(receivePacket);
            String stuffedData = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Received Stuffed Data: " + stuffedData);

            // Perform byte unstuffing
            String unstuffedData = byteUnstuffing(stuffedData);
            System.out.println("Original Data After Unstuffing: " + unstuffedData);

            
            serverSocket.close();
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
