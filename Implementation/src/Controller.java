/**
 * INFO: Falls Bilder aus dem Ordner images entfernt werden, müssen die Inhalte der Textdateien idx.txt und image_distances.txt
 * gelöscht werden
 */

import Distanzmasse.FastEMD;

import Distanzmasse.JaccardDistance;
import cluster.DBScan;
import keypointdetector.KeypointDetector;



import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections15.BidiMap;
import org.apache.commons.collections15.bidimap.*;
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

	
	public static void calcJaccard(List<MatOfKeyPoint> descriptorList, int threshold)
    { 
      List<Double> jacList = JaccardDistance.calculateJaccard(descriptorList, threshold);
      System.out.println("Jac Distance: " + jacList.get(0));
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
	      
	      if(distanceAlgorithm.equals("Jaccard")) {
	    		System.out.println("Jaccard chosen");
	    	    List<Double> jacList = JaccardDistance.calculateJaccard(_descriptorList, emdpenalty);
	    	    System.out.println("Jac Distance: " + jacList.get(0));
	      }
	      else {
	
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
		      
		      // check which distance algorithm was chosen in the UI
		      if(distanceAlgorithm.equals("EMD (Euclid)")) {
		    		System.out.println("EMD (Euclid) chosen");
		    		listOfDistances = FastEMD.calcDistances(_centeredDescriptors, images, emdpenalty, false);
			    }
			    else if(distanceAlgorithm.equals("EMD (Hamming)")) {
			    	System.out.println("EMD (Hamming) chosen");
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
		      	  sortedImages.add(getNextElement(map, listOfDistances, i));  	  
		        }
		        
		        
		        for(int i = 0; i < sortedImages.size(); i++)
		        {
		        KeypointDetector.drawKeypoints(sortedImages.get(i), i);
		        }
	      }
	      
	       	long endTime = System.currentTimeMillis()/1000;
	    	long duration = endTime - startTime;
	    	System.out.println("Program Duration: " + duration);
	}


   	
	private static String getNextElement(BidiMap<String, Double> map, List<Double> listOfDistances, int i) {
		//Für den Fall, dass Bilder die gleichen Distanzen haben, wird eine marginale Summe aufaddiert um Konflikte zu verhindern
		if(i < listOfDistances.size())
		{
			if(listOfDistances.get(i-1) == listOfDistances.get(i))
			{
				listOfDistances.set(i, listOfDistances.get(i) + 0.00001);
			} else if(listOfDistances.get(i-1) == listOfDistances.get(i) - 0.00001) {
				listOfDistances.set(i, listOfDistances.get(i) + 0.00002);
			} else if(listOfDistances.get(i-1) == listOfDistances.get(i) - 0.00002) {
				listOfDistances.set(i, listOfDistances.get(i) + 0.00003);
			} else if(listOfDistances.get(i-1) == listOfDistances.get(i) - 0.00003) {
				listOfDistances.set(i, listOfDistances.get(i) + 0.00004);
			}
		}
		return map.getKey(listOfDistances.get(i-1));
		
	}
	   	

}
   
   
