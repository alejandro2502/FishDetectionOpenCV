import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.highgui.HighGui;
import org.opencv.videoio.Videoio;

public class Test01 {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        // DSHOW just makes the camera launch faster
        VideoCapture camera = new VideoCapture(1, Videoio.CAP_DSHOW);
        Mat frame = new Mat();
        while (true) {
            camera.read(frame);
            HighGui.imshow("Camera", frame);
            if (HighGui.waitKey(1)  == 27){
                break;

            }
        }
        System.out.println("Program Terminated");
        camera.release();
        System.out.println("Camera Released");
        HighGui.destroyAllWindows();
        System.out.println("Windows Destroyed");
        HighGui.waitKey(1);
    }
}
