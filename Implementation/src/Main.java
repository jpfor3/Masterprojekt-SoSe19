import AKAZE.AKAZEMatchDemo;
import Distanzmasse.GewichteteDistanz;
import cluster.DBScan;
import keypointdetector.KeypointDetector;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.MatOfKeyPoint;


public class Main
{
	

   public static void main( String[] args )
   {
	 List<List<double[]>> imageList = new ArrayList<List<double[]>>();

	 
     System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	 //System.loadLibrary("C:/opencv/opencv_2.4.11/build/java/x64/opencv_java2411.dll");
     //Zu vergleichende Bilder
     String image1 =  "resources/images/HansSarpei.jpg";
     String image2 =  "resources/images/SchalkeTrikot.jpg";
     String image3 =  "resources/images/Tennisball.jpg";
     
      KeypointDetector.SurfDetector(image1, image2);    
      
      System.out.println("Creating clusters on Keypoints...");
      for(MatOfKeyPoint kp : KeypointDetector._descriptorList)
      {
   	  List<double[]> clusterlist = DBScan.cluster(kp);
   	  imageList.add(clusterlist);
   	   
      }
	
      System.out.println("Clustering Ended....");
      
      double normDist = GewichteteDistanz.calcDistances(imageList);
      
	  System.out.println("\n\n Distance between Image 0 and 1: " + normDist);

      //AKAZEMatch Akaze = new AKAZEMatch();
      //Akaze.run(image1, image2);
      
   }
   
   
}