import java.util.Scanner;
import java.lang.Math;

public class Lab2 {
    public static void main(String args[]) {
        Scanner scan = new Scanner(System.in);


        Point3d test = new Point3d(scan);



        System.out.println("Введите координаты первой точки:");
        double[] coordsFirst = new double[3];
        for (int i = 0; i < 3; ++i)
            coordsFirst[i] = scan.nextDouble();
        
        System.out.println("Введите координаты второй точки:");
        double[] coordsSecond = new double[3];
        for (int i = 0; i < 3; ++i)
            coordsSecond[i] = scan.nextDouble();
        
        System.out.println("Введите координаты третьей точки:");
        double[] coordsThird = new double[3];
        for (int i = 0; i < 3; ++i)
            coordsThird[i] = scan.nextDouble();
        scan.close();

        Point3d pointFirst = new Point3d(coordsFirst[0], coordsFirst[1], coordsFirst[2]);
        Point3d pointSecond = new Point3d(coordsSecond[0], coordsSecond[1], coordsSecond[2]);
        Point3d pointThird = new Point3d(coordsThird[0], coordsThird[1], coordsThird[2]);
        
        System.out.println(getSquare(pointFirst, pointSecond, pointThird));
    }
    public static double getSquare(Point3d pointFirst, Point3d pointSecond, Point3d pointThird) {
        double a = pointFirst.distanceTo(pointSecond);
        double b = pointSecond.distanceTo(pointThird);
        double c = pointThird.distanceTo(pointFirst);

        double p = (a + b + c) / 2;

        return Math.sqrt(p * (p - a) * (p - b) * (p - c));
    }

}
