/**
 * INFO: Falls Bilder aus dem Ordner images entfernt werden, müssen die Inhalte der Textdateien idx.txt und image_distances.txt
 * gelöscht werden
 */

import Distanzmasse.FastEMD;

import Distanzmasse.JaccardDistance;
import cluster.DBScan;
import keypointdetector.KeypointDetector;



import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections15.BidiMap;
import org.apache.commons.collections15.bidimap.DualHashBidiMap;
import org.opencv.core.Core;
import org.opencv.core.MatOfKeyPoint;


public class Controller
{
	static List<MatOfKeyPoint> _descriptorList = new ArrayList<MatOfKeyPoint>();
	 
	static List<List<double[]>> _centeredDescriptors = new ArrayList<List<double[]>>();
		
 	static int _threshold;
 	
	public Controller()
	{
	     System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
    
//	public static void main(String[] args)
//	{
//		
//		String inputImage = "resources/images/HansSarpei.jpg";
//		String compareImages = "resources/images";
//		try {
//			compareImages(inputImage, compareImages, 4, 0.1);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
	public static void calcJaccard(List<MatOfKeyPoint> descriptorList, int threshold)
    { 
	  List<Double> jacList = JaccardDistance.calculateJaccard(descriptorList, threshold);
      System.out.println("Jac Distance: " + jacList.get(0));

//      for(MatOfKeyPoint kp : _descriptorList) {
//    	  System.out.println(kp.toString());
//      }
      

    }

   	
	public static void compareImages(String inputImage, String compareImages, int minSamples, float eps, int emdpenalty, String distanceAlgorithm) throws IOException   	
	{     
		long startTime = System.currentTimeMillis()/1000;

		 File folder = new File(compareImages);
	     File[] listOfFiles = folder.listFiles();
	     List<String> images = new ArrayList<String>();
	
	     _centeredDescriptors.clear();
	
	     _descriptorList.clear();
	
	     /**
	      * Adding input image
	      */
	     images.add(inputImage);
	     
	
	     for(File file : listOfFiles)
	     {
	    	 images.add(file.getPath());
	     }
	
	    	 
	     File folder2 = new File("resources/sorted_output_images/");
	     File[] listOfFiles2 = folder2.listFiles();
	     
	     //Clean directories   
	     for(int i = 0; i < listOfFiles2.length; i++) 
	     {
	    	 listOfFiles2[i].delete();
	     }
	     
	     //Detecting Keypoints of images
	      KeypointDetector KPDetector = new KeypointDetector(images); 
	      _descriptorList = KPDetector.getDescriptorList();
	      
	      System.out.println("KP Detection Ended....");
	
	      System.out.println("Creating clusters on Keypoints...");
	
	      DBScan._massList.clear();
	      for(MatOfKeyPoint kp : _descriptorList)
	      {
	   	  List<double[]> clusterlist = DBScan.cluster(kp, minSamples, eps);
	   	  _centeredDescriptors.add(clusterlist);   
	      }
	      
	      System.out.println("Clustering Ended....");
	
	      System.out.println("Calculating Distances....");
	     
	      List<Double> listOfDistances = new ArrayList<Double>();
	      listOfDistances.clear();
	      
	      // check which distance algorithm was chosen in the UI
	      switch(distanceAlgorithm) {
	      case "Jaccard":
	    	  calcJaccard(_descriptorList, emdpenalty);
	      case "EMD":	
	          listOfDistances = FastEMD.calcDistances(_centeredDescriptors, images, emdpenalty, false);
	      case "Hamming":
	          listOfDistances = FastEMD.calcDistances(_centeredDescriptors, images, emdpenalty, true);
	      }
	
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
	        
	        
	        for(int i = 0; i < sortedImages.size(); i++)
	        {
	        KeypointDetector.drawKeypoints(sortedImages.get(i), i);
	        }

			long endTime = System.currentTimeMillis()/1000;
			long duration = endTime - startTime;
			System.out.println("Duration: " + duration);
			
   	}
	     
}
   
   
