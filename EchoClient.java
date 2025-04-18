import java.io.*;
import java.net.*;

class EchoClient
{
    public static void main(String argv[]) throws Exception
    {
        String sentence;
        String modifiedSentence;
        Socket clientSocket = new Socket("localhost", 6789);
        System.out.println("Client running, connected to server ");
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        sentence = "Hi from Client";
        System.out.println("Writing to socket ");
        outToServer.writeBytes(sentence + '\n');
        System.out.println("Reading from socket ");
        modifiedSentence = inFromServer.readLine();
        System.out.println("FROM SERVER: " + modifiedSentence);
        clientSocket.close();
    }
}