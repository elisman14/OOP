import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server {
    private static PrintWriter res;

    public static final String GET_DOCUMENT = "^GET (.*) HTTP/1.1$";
    public static final int PORT = 8080;

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }

    private void run() {
        try {
            ServerSocket server = new ServerSocket(PORT);
            System.out.println("Webserver starting up on port " + PORT);

            runHttpHandler(server);

            System.out.println("Server is closed");
            server.close();

        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private void runHttpHandler(ServerSocket server) {
        try {
            while (true) {
                Socket clientSocket = server.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                res = new PrintWriter(clientSocket.getOutputStream());

                String word = in.readLine();
                System.out.println(word);

                Matcher m = Pattern.compile(GET_DOCUMENT).matcher(word);
                String req = m.find() ? m.group(1) : "";

                if (req.equals("/exit")) { break; }

                sendStatusOk();

                pageRender(req);

                clientSocket.close();
                in.close();
                res.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendStatusOk() {
        res.println("HTTP/1.1 200 OK");
        res.println("Content-Type: text/html");
        res.println("Server: Bot");
        res.println("");
    }

    private void pageRender(String req) {
        switch (req) {
            case "/":
                sendPage("/home");
                break;
            case "/page1":
                sendPage("/page1");
                break;
            case "/page2":
                sendPage("/page2");
                break;
            case "/page3":
                sendPage("/page3");
                break;
            case "page2/page2_1":
                sendPage("/page2_1");
                break;
            case "page2/page2_2":
                sendPage("/page2_2");
                break;
        }

        res.flush();
    }

    private void sendPage(String pageName) {
        try {
            File index = new File("web" + pageName + ".html");
            BufferedReader reader = new BufferedReader(new FileReader(index));
            String line = reader.readLine();

            while (line != null)
            {
                res.println(line);
                line = reader.readLine();
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
