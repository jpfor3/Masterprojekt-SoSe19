package keypointdetector;

import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.Feature2D;
import org.opencv.features2d.ORB;
import org.opencv.highgui.HighGui;
import org.opencv.highgui.Highgui;
import org.opencv.core.Mat;



public class ORBDetector {
	
	public ORB computeKeypoints (String image1, String image2) {
		
		ORB orb = ORB.create();
		
		DescriptorExtractor orb_extractor = DescriptorExtractor.create(DescriptorExtractor.ORB);
		
		Mat descriptors = new Mat();
		
		return orb;
		
	}

}
