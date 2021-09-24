import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Program {
    private static String mainLink;
    private static int mainDepth;
    private static int mainPort;
    private static int mainTimeout;
    private static int startLinksSize;
    public static final int MAX_WORKERS = 8;

    public static void main(String[] args) throws InterruptedException {
        try {
            processArgs(args);
            System.out.println("Started crawler");

            ExecutorService threadPool = Executors.newFixedThreadPool(8);

            Link link = new Link(mainLink, 0);
            LinkedList<Link> linksToStart;

            Crawler mainCrawler = new Crawler(link, mainDepth, mainPort, mainTimeout);
            linksToStart = mainCrawler.getUncheckedLinks(link);
            startLinksSize = linksToStart.size();

            Map<Future<Link>, Future<Integer>> futureMap = new HashMap<>();
            ExecutorService executor = Executors.newFixedThreadPool(50);
            for (Link uncheckedLink : linksToStart) {
                Runnable worker = new Crawler(uncheckedLink, mainDepth, mainPort, mainTimeout);
                executor.execute(worker);
            }
                executor.shutdown();

                while (!executor.isTerminated()) {
                }

                System.out.println("Finished all threads");

            } catch(IllegalArgumentException e){
                System.out.println("Bad Socket: " + e.getMessage());
            }

        }
    }

    private static void processArgs (String[]args){
        if (args.length != 4) {
            usage();
            System.exit(1);
        } else {
            try {
                mainLink = args[0];
                mainDepth = Integer.parseInt(args[1]);
                mainPort = Integer.parseInt(args[2]);
                mainTimeout = Integer.parseInt(args[3]);

            } catch (NumberFormatException e) {
                usage();
                System.exit(1);
            }
        }
    }

    private static void usage() {
        System.out.println("usage: java Crawler <URL> <depth> <port> <socketTimeOut>");
    }

}
