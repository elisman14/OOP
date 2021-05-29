public class Program {
    public static void main(String[] args) throws InterruptedException {

        int[] intsFromInput = getInput(args);

        System.out.println("Started crawler");
        Link link = new Link(args[0], 0);

        Crawler crawler = new Crawler(link, intsFromInput[0], intsFromInput[1], intsFromInput[2]);
        crawler.runCrawler();

        System.out.println(crawler.getCheckedLinks());


    }

    private static int[] getInput(String[] args) {
        if (args.length != 4) {
            System.out.println("usage: java Crawler <URL> <depth>");
            System.exit(1);
        }
        else {
            try {
                return new int[] {Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3])};
            }
            catch (NumberFormatException e) {
                System.out.println("usage: java Crawler <URL> <depth>");
                System.exit(1);
            }
        }

        return new int[] {0, 8080, 3000};
    }

}
