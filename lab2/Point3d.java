/**
 * трёхменрый класс точки.
 */

import java.lang.Math;
import java.util.Scanner;

public class Point3d {
    /* координата Z */
    private double zCoord;
    private Point2d point2d;
    
    /* Конструктор инициализации */
    public Point3d (double x, double y, double z) {
        super(x, y);
        zCoord = z;
    }

    /* Конструктор по умолчанию. */
    public Point3d () {
        this(0, 0, 0);
    }

    public Point3d(Scanner s) {
        xCoord = s.nextDouble();
        yCoord = s.nextDouble();
        zCoord = s.nextDouble();
    }

    /* Возвращение координаты Z */
    public double getZ () {
        return zCoord;
    }

    /* Установка значения координаты Y. */
    public void setZ (double val) {
        zCoord = val;
    }

    /* Проверка на равенство двух точек. */
    public boolean equals (Point3d point) {
        return xCoord == point.getX() && yCoord == point.getY() && zCoord == point.getZ();
    }

    /* Расстояние между двумя точками */
    // с точностью 2 знака после точки
    public double distanceTo (Point3d point) {
        return Math.sqrt(Math.pow((xCoord - point.getX()), 2) + Math.pow((yCoord - point.getY()), 2) + Math.pow((zCoord - point.getZ()), 2));
    }
}
