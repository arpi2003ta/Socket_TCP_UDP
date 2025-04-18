import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

class BitStuffingServer {
    public static void main(String argv[]) throws Exception {
        DatagramSocket serverSocket = new DatagramSocket(6789);
        System.out.println("UDP Bit Stuffing Server is running on port 6789...\n");

        byte[] receiveBuffer = new byte[1024];
        byte[] sendBuffer;
        DatagramPacket sendPacket;

        while (true) {
            // Receive message from client
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            serverSocket.receive(receivePacket);
            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();
            
            String data = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Received Data: " + data);

            // Perform bit stuffing
            String stuffedData = bitStuff(data);
            System.out.println("Bit-Stuffed Data: " + stuffedData);

            // Perform bit unstuffing
            String unstuffedData = bitUnstuff(stuffedData);
            System.out.println("Bit-Unstuffed Data: " + unstuffedData);

            // Send stuffed data back to client
            sendBuffer = stuffedData.getBytes();
            sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
            serverSocket.send(sendPacket);
        }
    }

    // Method to perform bit stuffing
    public static String bitStuff(String data) {
        StringBuilder stuffedData = new StringBuilder("01111110"); // Start flag byte
        int count = 0;

        for (int i = 0; i < data.length(); i++) {
            if (data.charAt(i) == '1') {
                count++;
                stuffedData.append("1");
                if (count == 5) {
                    stuffedData.append("0"); // Stuff a '0' after five consecutive '1's
                    count = 0;
                }
            } else {
                stuffedData.append("0");
                count = 0;
            }
        }
        stuffedData.append("01111110"); // End flag byte
        return stuffedData.toString();
    }

    // Method to perform bit unstuffing
    public static String bitUnstuff(String data) {
        StringBuilder unstuffedData = new StringBuilder();
        int count = 0;

        // Remove flag bytes first
        String actualData = data.substring(8, data.length() - 8);

        for (int i = 0; i < actualData.length(); i++) {
            if (actualData.charAt(i) == '1') {
                count++;
                unstuffedData.append("1");
                if (count == 5 && i + 1 < actualData.length() && actualData.charAt(i + 1) == '0') {
                    i++; // Skip the stuffed '0'
                    count = 0;
                }
            } else {
                unstuffedData.append("0");
                count = 0;
            }
        }
        return unstuffedData.toString();
    }
}
