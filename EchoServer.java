import java.io.*; 
import java.net.*;

class EchoServer
{
    public static void main(String argv[]) throws Exception
    {
        String clientSentence; String capitalizedSentence;
        ServerSocket skt= new ServerSocket(6789);
        System.out.println("Server up and running, listening for connections ");
        while(true)
        {
            Socket connectionSocket = skt.accept();
            System.out.println("Client connected ");
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            System.out.println("Reading from socket ");
            clientSentence = inFromClient.readLine();
            System.out.println("Received and Sending: " + clientSentence);
            System.out.println("Writing to socket ");
            capitalizedSentence = clientSentence.toUpperCase() + '\n';
            outToClient.writeBytes(capitalizedSentence);
            System.out.println("Message sent ");
            connectionSocket.close();
        }
    }
}

