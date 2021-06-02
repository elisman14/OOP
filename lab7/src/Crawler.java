import java.net.*;
import java.util.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Crawler {
    public static final String HREF_PATTERN = "^.*<a.*href=\"((?:http://|/)[a-zA-Z-_./]+)\".*>.*</a>.*$";
    private final int port;
    private final int timeout;
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
        Link fromLink;

        while (!uncheckedLinks.isEmpty()) {
            fromLink = uncheckedLinks.removeFirst();
            checkedLinks.merge(fromLink, 1, Integer::sum);
            makeNewConnection(fromLink);

        }

    }

    private void makeNewConnection(Link fromLink) {
        Socket socket;
        BufferedReader bufferedReader;

        try {
            socket = getConnectionSocket(fromLink)
                    .orElseThrow(IllegalArgumentException::new);
            bufferedReader = getBufferedReader(fromLink, socket)
                    .orElseThrow(IllegalArgumentException::new);

            readHtmlSourceOfPage(fromLink, bufferedReader);

            socket.close();
            bufferedReader.close();

        } catch (IOException e) {
            System.out.println("Error with makeNewConnection: " + e.getMessage());
        }
    }

    private void readHtmlSourceOfPage(Link fromLink, BufferedReader bufferedReader) {
        String htmlLine;

        try {
            while (true) {
                htmlLine = bufferedReader.readLine();

                if (htmlLine == null) { break; }

                getHrefUrlListFromPageSource(htmlLine).ifPresent(hrefUrlList ->
                    sortUrlList(hrefUrlList, fromLink)
                );
            }

            bufferedReader.close();

        } catch (IOException e) {
            System.out.println("readHtmlSourceOfPage: " + e.getMessage());

        } catch (IllegalArgumentException e) {
            System.out.println("readHtmlSourceOfPage:\nSomething wrong with bufferedReader: " + e.getMessage());
        }
    }

    private Optional<LinkedList<String>> getHrefUrlListFromPageSource(String htmlLine) {
        Matcher matcher;
        LinkedList<String> hrefUrlList = new LinkedList<>();

        matcher = Pattern.compile(HREF_PATTERN).matcher(htmlLine);

        while (matcher.find()) {
            hrefUrlList.add(matcher.group(1));
        }

        return Optional.of(hrefUrlList);
    }

    private void sortUrlList(LinkedList<String> hrefUrlList, Link fromLink) {
        hrefUrlList.stream()
                .filter(s -> s.startsWith("/"))
                .map(s -> s = "http://" + fromLink.getWebHost() + s)
                .forEach(s -> {
                    Link linkToLoad = new Link(s, fromLink.getDepth() + 1);

                    if (checkedLinks.containsKey(linkToLoad) || linkToLoad.getDepth() >= mainDepth) {
                        checkedLinks.merge(linkToLoad, 1, Integer::sum);
                    } else {
                        uncheckedLinks.add(linkToLoad);
                    }
                });

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

    private Optional<BufferedReader> getBufferedReader(Link link, Socket socket) {
        InputStream inputStream;
        OutputStream outputStream;

        try {
            outputStream = socket.getOutputStream();

            PrintWriter p = new PrintWriter(outputStream);

            p.print("GET " + link.getDocPath() + " HTTP/1.1\r\n");
            p.print("Host: " + link.getWebHost() + "\r\n\r\n");
            p.print("Accept: */*");
            p.print("User-Agent: Java");
            p.print("Connection: close\r\n");
            p.print("");
            p.flush();

//            System.out.println("GET " + webHost + docPath);

            inputStream = socket.getInputStream();

            return Optional.of(new BufferedReader(new InputStreamReader(inputStream)));
        }
        catch (IOException e) {
            System.err.println("IOException from getBufferedReader: " + e.getMessage());
            return Optional.empty();
        }
    }

    public void printLinks() {
        int maxDepth = Objects.requireNonNull(checkedLinks.keySet().stream()
                .max(Comparator.comparingInt(Link::getDepth))
                .orElse(null))
                .getDepth();

        checkedLinks.forEach(((link, count) -> System.out.println(
                                                        "url: " + link.getUrl() +
                                                        "\ndepth: " + link.getDepth() +
                                                        "\ncount: " + count + "\n"
                                                        )
        ));

        System.out.println("uncheckedLinks size: " + uncheckedLinks.size());
        System.out.println("checkedLinks size: " + checkedLinks.size());
        System.out.println("Max found depth: " + maxDepth);
    }
}
