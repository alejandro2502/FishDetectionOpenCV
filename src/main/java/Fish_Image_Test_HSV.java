import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.highgui.HighGui;
import org.opencv.videoio.Videoio;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Fish_Image_Test_HSV {
    // function from chat gpt



    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat background = Imgcodecs.imread("images/background.png");
        Mat img = Imgcodecs.imread("images/1.png");
        Size size = new Size(480, 270);
        Imgproc.resize(img,img,size);
        Imgproc.resize(background, background,size);
        Mat diff = new Mat();
        Core.absdiff(background, img, diff);
        Mat comp = diff;
        Imgproc.cvtColor(comp, comp, Imgproc.COLOR_BGR2GRAY);
        HighGui.imshow("Comp", comp);
        HighGui.imshow("diff", diff);
        Mat gray = background;
        Mat canny = gray;
        Imgproc.cvtColor(background, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(gray, canny, new Size(11, 11), 3);
        HighGui.imshow("blur", canny);
        Imgproc.Canny(canny, canny, 9, 9);

        HighGui.imshow("Image1", img);
        HighGui.imshow("Image", background);
        HighGui.imshow("Image", gray);
        HighGui.imshow("Image", canny);




        HighGui.waitKey(0);

    }
}
