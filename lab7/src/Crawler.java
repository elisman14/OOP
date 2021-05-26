import java.net.*;
import java.util.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Crawler {

    public static final String HREF_PATTERN = "^\\s*<a href=\"(http:.*)\">.*</a>$";
    public static final int port = 8080;

    private final int mainDepth;

    private final Map<Link, Integer> checkedLinks;
    private final LinkedList<Link> uncheckedLinks;

    public Crawler(Link mainLink, int mainDepth) {
        this.mainDepth = mainDepth;
        checkedLinks = new HashMap<>();
        uncheckedLinks = new LinkedList<>();

        uncheckedLinks.add(mainLink);
    }

    public void runCrawler() {
        Socket socket;
        BufferedReader bufferedReader;
        int depth = 1;

        while (!uncheckedLinks.isEmpty() && depth <= mainDepth + 1){
            try {
                socket = getConnectionSocket(uncheckedLinks.getFirst())
                        .orElseThrow(IllegalArgumentException::new);

                bufferedReader = getBufferedReader(socket, uncheckedLinks.getFirst())
                        .orElseThrow(IllegalArgumentException::new);

                if (getAllLinksFromPage(bufferedReader, depth)) { ++depth; }
                checkedLinks.merge(uncheckedLinks.remove(0), 1, Integer::sum);

                socket.close();
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private boolean getAllLinksFromPage(BufferedReader bf, int depth) {
        String htmlLine;
        boolean found = false;

        while (true) {
            try {
                htmlLine = bf.readLine();

                if (htmlLine == null) { break; }

                Matcher m = Pattern.compile(HREF_PATTERN).matcher(htmlLine);

                if (m.find()) {
                    found = true;

                    do {
                        uncheckedLinks.add(new Link(m.group(1), depth));
                    } while (m.find());
                }
            }
            catch (IOException except) {
                System.err.println("IOException from bf readLine: " + except.getMessage());
            }
        }

        return found;
    }

    private Optional<Socket> getConnectionSocket(Link link) {
        Socket socket;

        try {
            socket = new Socket(link.getWebHost(), port);
        }
        catch (UnknownHostException e) {
            System.err.println("UnknownHostException: " + e.getMessage());
            return Optional.empty();
        }
        catch (IOException ex) {
            System.err.println("IOException: " + ex.getMessage());
            return Optional.empty();
        }

        try {
            socket.setSoTimeout(3000);
        }
        catch (SocketException exc) {
            System.err.println("SocketException in setSoTimeout: " + exc.getMessage());
            return Optional.empty();
        }

        return Optional.of(socket);
    }

    private Optional<BufferedReader> getBufferedReader(Socket socket, Link link) {
        String docPath = link.getDocPath();
        String webHost = link.getWebHost();
        InputStream inputStream;
        OutputStream outputStream;

        try {
            outputStream = socket.getOutputStream();

            PrintWriter p = new PrintWriter(outputStream);

            p.println("GET " + docPath + " HTTP/1.1\r\n");
            System.out.println("GET " + docPath + " HTTP/1.1\r\n");
            p.println("Host: " + webHost);
            p.println("Connection: close\r\n\r\n");
            p.flush();

            inputStream = socket.getInputStream();

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            return Optional.of(new BufferedReader(inputStreamReader));
        }
        catch (IOException e) {
            System.err.println("IOException from getBufferedReader: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Map<Link, Integer> getCheckedLinks() {
        return checkedLinks;
    }
}
