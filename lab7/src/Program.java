public class Program {
    public static void main(String[] args) throws InterruptedException {

        int depth = getInput(args);

        System.out.println("Started crawler");
        Link link = new Link(args[0], 0);

        Crawler crawler = new Crawler(link, depth);
        crawler.runCrawler();

        System.out.println(crawler.getCheckedLinks());
    }

    private static int getInput(String[] args) {
        if (args.length != 2) {
            System.out.println("usage: java Crawler <URL> <depth>");
            System.exit(1);
        }
        else {
            try {
                return Integer.parseInt(args[1]);
            }
            catch (NumberFormatException e) {
                System.out.println("usage: java Crawler <URL> <depth>");
                System.exit(1);
            }
        }

        return 0;
    }

}
