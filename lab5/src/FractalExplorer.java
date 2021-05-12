import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class FractalExplorer {
    private final int frameSize;
    private final JImageDisplay display;
    private FractalGenerator fractal;
    private final Rectangle2D.Double range;

    public static void main(String[] args) {
        FractalExplorer displayExplorer = new FractalExplorer(800);
        displayExplorer.createAndShowGUI();
        displayExplorer.drawFractal();
    }


    public FractalExplorer(int size) {
        this.frameSize = size;

        this.fractal = new Mandelbrot();
        this.range = new Rectangle2D.Double();
        this.fractal.getInitialRange(range);
        this.display = new JImageDisplay(frameSize, frameSize);

    }

    public void createAndShowGUI() {
        this.display.setLayout(new BorderLayout());
        JFrame frame = new JFrame("Mandelbrot Fractals");
        frame.add(this.display, BorderLayout.CENTER);

        JButton resetButton = new JButton("Reset");
        ButtonHandler resetHandler = new ButtonHandler();
        resetButton.addActionListener(resetHandler);
        frame.add(resetButton, BorderLayout.SOUTH);

        MouseHandler clickHandler = new MouseHandler();
        display.addMouseListener(clickHandler);

        JComboBox<FractalGenerator> comboBox = new JComboBox<>();
        FractalGenerator mandelbrotFractal = new Mandelbrot();
        comboBox.addItem(mandelbrotFractal);
        FractalGenerator tricornFractal = new Tricorn();
        comboBox.addItem(tricornFractal);
        FractalGenerator burningShipFractal = new BurningShip();
        comboBox.addItem(burningShipFractal);

        ButtonHandler fractalChooser = new ButtonHandler();
        comboBox.addActionListener(fractalChooser);

        JPanel myPanel = new JPanel();
        JLabel myLabel = new JLabel("Fractal:");
        myPanel.add(myLabel);
        myPanel.add(comboBox);
        frame.add(myPanel, BorderLayout.NORTH);

        JButton saveButton = new JButton("Save");
        JPanel myBottomPanel = new JPanel();
        myBottomPanel.add(saveButton);
        myBottomPanel.add(resetButton);
        frame.add(myBottomPanel, BorderLayout.SOUTH);

        ButtonHandler saveHandler = new ButtonHandler();
        saveButton.addActionListener(saveHandler);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void drawFractal()
    {
        for (int i = 0; i < frameSize; ++i) {
            for (int j = 0; j < frameSize; ++j) {
                double xCoord = FractalGenerator.getCoord(range.x, range.x + range.width, frameSize, i);
                double yCoord = FractalGenerator.getCoord(range.y, range.y + range.height, frameSize, j);

                int iteration = fractal.numIterations(xCoord, yCoord);

                if (iteration == -1) display.drawPixel(i, j, 0);
                else {
                    float hue = 0.7f + (float) iteration / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    display.drawPixel(i, j, rgbColor);
                }

            }
        }

        display.repaint();
    }

    private class ButtonHandler implements ActionListener {
        public void  actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            if (e.getSource() instanceof JComboBox) {
                JComboBox<FractalGenerator> mySource = (JComboBox<FractalGenerator>) e.getSource();
                fractal = (FractalGenerator) mySource.getSelectedItem();
                Objects.requireNonNull(fractal).getInitialRange(range);
                drawFractal();

            }
            else if (cmd.equals("Reset")) {
                fractal.getInitialRange(range);
                drawFractal();
            }
            else if (cmd.equals("Save")) {

                JFileChooser myFileChooser = new JFileChooser();

                FileFilter extensionFilter =
                        new FileNameExtensionFilter("PNG Images", "png");
                myFileChooser.setFileFilter(extensionFilter);

                myFileChooser.setAcceptAllFileFilterUsed(false);

                int userSelection = myFileChooser.showSaveDialog(display);

                if (userSelection == JFileChooser.APPROVE_OPTION) {

                    java.io.File file = myFileChooser.getSelectedFile();

                    try {
                        BufferedImage displayImage = display.getImage();
                        javax.imageio.ImageIO.write(displayImage, "png", file);
                    }
                    catch (Exception exception) {
                        JOptionPane.showMessageDialog(display,
                                exception.getMessage(), "Cannot Save Image",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    private class MouseHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            double xCoord = FractalGenerator.getCoord(range.x, range.x + range.width, frameSize, x);
            double yCoord = FractalGenerator.getCoord(range.y, range.y + range.height, frameSize, y);

            fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
            drawFractal();
        }
    }
}
