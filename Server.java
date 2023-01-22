import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {

        // Port the Server/ Client connection
        args = new String[]{"30121"};
        int portNumber = Integer.parseInt(args[0]);

        //port error
        if (args.length != 1) {
            System.err.println("Usage: java EchoServer <port number>");
            System.exit(1);
        }

        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
             Socket clientSocket = serverSocket.accept();
             PrintWriter responseWriter = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader requestReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {
            System.out.println("Server created connection.\n");
            String[] packets = extractMessages();
            initialPacketSend(responseWriter, packets);
            responseWriter.println("Last Packet. Packet #: " + packets.length);
            sendMissingPackets(responseWriter, requestReader, packets);
        } catch (IOException e) {
            System.out.println(
                    "Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }

    }

    private static void sendMissingPackets(PrintWriter responseWriter1, BufferedReader requestReader1, String[] packets) throws IOException {
        String clientRequestMissingPackets;
        while ((clientRequestMissingPackets = requestReader1.readLine()) != null) {
            String[] request = clientRequestMissingPackets.split(" ");
            for (String s : request) {
                if (Math.random() < .80) {
                    System.out.println("Sending missing packet #: \" " + s + "\"");
                    responseWriter1.println(packets[Integer.parseInt(s)] + " Packet #: " + s);
                }
            }
            responseWriter1.println("Last Packet. Packet #: " + packets.length);
        }
    }

    private static void initialPacketSend(PrintWriter responseWriter1, String[] packets) {
        for (int i = 0; i < packets.length; i++) {
            if (Math.random() < .80) {
                System.out.println("Sending: \" " + packets[i] + "\"");
                responseWriter1.println(packets[i] + " Packet #: " + i);
            }
        }
    }

    private static String[] extractMessages() {
        String message = "Mary had a little lamb whose fleece was white as snow and everywhere that mary went the lamb was sure to go.";
        String[] packets = message.split(" ");
        return packets;
    }
}
