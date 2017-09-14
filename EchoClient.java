
import java.io.*;
import java.net.*;

public class EchoClient implements Runnable {

    private static Socket echoSocket;
    private static BufferedReader in;

    public void run() {
        String responseLine;
        try {
            while ((responseLine = in.readLine()) != null) {
                System.out.println(responseLine);
            }
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }



    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            System.err.println(
                    "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try {
            echoSocket = new Socket(hostName, portNumber);
            String userInput;
            PrintWriter out =
                    new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader stdIn =
                    new BufferedReader(
                            new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            new Thread(new EchoClient()).start();
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
            }

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }
}