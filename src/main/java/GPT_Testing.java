import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class GPT_Testing {

    private static final String IMG_PATH_1 = "images/background.png";
    private static final String IMG_PATH_2 = "images/2.png";
    private static final int DEFAULT_THRESHOLD = 50;

    private static JLabel imgLabel;

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat img1 = Imgcodecs.imread(IMG_PATH_1);
        Mat img2 = Imgcodecs.imread(IMG_PATH_2);
        Mat diffImg = new Mat();
        Mat results = new Mat();


        // Calculate initial difference
        Core.absdiff(img1, img2, diffImg);
        Imgproc.cvtColor(diffImg, diffImg, Imgproc.COLOR_BGR2GRAY);

        // Create frame and components
        JFrame frame = new JFrame("Image Difference");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        imgLabel = new JLabel();
        imgLabel.setPreferredSize(new Dimension(img1.width(), img1.height()));
        JPanel sliderPanel = new JPanel();
        JSlider thresholdSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, DEFAULT_THRESHOLD);
        thresholdSlider.setMajorTickSpacing(50);
        thresholdSlider.setMinorTickSpacing(10);
        thresholdSlider.setPaintTicks(true);
        thresholdSlider.setPaintLabels(true);
        sliderPanel.add(new JLabel("Threshold: "));
        sliderPanel.add(thresholdSlider);

        // Add components to frame
        frame.getContentPane().add(imgLabel, BorderLayout.CENTER);
        frame.getContentPane().add(sliderPanel, BorderLayout.SOUTH);

        // Add listener to slider
        thresholdSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int threshold = thresholdSlider.getValue();
                Core.absdiff(img1, img2, diffImg);
                Imgproc.cvtColor(diffImg, diffImg, Imgproc.COLOR_BGR2GRAY);
                Core.compare(diffImg, new Mat(diffImg.size(), diffImg.type(), Scalar.all(threshold)), diffImg, Core.CMP_GT);
                HighGui.imshow("Image Difference", diffImg);
                //results.copyTo(img2, diffImg);
                //HighGui.imshow("Image 32", results);
                HighGui.waitKey(10);
            }
        });

        // Show initial difference image
        img2.copyTo(img2, diffImg);
        HighGui.imshow("Image 32", img2);
        HighGui.imshow("Image Difference1", diffImg);
        HighGui.waitKey(10);

        // Display frame
        frame.pack();
        frame.setVisible(true);
    }
}
