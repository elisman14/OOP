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
    private final Set<Link> blackList;


    public Crawler(Link mainLink, int mainDepth, int port, int timeout) {
        this.mainDepth = mainDepth;
        this.port = port;
        this.timeout = timeout;
        checkedLinks = new HashMap<>();
        uncheckedLinks = new LinkedList<>();
        blackList = new HashSet<>();

        uncheckedLinks.add(mainLink);
    }

    public LinkedList<Link> getUncheckedLinks(Link link) throws IllegalArgumentException{
        makeNewConnection(link);
        return uncheckedLinks;
    }

    private void makeNewConnection(Link fromLink) {
        Socket socket;
        BufferedReader bufferedReader;

        if (blackList.contains(fromLink)) { return; }

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

        } catch (IllegalArgumentException e) {
            blackList.add(fromLink);
        }
    }

    private void readHtmlSourceOfPage(Link fromLink, BufferedReader bufferedReader) {
        String htmlLine;

        try {
            while (true) {
                htmlLine = bufferedReader.readLine();

                if (htmlLine == null) { break; }

                getHrefUrlListFromPageSource(htmlLine).ifPresent(hrefUrl ->
                    addLinkToUncheckedList(hrefUrl, fromLink)
                );
            }

            bufferedReader.close();

        } catch (IOException e) {
            System.out.println("readHtmlSourceOfPage: " + e.getMessage());

        } catch (IllegalArgumentException e) {
            System.out.println("readHtmlSourceOfPage:\nSomething wrong with bufferedReader: " + e.getMessage());
        }
    }

    private Optional<String> getHrefUrlListFromPageSource(String htmlLine) {
        Matcher matcher;
        String hrefUrlList;

        matcher = Pattern.compile(HREF_PATTERN).matcher(htmlLine);

        if (matcher.find()) {
            hrefUrlList = matcher.group(1);
            return Optional.of(hrefUrlList);
        }

        return Optional.empty();
    }

    private void addLinkToUncheckedList(String hrefUrl, Link fromLink) {
        System.out.println(hrefUrl);
//        hrefUrl.stream()
//                .map(s -> {
//                    s = s.startsWith("/") ? "http://" + fromLink.getWebHost() + s : s;
//                    return new Link(s, fromLink.getDepth() + 1);
//                })
//                .forEach(uncheckedLinks::add);

        hrefUrl = hrefUrl.startsWith("/") ? "http://" + fromLink.getWebHost() + hrefUrl : hrefUrl;
        Link link = new Link(hrefUrl, fromLink.getDepth() + 1);
        if (checkedLinks.containsKey(link) || link.getDepth() >= mainDepth) {
            checkedLinks.merge(link, 1, Integer::sum);
            return;
        }
        uncheckedLinks.add(link);
    }

    private void addToCheckedLinks(Link link) {
//        uncheckedLinks
//                .forEach(link -> {
//                    if (checkedLinks.containsKey(link) || link.getDepth() >= mainDepth) {
//                        checkedLinks.merge(link, 1, Integer::sum);
//                    } else {
//                        uncheckedLinks.add(link);
//                    }
//                });


    }

    private Optional<Socket> getConnectionSocket(Link link) {
        Socket socket;

        try {
            socket = new Socket(link.getWebHost(), port);

        }

        catch (UnknownHostException e) {
            System.err.println("UnknownHostException: " + link.getUrl());
            return Optional.empty();
        }

        catch (IOException e) {
            System.err.println("IOException: " + e.getMessage() + link.getUrl());
            return Optional.empty();
        }

        try {
            socket.setSoTimeout(timeout);
            return Optional.of(socket);

        } catch (SocketException e) {
            System.out.println("SocketException: " + e.getMessage());
        }

        return Optional.empty();
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

    public void run() {
       while (!uncheckedLinks.isEmpty()) {
           getUncheckedLinks(uncheckedLinks.getFirst());
           checkedLinks.put(uncheckedLinks.removeFirst(), 1);
       }
    }
}
