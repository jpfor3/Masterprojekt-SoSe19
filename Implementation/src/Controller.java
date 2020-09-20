import Distanzmasse.FastEMD;


import Distanzmasse.JaccardDistance;
import cluster.DBScan;
import keypointdetector.KeypointDetector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
	
	private static List<Long> _clusterDurations = new ArrayList<Long>();

	public static FileOutputStream fos = null;
	private static int final_score;
	private static int bw_score = 0;
	private static int rot_score = 0;
	private static int mir_score = 0;

	private String dateiname;
		
	public Controller()
	{
	     System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	
	public static void calcJaccard(List<MatOfKeyPoint> descriptorList, int threshold)
    { 
      List<Double> jacList = JaccardDistance.calculateJaccard(descriptorList, threshold);
      System.out.println("Jac Distance: " + jacList.get(0));
    }

	/**
	 * For Call over Console Prompt
	 */
	public static void main(String[] args) throws IOException {
		
		 List<String> images = new ArrayList<String>();
		 String imageFolder = "C:\\Users\\ACER\\Git Repositories\\Projekt Master Branch2\\Masterprojekt-SoSe19\\Implementation\\resources\\images";
		 File folder = new File(imageFolder);
	     File[] listOfFiles = folder.listFiles();

	     for(File file : listOfFiles)
	     {
	    	 String imagePath = file.getPath();
	    	 String imageName = imagePath.substring(imagePath.lastIndexOf("\\")+1);

	    	 if(!imageName.contains("_"))
	    	 {
	    		 images.add(file.getPath());
	    	 }
	     }

	     for(String image: images)
	     {
	     compareImages(image, imageFolder, 8, 0.2, -1, "EMD (Euclid)");

	     }
	}
	
   	public static void compareImages(String inputImage, String imageDirectory, int minSamples, double eps, int emdpenalty, String distanceAlgorithm) throws IOException
   	{     		
   		 String ordnername = imageDirectory.substring(0, imageDirectory.lastIndexOf("\\")+1) + trimPathLong(inputImage) + "_minSamples-" + String.valueOf(minSamples) + "_eps-" + String.valueOf(eps) + "_penalty-" + String.valueOf(emdpenalty) + "_algorithm-" + distanceAlgorithm;
	     String dateiname = ordnername + "\\" + trimPathLong(inputImage) + "_minSamples-" + String.valueOf(minSamples) + "_eps-" + String.valueOf(eps) + "_penalty-" + String.valueOf(emdpenalty) + "_algorithm-" + distanceAlgorithm + ".txt";
	     File folder2 = new File(ordnername);
	     folder2.mkdirs();
	     fos = new FileOutputStream(dateiname);        
	     OutputStreamWriter osw = new OutputStreamWriter(fos);
   		
		 long startTime = System.nanoTime();

		 File folder = new File(imageDirectory);
	     File[] listOfFiles = folder.listFiles();
	     List<String> images = new ArrayList<String>();
	
	     _centeredDescriptors.clear();
	     _descriptorList.clear();
	     
	     //√úberpr√ºfe zu welcher Kategorie das Input Image geh√∂rt
	     String refImage = null;
	     switch(trimPathShort(inputImage)){
	        case "Auto":
	        	refImage = "Auto";
	        break;
	        case "Bike":
	        	refImage = "Bike"; 
	        break;
	        case "Landschaft":
	        	refImage = "Landschaft"; 
	        break;
	        case "Mensch":
	        	refImage = "Mensch"; 
	        break;
	        case "Pferd":
	        	refImage = "Pferd"; 
	        break;
	        case "Rad":
	        	refImage = "Rad";
	        break;
	        case "Schaf":
	        	refImage = "Schaf"; 
	        break;
	        
	     }
	     
	
	     /**
	      * Adding input image
	      */
	     // Alle Bilder zur Liste hinzuf√ºgen
	     images.add(inputImage);
	     for(File file : listOfFiles)
	     {
	    	 images.add(file.getPath());
	     }
	     
	     
	     writeToFile("Zeitstempel: " + java.time.LocalDateTime.now() + "  ///  ", osw);
	     writeToFile("Zu vergleichendes Bild: " + trimPathLong(inputImage) + "\n\n", osw);
	     writeToFile("________________________________________________________________________________________", osw);

	     //Detecting Keypoints of images
	      KeypointDetector KPDetector = new KeypointDetector(images, ordnername); 
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
		    	  long startingTime = System.currentTimeMillis();

			   	  List<double[]> clusterlist = DBScan.cluster(kp, minSamples, eps, osw);
			   	  
		    	  long endTime = System.currentTimeMillis();
		    	  long duration = endTime - startingTime;
		    	  _clusterDurations.add(duration);    	  
			   	  
			   	  _centeredDescriptors.add(clusterlist);   
		      }

		     
		      writeToFile("\n" + "Distanzen zwischen den Bildern:" + "\n\n", osw);
		      
		      System.out.println("Clustering Ended....");
		
		      System.out.println("Calculating Distances....");
		      
		      
		      List<Double> listOfDistances = new ArrayList<Double>();
		      
		      // check which distance algorithm was chosen in the UI
		      if(distanceAlgorithm.equals("EMD (Euclid)")) {
		    		System.out.println("EMD (Euclid) chosen");
		    		listOfDistances = FastEMD.calcDistances(_centeredDescriptors, images, osw, emdpenalty, false);
			    }
			    else if(distanceAlgorithm.equals("EMD (Hamming)")) {
			    	System.out.println("EMD (Hamming) chosen");
		    		listOfDistances = FastEMD.calcDistances(_centeredDescriptors, images, osw, emdpenalty, true);
			    }
		    		      
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
		        

//		        for(int i = 0; i < sortedImages.size(); i++) {
//		        	KeypointDetector.drawKeypoints(sortedImages.get(i), i, ordnername);
//		        }      		        
		   

		        for(int i = 1; i < images.size(); i++)
		        {
		        	sortedImages.add(finalMap.first().getKey());
		        	finalMap.remove(finalMap.first());

		        }
		        
		      //Berechne Score der Top Ten Bilder sowie Score f¸r die Transformationen Black/White, Rotatetd, Mirrored
		        final_score = 0; 	 
		        bw_score = 0;
		    	rot_score = 0;
		    	mir_score = 0;
		        for(int i=0; i < 10; i++)
		        {
		        	String refFile = sortedImages.get(0).substring(sortedImages.get(i).lastIndexOf("\\")+1);
		        	String refFileShort = refFile.substring(0, refFile.length() - 4);
		        	
		        	String fileName = sortedImages.get(i).substring(sortedImages.get(i).lastIndexOf("\\")+1);
		        	int index = fileName.indexOf("_");

		        	if(index > 0)
		        	{
			        	String fileNameShort = fileName.substring(0, index);

			        	if(fileNameShort.equals(refFileShort))
			        	{
			        		final_score += 2;
			        		
			        		System.out.println(fileName);
			        		//Berechne Score f¸r Transformationen
			        		if(fileName.contains("_bw")){
			        			bw_score = 10 - i;
			        		}
			        		if(fileName.contains("_rot")){
			        			rot_score = 10 - i;
			        		}
			        		if(fileName.contains("_mir")){
			        			mir_score = 10 - i;
			        		}
			        		
			        	} else if(fileNameShort.substring(0, fileNameShort.length() - 2).equals(refImage))
						{
							final_score += 1;
						}
		        	
		        	} else if(trimPathShort(sortedImages.get(i)).equals(refImage))
		        	{
		        		final_score += 1;
		        	}
		        }
        
		          long elapsedTime = System.nanoTime() - startTime;
		          
		          writeToFile("\nExecution Time: " + elapsedTime/1000000000 + " s", osw);
		          writeToFile("\nScore (Image): " + final_score + "\n\n", osw);
		          
		    	  writeToFile("Score (Schwarz-weiﬂ): " + bw_score + "\n", osw);
		    	  writeToFile("Score (Rotiert 90 Grad): " + rot_score + "\n", osw);
		    	  writeToFile("Score (Gespiegelt): " + mir_score + "\n", osw);
	 		   	  writeToFile("___________________________________________________________________________________\n\n", osw);
		          writeToFile("Performance: " + "\n", osw);
				
		          
		          for(int i = 1; i < images.size(); i++)
			      {
			    	  long kpDuration = KeypointDetector._kpDurations.get(i);
			    	  long clusterDuration = _clusterDurations.get(i);
			    	  long distanceDuration = FastEMD._distDurations.get(i-1);
			    	  long sum = kpDuration + clusterDuration + distanceDuration;
			    	  writeToFile("\nF√ºr " + images.get(i).substring(images.get(i).lastIndexOf("\\")+1) + ": \n", osw );
			    	  writeToFile("Berechnungszeit KPs: " + kpDuration +"\n", osw);
			    	  writeToFile("Berechnungszeit Cluster: " + clusterDuration +"\n", osw);
			    	  writeToFile("Berechnungszeit Distanz: " + distanceDuration +"\n", osw);
			    	  writeToFile("Gesamt:  " + sum + " ms\n", osw);
			      }

	      }     
	      
	      
          closeFile(osw);
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
	
	private static String trimPathShort(String image)
	{
		image = image.substring(image.lastIndexOf("\\")+1);
		return image.substring(0, image.length()-6);
	}
	
	private static String trimPathLong(String image)
	{
		image = image.substring(image.lastIndexOf("\\")+1);
		return image.substring(0, image.length()-4);
	}	
	
	public static void writeToFile(String message, OutputStreamWriter osw){
	    try {
	        osw.write(message);
	        osw.flush();
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	}
	
	public static void closeFile(OutputStreamWriter osw) throws IOException {
		osw.close();
	}
	
}  	



   
