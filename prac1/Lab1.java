import java.util.Scanner;

class Lab1 {
    public static void main(String[] args) {


        System.out.println("task 1: " + minToSeconds(2));
        System.out.println("task 2: " + getBasketballPoint(38, 8));
        System.out.println("task 3: " + pointsCounter(0, 0, 1));
        System.out.println("task 4: " + divFiveChecker(37));
        System.out.println("task 5: " + conjunction(false, false));
        System.out.println("task 6: " + drawWalls(41, 3, 6));
        System.out.println("task 7: " + Challenge.squaed(100));
        System.out.println("task 8: " + strangeComprassion(0.9, 3, 2));
        System.out.println("task 9: " + fpsCounter(10, 25));
        System.out.println("task 10: " + mod(6, 3));
    }

    private static void printAnswer(int taskNumber, int[] args, String result) {
        System.out.print("Task # " + taskNumber + "\nargs -> " + args + "\nresult -> " + result);
    }

    private static int minToSeconds(int minutes) { return minutes * 60; }

    private static int getBasketballPoint(int secondPoints, int thirdPoints) { return secondPoints * 2 + thirdPoints * 3; }

    private static int pointsCounter(int wins, int draws, int losses) { return wins * 3 + draws; }

    private static boolean divFiveChecker(int number) { return number % 5 == 0; }

    private static boolean conjunction(boolean a, boolean b) { return (a == true && b == true) ? true : false; }

    private static int drawWalls(int square, int width, int height) { return square / (width * height); }

    private static boolean strangeComprassion(double a, double b, double c) { return a * b > c; }

    private static int fpsCounter(int fps, int minutes) { return fps * 60 * minutes; }

    private static int mod(int a, int b) { return (a < b) ? 0 : a - ((a / b) * b); }
}
