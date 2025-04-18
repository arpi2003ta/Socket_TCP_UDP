import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

class BitStuffingClient {
    public static void main(String[] args) throws Exception {
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress serverAddress = InetAddress.getByName("localhost");
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter binary data: ");
        String data = scanner.nextLine();
        scanner.close();

        // Send data to server
        byte[] sendBuffer = data.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, 6789);
        clientSocket.send(sendPacket);

        // Receive stuffed data from server
        byte[] receiveBuffer = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        clientSocket.receive(receivePacket);
        String stuffedData = new String(receivePacket.getData(), 0, receivePacket.getLength());

        System.out.println("Received Bit-Stuffed Data: " + stuffedData);

        clientSocket.close();
    }
}
