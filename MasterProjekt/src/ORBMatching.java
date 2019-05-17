import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.ORB;
import org.opencv.highgui.Highgui;
import org.opencv.imgcodecs.Imgcodecs;

public class ORBMatching {
    public void run() {
        String filename1 = "resources/images/Angelina_Jolie_009.jpg";
        String filename2 = "resources/images/Brad_Pitt_009.jpg";
        Mat img1 = Imgcodecs.imread(filename1, Imgcodecs.IMREAD_GRAYSCALE);
        Mat img2 = Imgcodecs.imread(filename2, Imgcodecs.IMREAD_GRAYSCALE);
        if (img1.empty() || img2.empty()) {
            System.err.println("Cannot read images!");
            System.exit(0);
        }
        //-- Step 1: Detect the keypoints using ORB Detector, compute the descriptors
        FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.SURF);
        //-- Step 2: Matching descriptor vectors with a brute force matcher
        // Since SURF is a floating-point descriptor NORM_L2 is used
        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
        MatOfDMatch matches = new MatOfDMatch();
        matcher.match(descriptors1, descriptors2, matches);
        //-- Draw matches
        Mat imgMatches = new Mat();
        Features2d.drawMatches(img1, keypoints1, img2, keypoints2, matches, imgMatches);
        HighGui.imshow("Matches", imgMatches);
        HighGui.waitKey(0);
        System.exit(0);
    }
}