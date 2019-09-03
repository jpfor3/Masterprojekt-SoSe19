package keypointdetector;

import org.opencv.core.Core;
import org.opencv.core.DMatch;
import org.opencv.features2d.BFMatcher;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.Feature2D;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.ORB;
import org.opencv.highgui.HighGui;
import org.opencv.highgui.Highgui;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;



public class ORBDetector {
	
	public void detect_keypoints (String image1, String image2) {
		
		// Vorabversion abgeschrieben vom Flann_Matcher-tutorial, adaptiert von SURF auf ORB
		
		   File lib = null;
	       String os = System.getProperty("os.name");
	       String bitness = System.getProperty("sun.arch.data.model");

	       if (os.toUpperCase().contains("WINDOWS")) {
	           if (bitness.endsWith("64")) {
	               lib = new File("libs//x64//" + System.mapLibraryName("opencv_java2411"));
	           } else {
	               lib = new File("libs//x86//" + System.mapLibraryName("opencv_java2411"));
	           }
	       }

	       System.out.println(lib.getAbsolutePath());
	       System.load(lib.getAbsolutePath());
		
		ORB orb = ORB.create();
		
		DescriptorExtractor orb_extractor = DescriptorExtractor.create(DescriptorExtractor.ORB);
		
		Mat img1 = Imgcodecs.imread(image1, Imgcodecs.IMREAD_GRAYSCALE);
		Mat img2 = Imgcodecs.imread(image2, Imgcodecs.IMREAD_GRAYSCALE);
		
		Mat mask = new Mat();
		
		Mat descriptors1 = new Mat(), descriptors2 = new Mat();
		
		MatOfKeyPoint keypoints1 = new MatOfKeyPoint(), keypoints2 = new MatOfKeyPoint();
		
		System.out.println("Start DetectAndCompute...");
		
		orb.detectAndCompute(img1, new Mat(), keypoints1, descriptors1);
		orb.detectAndCompute(img2, new Mat(), keypoints2, descriptors2);
		
		System.out.println("Creating Matcher...");
		DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
		List<MatOfDMatch> knnMatches = new ArrayList<>();
		matcher.knnMatch(descriptors1, descriptors2, knnMatches, 2);
		
		
		System.out.println("Finde beste Matches...");
		
		float ratioThresh = 0.7F;
		List<DMatch> listOfGoodMatches = new ArrayList<>();
		for(int i = 0; i < knnMatches.size(); i++) {
			if(knnMatches.get(i).rows() > 1) {
				DMatch[] matches = knnMatches.get(i).toArray();
				if(matches[0].distance < ratioThresh * matches[1].distance) {
					listOfGoodMatches.add(matches[0]);
				}
			}
		}
		
		MatOfDMatch goodMatches = new MatOfDMatch();
		goodMatches.fromList(listOfGoodMatches);
		
		System.out.println("Zeichne Matches...");
		
		Mat imgmatches = new Mat();
		Features2d.drawMatches(img1, keypoints1, img2, keypoints2, goodMatches, imgmatches, Scalar.all(-1), Scalar.all(-1), new MatOfByte(), Features2d.DrawMatchesFlags_NOT_DRAW_SINGLE_POINTS);
		
		System.out.println("Showing Matches...");
		//HighGui.imshow("Good Matches", imgmatches);
		//HighGui.waitKey(0);
		
		JFrame frame = new JFrame("Result");
		frame.setSize(1280, 960);
		frame.add(new JLabel(new ImageIcon(HighGui.toBufferedImage(imgmatches))));
	    frame.setVisible(true);
		
		
		System.out.println("Done.");
		//System.exit(0);
		
		
		
		//TODO Einstellungen variabel machen zur sp�teren Einbindung in das/die(genus unklar) GUI.
		
		
	}

}
