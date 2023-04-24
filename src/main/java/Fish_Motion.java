import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Fish_Motion

{

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        VideoCapture video = new VideoCapture("videos/fishtank.mp4");
        System.out.println("Video captured");
        Mat frame1 = new Mat();
        Mat frame2 = new Mat();
        Mat diff = new Mat();
        Mat img_p = new Mat();
        // This is an specifc type of list to save detected contours.
        // The value in BGR of contours
        Scalar c_color = new Scalar(255, 0, 0);
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
        Mat hierarchy = new Mat();
        Size b_size = new Size(5, 5);
        int minValue = 350;
        int c = 0;
        List<MatOfPoint> p_contours = new ArrayList<>();
        while (true)
        {

            // Two frames are needed to find the difference between them. With the differences you can find motion.
            // The contours list need to be inside the loop to avoid over drawing
            List<MatOfPoint> contours = new ArrayList<>();
            video.read(frame1);
            video.read(frame2);
            Size frame_size = new Size(612, 220);
            Imgproc.resize(frame1, frame1, frame_size);
            Imgproc.resize(frame2, frame2, frame_size);
            // finding absolut diff, essential image1 - image 2.
            Core.absdiff(frame1, frame2, diff);
            // Image processing
            Imgproc.GaussianBlur(diff, img_p, b_size, 0);
            Imgproc.threshold(img_p,img_p,20, 255, Imgproc.THRESH_BINARY);
            //Kernel 3,3 is the default and new Point just refers to 3 iterations.
            Imgproc.dilate(img_p, img_p, kernel, new Point(-1,-1), 3);
            Imgproc.threshold(img_p, img_p, 20, 255, Imgproc.THRESH_BINARY);
            Imgproc.cvtColor(img_p, img_p, Imgproc.COLOR_BGR2GRAY);

            Imgproc.findContours(img_p, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
            //System.out.println(contours.size() + "\n");
            for (int i = 0; i < contours.size(); i++)
            {
                Rect rect = Imgproc.boundingRect(contours.get(i));
                int x_c = rect.x;
                int y_c = rect.y;

                 if (Imgproc.contourArea(contours.get(i)) > minValue && Imgproc.contourArea(contours.get(i)) < 4500)
                {
                    p_contours.add(contours.get(i));
                    Mat box = new Mat(frame1, rect);
                    HighGui.imshow("box", box);
                    Imgcodecs.imwrite(("fish_box/" + c + ".jpg"), box);

                    try
                    {
                        FileWriter writer = new FileWriter("coordinates.txt", true);
                        BufferedWriter bufferedWriter = new BufferedWriter(writer);

                        bufferedWriter.write(x_c + "," + y_c);
                        bufferedWriter.newLine();

                        bufferedWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();// Cannot open/find file
                    }

                }

            }
            Imgproc.drawContours(frame1, p_contours, -1, c_color, 2);
            p_contours.clear();
            HighGui.imshow("Frame", frame1);
            c += 1;
            if (HighGui.waitKey(1) == 27)
            {
                break;
            }
        }
    }
}
