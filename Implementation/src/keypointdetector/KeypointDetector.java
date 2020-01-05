package keypointdetector;
import org.opencv.calib3d.Calib3d;







import org.opencv.core.*;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.HighGui;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.xfeatures2d.PCTSignatures;

import cluster.DBScan;
import cluster.KMeans;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



public class KeypointDetector {

    public static List<MatOfKeyPoint> _descriptorList = new ArrayList<MatOfKeyPoint>();


	public KeypointDetector(List<String> images){
	 
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


       System.out.println("Started....");
       System.out.println("Loading images...");
       
       int i = 0;
       
       for(String image : images)
       {
    	   MatOfKeyPoint descriptors = detectSURFKeypoints(image, i);
    	   _descriptorList.add(descriptors);
	       i +=1;
       } 
       
   }
	
	
	
	 private MatOfKeyPoint detectSURFKeypoints(String image, int i)
     {
  	   Mat img = Highgui.imread(image, Highgui.CV_LOAD_IMAGE_COLOR);    
       MatOfKeyPoint kp = new MatOfKeyPoint();
         
       //TODO: Try other Algorithms
       FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.SURF);
       featureDetector.detect(img, kp);
       
       MatOfKeyPoint descriptors = new MatOfKeyPoint();
       DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SURF);
       descriptorExtractor.compute(img, kp, descriptors);
         
       return descriptors;
         
     }
	 
	 public static void drawKeypoints(String image, int i)
	 {
		 Mat img = Highgui.imread(image, Highgui.CV_LOAD_IMAGE_COLOR);    
         MatOfKeyPoint kp = new MatOfKeyPoint();
         
         FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.SURF);
         featureDetector.detect(img, kp);
         
		 // Create the matrix for output image.
         Mat outputImage = new Mat(img.rows(), img.cols(), Highgui.CV_LOAD_IMAGE_COLOR);
         Scalar KeypointColor = new Scalar(255, 0, 0);
         
         Features2d.drawKeypoints(img, kp, outputImage, KeypointColor, 0);
         
         Highgui.imwrite("resources/sorted_output_images/" + i + ".jpg", outputImage);
	 }
	 
	 public List<MatOfKeyPoint> getDescriptorList()
	 {
		 return  _descriptorList;
	 }	

}
