import java.net.*;
import java.util.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Crawler {

    public static final String HREF_PATTERN = "^.*<a.*href=\"((?:http://|/)[a-zA-Z-_./]+)\".*>.*</a>.*$";
    private final int port;
    private final int timeout;
    private String webHost;

    private final int mainDepth;

    private final Map<Link, Integer> checkedLinks;
    private final LinkedList<Link> uncheckedLinks;

    public Crawler(Link mainLink, int mainDepth, int port, int timeout) {
        this.mainDepth = mainDepth;
        this.port = port;
        this.timeout = timeout;
        checkedLinks = new HashMap<>();
        uncheckedLinks = new LinkedList<>();

        uncheckedLinks.add(mainLink);
    }

    public void runCrawler() throws IllegalArgumentException{
        Link link;

        while (!uncheckedLinks.isEmpty()){
            try {
                link = uncheckedLinks.getFirst();

                if (link.getDepth() == mainDepth || checkedLinks.containsKey(link)) {
                    System.out.println("Max depth or already searched: " + link);
                    checkedLinks.merge(link, 1, Integer::sum);
                    continue;
                }

                System.out.println("Searching in url: " + link);

                getAllLinksFromPage(link);

            } catch (Exception e) {
                System.out.println("Exception from runCrawler: " + e.getMessage() + "\nProblem from: " + uncheckedLinks.remove());
                uncheckedLinks.removeFirst();
            }

        }
    }

    private void getAllLinksFromPage(Link link) {
        Socket socket;
        BufferedReader bufferedReader;
        String htmlLine, currentLink;
        Matcher matcher;
        Link newLink;

        try {
            socket = getConnectionSocket(link)
                    .orElseThrow(IllegalArgumentException::new);

            bufferedReader = getBufferedReader(socket, link)
                    .orElseThrow(IllegalArgumentException::new);

            while (true) {
                htmlLine = bufferedReader.readLine();

                if (htmlLine == null) {
                    break;
                }

                matcher = Pattern.compile(HREF_PATTERN).matcher(htmlLine);

                while (matcher.find()) {
                    currentLink = matcher.group(1);

                    if (currentLink.startsWith("/")) {
                        currentLink = "http://" + webHost + currentLink;
                    }

                    newLink = new Link(currentLink, link.getDepth());


                    if (checkedLinks.containsKey(newLink) || uncheckedLinks.getFirst().equals(newLink)) {
                        checkedLinks.merge(newLink, 1, Integer::sum);
                        System.out.println("Copy found: " + newLink);
                        continue;
                    }

                    checkedLinks.merge(uncheckedLinks.removeFirst(), 1, Integer::sum);
                    uncheckedLinks.addLast(newLink);
                    System.out.println("New link added to uncheckedQueue: " + newLink);

                }
            }

            socket.close();
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("Error from runCrawler: " + e.getMessage());
        }
    }

    private Optional<Socket> getConnectionSocket(Link link) {
        Socket socket;

        try {
            socket = new Socket(link.getWebHost(), port);
        }
        catch (UnknownHostException e) {
            System.err.println("UnknownHostException: " + e.getMessage() + "\nurl: " + link.getUrl());
            return Optional.empty();
        }
        catch (IOException e) {
            System.err.println("IOException: " + e.getMessage() + link.getUrl());
            return Optional.empty();
        }

        try {
            socket.setSoTimeout(timeout);
        }
        catch (SocketException e) {
            System.err.println("SocketException in setSoTimeout: " + e.getMessage());
            return Optional.empty();
        }

        return Optional.of(socket);
    }

    private Optional<BufferedReader> getBufferedReader(Socket socket, Link link) {
        String docPath = link.getDocPath();
        webHost = link.getWebHost();

        InputStream inputStream;
        OutputStream outputStream;

        try {
            outputStream = socket.getOutputStream();

            PrintWriter p = new PrintWriter(outputStream);

            p.print("GET " + docPath + " HTTP/1.1\r\n");
            p.print("Host: " + webHost + "\r\n\r\n");
            p.print("Accept: */*");
            p.print("User-Agent: Java");
            p.print("Connection: close\r\n");
            p.print("");
            p.flush();

//            System.out.println("GET " + webHost + docPath);

            inputStream = socket.getInputStream();

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            return Optional.of(new BufferedReader(inputStreamReader));
        }
        catch (IOException e) {
            System.err.println("IOException from getBufferedReader: " + e.getMessage());
            return Optional.empty();
        }
    }

    public void printLinks() {
        checkedLinks.forEach(((link, count) -> System.out.println(
                                                        "url: " + link.getUrl() +
                                                        "\ndepth: " + link.getDepth() +
                                                        "\ncount: " + count + "\n"
                                                        )
        ));

        System.out.println("uncheckedLinks size: " + uncheckedLinks.size());
        System.out.println("checkedLinks size: " + checkedLinks.size());
    }
}
