import javax.swing.JComponent;
import java.awt.*;
import java.awt.image.BufferedImage;

public class JImageDisplay extends JComponent{
    private final BufferedImage img;

    JImageDisplay(int width, int height) {
        this.img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.setPreferredSize(new Dimension(width, height));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.img, 0, 0, img.getWidth(), img.getHeight(), null);
    }

    public void clearImage() {
        for (int i = 0; i < img.getHeight(); ++i)
            for (int j = 0; j < img.getWidth(); ++j)
                this.img.setRGB(i, j, 0);

    }

    public void drawPixel(int x, int y, int clr) { this.img.setRGB(x, y, clr); }

    public BufferedImage getImage() {
        return img;
    }
}
