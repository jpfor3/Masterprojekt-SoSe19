import AKAZE.AKAZEMatchDemo;
import Distanzmasse.FastEMD;
import cluster.DBScan;
import keypointdetector.KeypointDetector;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;


public class Main
{
	

   public static void main( String[] args )
   {
	 List<MatOfKeyPoint> _descriptorList = new ArrayList<MatOfKeyPoint>();
	 
	 List<List<double[]>> _centeredDescriptors = new ArrayList<List<double[]>>();
	 
     System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
     
     //Clean directory
     for(File file: new java.io.File("resources/output_images").listFiles()) 
 	    if (!file.isDirectory()) 
 	        file.delete();

     String inputImage = "resources/images/Profil_Kenny.jpg";
     
     File folder = new File("resources/images/");
     File[] listOfFiles = folder.listFiles();
     List<String> images = new ArrayList<String>();

     //Adding input image to image list
     images.add(inputImage);
     
     //Adding images for comparison
     for (File file : listOfFiles) {
         if (file.isFile()) {
             images.add("resources/images/" + file.getName());
         }
     }
     
     //Detecting Keypoints of images
      KeypointDetector KPDetector = new KeypointDetector(images);    
      _descriptorList = KPDetector.getDescriptorList();
      
      System.out.println("KP Detection Ended....");
      
      System.out.println("Creating clusters on Keypoints...");
      
      for(MatOfKeyPoint kp : _descriptorList)
      {
   	  List<double[]> clusterlist = DBScan.cluster(kp);
   	  _centeredDescriptors.add(clusterlist);   
      }
	
      System.out.println("Clustering Ended....");
      
      System.out.println("Calculating Distances....");
      List<Double> listOfDistances = FastEMD.calcDistances(_centeredDescriptors);
      
      //HashMap map = new HashMap();
      //map.put(images.get(0), listOfDistances.get(0));
      
      //Sorting Images and printing into new folder regarding calculated distances

      
   }
   
   
}