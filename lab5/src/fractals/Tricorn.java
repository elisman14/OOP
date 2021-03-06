package fractals;

import java.awt.geom.Rectangle2D;

public class Tricorn extends FractalGenerator {
    @Override
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -2;
        range.width = 4;
        range.height = 4;
    }

    @Override
    public int numIterations(double x, double y) {
        int iteration = 0;
        double zreal = 0;
        double zimaginary = 0;

        while (iteration < FractalGenerator.MAX_ITERATIONS &&
                zreal * zreal + zimaginary * zimaginary < 4)
        {
            double zrealUpdated = zreal * zreal - zimaginary * zimaginary + x;
            double zimaginaryUpdated = -2 * zreal * zimaginary + y;
            zreal = zrealUpdated;
            zimaginary = zimaginaryUpdated;
            iteration += 1;
        }

        return (iteration != FractalGenerator.MAX_ITERATIONS) ? iteration : -1;
    }

    @Override
    public String toString() {
        return "fractals.Tricorn";
    }
}
