import Distanzmasse.FastEMD;
import cluster.DBScan;
import keypointdetector.KeypointDetector;
import org.apache.commons.collections15.BidiMap;
import org.apache.commons.collections15.bidimap.DualHashBidiMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.MatOfKeyPoint;


public class Main
{
   public static void main(String[] args ) throws IOException
   {
	 List<MatOfKeyPoint> _descriptorList = new ArrayList<MatOfKeyPoint>();
	 
	 List<List<double[]>> _centeredDescriptors = new ArrayList<List<double[]>>();
	 
     System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
     
     File folder = new File("resources/images/");
     File[] listOfFiles = folder.listFiles();
     List<String> images = new ArrayList<String>();

     /**
      * Adding input image
      */
     images.add("resources/images/Kimmich.jpg");
     
     /**
      * Deleting all buffered images if input image changes
      */
     BufferedReader reader = new BufferedReader(new FileReader("resources/index/idx.txt"));
     reader.readLine();
     reader.readLine();
     if(!images.get(0).equals(reader.readLine()))
	 {
    	 //Clearing index file
    	 PrintWriter writer = new PrintWriter("resources/index/idx.txt");
    	 writer.print("");
    	 writer.close();
    	 
    	 //Clearing image_distances file
    	 writer = new PrintWriter("resources/index/image_distances.txt");
    	 writer.print("");
    	 writer.close();
	 }
     reader.close();
     
     
     /**Indexing images by checking if calculations have been done 
      * on the given images during previous execution
     */
     int index = 0;
     reader = new BufferedReader(new FileReader("resources/index/idx.txt"));
     if(reader.readLine() != null)
     {
    	index = Integer.parseInt(reader.readLine());
     }
     else
     {
    	 index = 0;
     }
     reader.close();

     //Adding images for comparison
     for(int i = 0; i < listOfFiles.length; i++)
     {
         reader = new BufferedReader(new FileReader("resources/index/image_distances.txt"));
    	 boolean contains = false;
    	 for(int j = 0; j < index; j++)
    	 {
	    	 if (listOfFiles[i].isFile() && reader.readLine().equals(listOfFiles[i].getPath())) {
	         contains = true;
	    	 }   
	    	 reader.readLine();
    	 }
    	 if(!contains) {
    		 images.add(listOfFiles[i].getPath());
    	 }
     }
     reader.close();

     File folder2 = new File("resources/sorted_output_images/");
     File[] listOfFiles2 = folder2.listFiles();
     
     //Clean directories   
     for(int i = index; i < listOfFiles2.length; i++) 
     {
    	 listOfFiles2[i].delete();
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
      List<Double> listOfDistances = FastEMD.calcDistances(_centeredDescriptors, images);
      
      //Load buffered images into index
      List<Double> bufferedDistances = new ArrayList<Double>();
      List<String> bufferedImages = new ArrayList<String>();
      
      reader = new BufferedReader(new FileReader("resources/index/image_distances.txt"));

      for(int i = 1; i < index+1; i++)
      {
    	  bufferedImages.add(reader.readLine());
    	  bufferedDistances.add(new Double(reader.readLine()));
      }
      
      reader.close();
      
      images.addAll(bufferedImages);
      listOfDistances.addAll(bufferedDistances);
      
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
      
      appendingToTxt(sortedImages, listOfDistances, images);

   }

   
   private static void appendingToTxt(List<String> sortedImages, List<Double> listOfDistances, List<String> images) throws IOException 
   {
	   //Printing index Nr
	   FileWriter writer = new FileWriter("resources/index/idx.txt");
	   writer.append("\n" + sortedImages.size());
	   
	   //Printing input image
	   writer.append("\n" + images.get(0));
	   writer.close();
	   
	   writer = new FileWriter("resources/index/image_distances.txt");

	   for(int i = 0; i < sortedImages.size(); i++)
	      {
		   writer.append(sortedImages.get(i) + "\n");
		   writer.append(listOfDistances.get(i) + "\n");	      
		   }
	      
      writer.close();
	   
   }
   
   
}