import Distanzmasse.FastEMD;
import cluster.DBScan;
import keypointdetector.KeypointDetector;
import org.apache.commons.collections15.BidiMap;
import org.apache.commons.collections15.bidimap.DualHashBidiMap;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.MatOfKeyPoint;


public class Main
{
	

   public static void main( String[] args )
   {
	 List<MatOfKeyPoint> _descriptorList = new ArrayList<MatOfKeyPoint>();
	 
	 List<List<double[]>> _centeredDescriptors = new ArrayList<List<double[]>>();
	 
     System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
     
     //Clean directories   
     for(File file: new File("resources/sorted_output_images").listFiles()) 
     {
    	 file.delete();
     }
     
     File folder = new File("resources/images/");
     File[] listOfFiles = folder.listFiles();
     List<String> images = new ArrayList<String>();

     //Adding input image
     images.add("resources/images/Burgstaller.jpg");
     
     //Adding images for comparison
     for (File file : listOfFiles) {
         if (file.isFile()) {
             images.add(file.getPath());
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
      
      
      //Map and sort distances of images
      BidiMap<String, Double> map = new DualHashBidiMap<>();

      for(int i = 1; i < images.size(); i++)
      {
          map.put(images.get(i), listOfDistances.get(i-1));
      }
      
      Collections.sort(listOfDistances);
      List<String> sortedImages = new ArrayList<String>();
      
      for(int i = 1; i < images.size(); i++)
      {
    	  sortedImages.add(map.getKey(listOfDistances.get(i-1)));  	  
      }
      
     for(int i = 1; i < images.size(); i++)
      {
      KeypointDetector.drawKeypoints(sortedImages.get(i-1), i);
      }
   }
   
   
}