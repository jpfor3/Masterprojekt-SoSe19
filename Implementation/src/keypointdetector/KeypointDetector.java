  package keypointdetector;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.highgui.Highgui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;



public class KeypointDetector {

    public static List<MatOfKeyPoint> _descriptorList = new ArrayList<MatOfKeyPoint>();
    public static String _detector;
    public static int _kpDetector;
    public static int _descriptorExtractor;
    
    public FileOutputStream fos;
    private String dateiname_clustering;
    private String dateiname_distanzen;
    private String dateiname_keypoints;
    

	public KeypointDetector(List<String> images, FileOutputStream fos) throws IOException{
	 
	   _descriptorList.clear();
	   
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
       
       
	   _detector = "SURF";
	     
	   switch (_detector) {
	   case "SIFT": _kpDetector = 3;
			   		_descriptorExtractor = 1;
			    	break;
	   case "SURF": _kpDetector = 4;
	   				_descriptorExtractor = 2;
	     		    break;
	   case "ORB": _kpDetector = 5;
	   				_descriptorExtractor = 3;
	     			break;
	   case "BRISK": _kpDetector = 11;
	   				_descriptorExtractor = 5;
	     			break;
	     }
       
	   
   	  //Initializing log file
	  BufferedWriter writer = new BufferedWriter(new FileWriter("./resources/logs/duration_keypoints.txt", true));
	  writer.write("\nDatum: " + java.time.LocalDateTime.now() + "\n");
	  long overallDuration = 0;
	  
	  // Erstelle die Textdatei
	  // dateiname = durchlauf mit parametern
		 

	  
	  int iterator = 0;
		  
       for(String image : images)
       {
    	   long startingTime = System.currentTimeMillis();

    	   MatOfKeyPoint descriptors = detectKeypoints(image);
    	    	  
    	   long endTime = System.currentTimeMillis();
    	   long duration = endTime - startingTime;
    	   
    	   try {
    		   OutputStreamWriter osw = new OutputStreamWriter(fos); 
    		   osw.write(image.substring(image.lastIndexOf("\\")+1) + " KP: " + duration + "\n");
    	   } finally {
    		   if (fos != null) {
    			   try {
    				   fos.close();
    			   } finally {}
    		   }
    	   }
    	   overallDuration += duration;
    	   
		   iterator++;
	    	  
    	   _descriptorList.add(descriptors);
	       i +=1;
       } 

       long meanDuration = overallDuration/images.size();
	   writer.write("Für " + images.get(0).substring(images.get(0).lastIndexOf("\\")+1) + "\n");
	   writer.write("Mittelere Berechnungsdauer je KP: " + meanDuration + "\n");
       writer.close();
       
   }
	
	
	
	 private MatOfKeyPoint detectKeypoints(String image)
     {
  	   Mat img = Highgui.imread(image, Highgui.CV_LOAD_IMAGE_COLOR);    
       MatOfKeyPoint kp = new MatOfKeyPoint();
       
       FeatureDetector featureDetector = FeatureDetector.create(_kpDetector);
       featureDetector.detect(img, kp);
       
       MatOfKeyPoint descriptors = new MatOfKeyPoint();
       DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(_descriptorExtractor);
       descriptorExtractor.compute(img, kp, descriptors);

       return descriptors;
       
     }
	 
	 public static void drawKeypoints(String image, int i)
	 {
		 Mat img = Highgui.imread(image, Highgui.CV_LOAD_IMAGE_COLOR);    
         MatOfKeyPoint kp = new MatOfKeyPoint();
         
         FeatureDetector featureDetector = FeatureDetector.create(_kpDetector);
         featureDetector.detect(img, kp);
         
         Mat empty = new Mat();  
		 // Create the matrix for output image.
         if(!empty.dump().equals(img.dump()))
         {
	         Mat outputImage = new Mat(img.rows(), img.cols(), Highgui.CV_LOAD_IMAGE_COLOR);
	         Scalar KeypointColor = new Scalar(255, 0, 0);
	         
	         Features2d.drawKeypoints(img, kp, outputImage, KeypointColor, 0);
	         
	         Highgui.imwrite("resources/sorted_output_images/(" + i + ") " + image.substring(image.lastIndexOf("\\")+1) +  ".jpg", outputImage);
         }
     }
	 
	 public List<MatOfKeyPoint> getDescriptorList()
	 {
		 return  _descriptorList;
	 }	
	 


}
