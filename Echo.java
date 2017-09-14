import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Echo implements Runnable {

    public static Set<Socket> clients = new HashSet<>();
    public static Lock lock = new ReentrantLock();


    private Socket client;

    public Echo(Socket client) {
        this.client = client;
        Echo.clients.add(this.client);
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));
            String inputLine;
            lock.lock();
            for (Socket client1 : Echo.clients) {
                PrintWriter out =
                        new PrintWriter(client1.getOutputStream(), true);
                out.println(Integer.toString(client.getPort()) + " just joined the chatroom");
            }
            lock.unlock();

            while ((inputLine = in.readLine()) != null) {
                lock.lock();
                for (Socket client1 : Echo.clients) {
                    PrintWriter out =
                            new PrintWriter(client1.getOutputStream(), true);
                    out.println("<" + Integer.toString(client.getPort()) + "> " + inputLine);
                }
                lock.unlock();
            }

            client.close();
            clients.remove(client);

            lock.lock();
            for (Socket client1 : Echo.clients) {
                PrintWriter out =
                        new PrintWriter(client1.getOutputStream(), true);
                out.println(Integer.toString(client.getPort()) + " just left the chatroom");
            }
            lock.unlock();

        } catch (IOException e) {
            System.err.println("Exception caught when trying to serve client "
                    + Integer.toString(client.getPort()));
            System.err.println(e.getMessage());
        }

    }
}
