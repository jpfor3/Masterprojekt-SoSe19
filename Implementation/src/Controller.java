/**
 * INFO: Falls Bilder aus dem Ordner images entfernt werden, müssen die Inhalte der Textdateien idx.txt und image_distances.txt
 * gelöscht werden
 */

import Distanzmasse.FastEMD;

import Distanzmasse.JaccardDistance;
import cluster.DBScan;
import keypointdetector.KeypointDetector;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

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

   	
   	public static void compareImages(String inputImage, String compareImages, int minSamples, double eps, int emdpenalty, String distanceAlgorithm) throws IOException
   	{     		
	     File folder = new File(compareImages);
	     File[] listOfFiles = folder.listFiles();
	     List<String> images = new ArrayList<String>();
	
	     _centeredDescriptors.clear();
	 	
	     _descriptorList.clear();
	     
	     //Überprüfe zu welcher Kategorie das Input Image gehört
	     String refImage = null;
	     switch(trimPath(inputImage)){
	        case "Hund":
	        	refImage = "Hund";
	        break;
	        case "Rind":
	        	refImage = "Rind";
	        break;
	        case "Landschaft":
	        	refImage = "Landschaft";
	        break;
	        case "Katze":
	        	refImage = "Katze";
	        break;
	        case "Schaf":
	        	refImage = "Schaf";
	        break;
	        case "Rad":
	        	refImage = "Rad";
	        break;
	        case "Bike":
	        	refImage = "Bike";
	        break;
	        case "Auto":
	        	refImage = "Auto";
	        break;
	        case "Pferd":
	        	refImage = "Pferd";
	        break;
	        case "Mensch":
	        	refImage = "Mensch";
	        break;
	     }
	     
	
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
		      
		   	  //Initializing log file
			  BufferedWriter writer = new BufferedWriter(new FileWriter("./resources/logs/duration_clustering.txt", true));
			  writer.write("\nDatum: " + java.time.LocalDateTime.now() + "\n");
			  writer.write("Für " + inputImage.substring(inputImage.lastIndexOf("\\")+1) + "\n");
			  
			  long overallDuration = 0;
			  
			  int iterator = 0;
		      for(MatOfKeyPoint kp : _descriptorList)
		      {
		    	  long startingTime = System.currentTimeMillis();

			   	  List<double[]> clusterlist = DBScan.cluster(kp, minSamples, eps);
			   	  
		    	  long endTime = System.currentTimeMillis();
		    	  long duration = endTime - startingTime;

			   	 overallDuration += duration;
			   	  iterator++;
			   	  
			   	  _centeredDescriptors.add(clusterlist);   
		      }
		      
		      long meanDuration = overallDuration/_descriptorList.size();
		      writer.write("Mittelere Clustering Dauer je KP: " + meanDuration + "\n");
		      writer.close();
		      
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
		        
		        //Map and sort distances of images
		        Map<String, Double> map = new HashMap<String, Double>();
		      		        
		        for(int i = 1; i < images.size(); i++)
		        {
		            map.put(images.get(i), listOfDistances.get(i-1));		       
		        }
		        
			    Map<String, Double> treeMap = new TreeMap<String, Double>();
			    treeMap.putAll(map); 

		        SortedSet<Entry<String, Double>> finalMap = entriesSortedByValues(treeMap);

		        System.out.println(finalMap);

			    

			    
		        Collections.sort(listOfDistances);
		        List<String> sortedImages = new ArrayList<String>();
		        

		        Iterator<Map.Entry<String, Double>> it = finalMap.iterator();

		        while(it.hasNext())
		        {
		        	sortedImages.add(it.next().getKey());
		        }
		        

		        for(int i = 0; i < sortedImages.size(); i++) {
		        	KeypointDetector.drawKeypoints(sortedImages.get(i), i);
		        }      		        
		   

		        for(int i = 1; i < images.size(); i++)
		        {
		        	sortedImages.add(finalMap.first().getKey());
		        	finalMap.remove(finalMap.first());

		        }
		        
		        //Berechne Score der Top Ten Bilder
		        int score = 0;
		        for(int i=0; i < 10; i++)
		        {
		        	if(trimPath(sortedImages.get(i)).equals(refImage))
		        	{
		        		score += 10 - i;
		        	}
		        }
		        
		        BufferedWriter scoreWriter = new BufferedWriter(new FileWriter("./resources/logs/score.txt", true));
				  scoreWriter.write("Score für " + inputImage.substring(inputImage.lastIndexOf("\\")+1) + ": " + score + "\n");
				  scoreWriter.close();
	      }
	}


	static <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
            new Comparator<Map.Entry<K,V>>() {
                @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                    int res = e1.getValue().compareTo(e2.getValue());
                    return res != 0 ? res : 1;
                }
            }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }
	
	private static String trimPath(String image)
	{
		image = image.substring(image.lastIndexOf("\\")+1);
		return image.substring(0, image.length()-6);
	}
	
}  	



   



