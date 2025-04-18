import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

class BitStuffingUDPClient {
    public static void main(String[] args) throws Exception {
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress serverAddress = InetAddress.getByName("localhost");
        Scanner scanner = new Scanner(System.in);

        byte[] receiveBuffer = new byte[1024];
        byte[] sendBuffer;
        DatagramPacket sendPacket;
        DatagramPacket receivePacket;

        //receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length, serverAddress, 6789);
        //clientSocket.receive(receivePacket);

        

        
            

        // Get user input
        System.out.print("Enter binary data  ");
        String inputData = scanner.nextLine();
        sendBuffer = inputData.getBytes();
        sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, 6789);
        clientSocket.send(sendPacket);

            

        // Receive stuffed data from server
        receiveBuffer = new byte[1024]; // Clear buffer
        receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        clientSocket.receive(receivePacket);
        String stuffedData = new String(receivePacket.getData(), 0, receivePacket.getLength());
        System.out.println(stuffedData);

            
        

        clientSocket.close();
        scanner.close();
    }
}
