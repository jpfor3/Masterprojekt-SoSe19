import AKAZE.AKAZEMatchDemo;
import Distanzmasse.GewichteteDistanz;
import cluster.DBScan;
import keypointdetector.KeypointDetector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.MatOfKeyPoint;


public class Main
{
	

   public static void main( String[] args )
   {
	 List<MatOfKeyPoint> descriptorList = new ArrayList<MatOfKeyPoint>();
	 List<List<double[]>> centeredDescriptors = new ArrayList<List<double[]>>();
	 
     System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
     
     //Zu vergleichende Bilder
     /**String image1 =  "resources/images/HansSarpei.jpg";
     String image2 =  "resources/images/SchalkeTrikot.jpg";
     String image3 =  "resources/images/Tennisball.jpg";
     */
     File folder = new File("resources/images/");
     File[] listOfFiles = folder.listFiles();
     List<String> images = new ArrayList<String>();

     for (File file : listOfFiles) {
         if (file.isFile()) {
             images.add("resources/images/" + file.getName());
         }
     }
     
      KeypointDetector.SurfDetector(images);    
      descriptorList = KeypointDetector._descriptorList;
      
      System.out.println("Creating clusters on Keypoints...");
      
      for(MatOfKeyPoint kp : descriptorList)
      {
   	  List<double[]> clusterlist = DBScan.cluster(kp);
   	  centeredDescriptors.add(clusterlist);   
      }
	
      System.out.println("Clustering Ended....");
      
      List<Double> listOfDistances = GewichteteDistanz.calcDistances(centeredDescriptors);
      
      //AKAZEMatch Akaze = new AKAZEMatch();
      //Akaze.run(image1, image2);
      
   }
   
   
}