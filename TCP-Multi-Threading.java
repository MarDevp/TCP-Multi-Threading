import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCP-Multi-Threading {
    public static void main(String[] args) {
        final int MaxClients = 10;
        final int port = 12345;

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Serveur démarré. En attente de connexions");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nouvelle connexion acceptée.");

                // Vérification du nombre maximal de clients
                if (Thread.activeCount() - 1 > MaxClients) { 
                    System.out.println("Nombre maximal de clients atteint. Nouvelle connexion refusée.");
                    clientSocket.close();
                    continue;
                }

                Thread clientThread = new ClientHandler(clientSocket);
                clientThread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private final Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                   
                    StringBuilder reversedString = new StringBuilder(inputLine).reverse();

                    Thread.sleep(2000); // 2 secondes
   
                    out.println(reversedString.toString());
                }

               
                in.close();
                out.close();
                clientSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
