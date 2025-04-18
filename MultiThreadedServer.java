import java.io.*; 
import java.net.*; 
import java.text.SimpleDateFormat;
import java.util.Date;

class MultiThreadedServer {
    public static void main(String[] args) throws IOException {
        int port = 6789;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server running on port " + port);

        while (true) {
            // Accept new client connection
            Socket clientSocket = serverSocket.accept();
            System.out.println("New client connected from port: " + clientSocket.getPort());

            // Create separate threads for reading and writing
            Thread readThread = new Thread(new ReadHandler(clientSocket));
            Thread writeThread = new Thread(new WriteHandler(clientSocket));

            // Start both threads
            readThread.start();
            writeThread.start();
        }
    }
}

// Thread for reading data from the client
class ReadHandler implements Runnable {
    private Socket clientSocket;

    public ReadHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String clientMessage;

            while ((clientMessage = inFromClient.readLine()) != null) {
                System.out.println("Client says: " + clientMessage);
            }

            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Client disconnected.");
        }
    }
}

// Thread for writing data to the client (sends system time once)
class WriteHandler implements Runnable {
    private Socket clientSocket;

    public WriteHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());

            // Send current system time once
            String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
            outToClient.writeBytes("Server Time: " + timeStamp + "\n");

            // Close output stream after sending time once
            outToClient.flush();
        } catch (IOException e) {
            System.out.println("Client disconnected.");
        }
    }
}
