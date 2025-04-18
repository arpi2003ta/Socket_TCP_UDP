import java.io.*; 
import java.net.*; 
import java.text.SimpleDateFormat;
import java.util.Date;

class ConcurrentServer {
    public static void main(String[] args) throws IOException {
        int port = 6789;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server running on port " + port);

        while (true) {
            // Accept new client connection
            Socket clientSocket = serverSocket.accept();
            System.out.println("New client connected from port: " + clientSocket.getPort());

            // Handle client in a new thread
            ClientHandler clientHandler = new ClientHandler(clientSocket);
            new Thread(clientHandler).start();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            // Get output stream to send data to client
            DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());

            // Get current system time
            String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());

            // Send system time to client
            outToClient.writeBytes("Current Server Time: " + timeStamp + "\n");

            // Close client connection
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}