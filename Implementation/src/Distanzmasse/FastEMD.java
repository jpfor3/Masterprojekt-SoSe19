 package Distanzmasse;

import java.util.ArrayList;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Path.*;


import java.util.List;
import com.telmomenezes.jfastemd.*;



import cluster.DBScan;

public class FastEMD {

	private static List<List<double[]>> _centeredDescriptors;
 	private static List<Double> _listOfDistances = new ArrayList<Double>();
 	
 	public static List<Long> _distDurations = new ArrayList<Long>();

	 /**
	  * Method to which calculates the distances between every image based on EMD.
	  * To perform the EMD algorithm an external .jar file from GitHub has been used. In order to use
	  * this external .jar the descriptors and corresponding weights of every image have to be transformed into
	  * Signatur class.
	  * 
	  * @param centeredDescriptors
	  * @throws IOException 
	  */
	 public static List<Double> calcDistances(List<List<double[]>> centeredDescriptors, List<String> images, OutputStreamWriter osw, int emdpenalty, boolean hamming) throws IOException
	 { 	  	  
		 _centeredDescriptors = centeredDescriptors;
		 _listOfDistances.clear();
	  	  /**
	  	   * Creation of double arrays which contain every descriptor of an image. Therefore all features of all 
	  	   * descriptors have to be aggregated in one array of the size of the number of features 
	  	   * times the number of descriptors. 
	  	   */
		 
		 /**
		  * Creating of array for input image
		  */
		  //Creating the feature array with all features
	  	  double[] featuresG = new double[centeredDescriptors.get(0).size() * DBScan._descriptor.cols()];
	  	  
	  	  //Creating the array with the corresponding weights of every feature
	  	  double[] weightsG = new double[DBScan._massList.get(0).length * DBScan._descriptor.cols()];
	  	  
	  	  int arrayPosG = 0;
		  int weightPosG = 0;
		  int featurePosG = 0;
		  
		  //Determining the features and weights for image g
		  for(int i=0; i < centeredDescriptors.get(0).size() ; i++)
		  {				  	  

			  //Copying the features from i into the feature array
			  featuresG = concatenateFeaturesG(featuresG, arrayPosG, featurePosG, i);
			  
			  //Inserting the weights of every descriptor i 
			  weightsG = concatenateWeightsG(weightsG, arrayPosG, weightPosG, i);		
			  
			  arrayPosG += DBScan._descriptor.cols();			 
		  }
		  
		  //Inserting feature and weight arrays into Signature class 
		  Signature sigG = new Signature();
		  
		  //Inserting weights into Signature class
	   	  sigG.setWeights(weightsG);

	   	  //Transforming features array of type double into type feature
	   	  Feature1D[] features1DG = new Feature1D[featuresG.length];
	   	  for(int featcount = 0; featcount < featuresG.length; featcount++)
	   	  {
	   		features1DG[featcount] = new Feature1D(featuresG[featcount]);
	   	  }
	   	  
	   	  //Inserting features into Signature class
	   	  sigG.setNumberOfFeatures(features1DG.length);
	   	  sigG.setFeatures(features1DG);
	   	  
		  
		  /**
		   * Creating arrays for all h compare images 
		   */
	   	  
		  for(int h=1; h < centeredDescriptors.size(); h++)
		  {
			  long startingTime = System.currentTimeMillis();

			  //Creating the feature array with all features
		  	  double[] featuresH = new double[centeredDescriptors.get(h).size() * DBScan._descriptor.cols()];

		  	  //Creating the array with the corresponding weights of every feature
		  	  double[] weightsH = new double[DBScan._massList.get(h).length * DBScan._descriptor.cols()];

			 
			  int arrayPosH = 0;
			  int weightPosH = 0;
			  int featurePosH = 0;
			  
			//Determining the features and weights for image h
			  for(int j=0; j < centeredDescriptors.get(h).size() ; j++)
			  {				  	  
				  
				  //Copying the features from j into the feature array
				  featuresH = concatenateFeaturesH(featuresH, arrayPosH, featurePosH, h, j);
				  
				  //Inserting the weights of every descriptor j 
				  weightsH = concatenateWeightsH(weightsH, arrayPosH, weightPosH, h, j);		
				  
				  arrayPosH += DBScan._descriptor.cols();			 
			  }
			  
			  //Inserting feature and weight arrays into Signature class 
		   	  Signature sigH = new Signature();			
			  
		   	  //Inserting weights into Signature class
		   	  sigH.setWeights(weightsH);
		   	  
		   	  //Transforming features array of type double into type feature
		   	  Feature1D[] features1DH = new Feature1D[featuresH.length];
		   	  for(int featcount = 0; featcount < featuresH.length; featcount++)
		   	  {
		   		features1DH[featcount] = new Feature1D(featuresH[featcount]);
		   	  }
		   	  
		   	  //Inserting features into Signature class
		   	  sigH.setNumberOfFeatures(features1DH.length);
		   	  sigH.setFeatures(features1DH);
		   	  
		   	  
//		   	  String Euklid_Hamming = new String();
//		   	  if(hamming) { Euklid_Hamming = "The EMD Distance with Hamming is";} else {Euklid_Hamming = "The EMD Distance with Euclid is";}
//		   	  
		   	  double EMDdistance = JFastEMD.distance(sigG, sigH, emdpenalty, hamming);
//		   	  System.out.println("\n" + Euklid_Hamming + "\t\t" + EMDdistance + "\t" + "between" + "\t\t" + images.get(0).substring(images.get(0).lastIndexOf("\\")+1) + "\t" + "and" + "\t\t" + images.get(h).substring(images.get(h).lastIndexOf("\\")+1));
		   	  _listOfDistances.add(EMDdistance);

 		   	  System.out.println("\nDistanz zwischen " + images.get(0).substring(images.get(0).lastIndexOf("\\")+1) + " und " + images.get(h).substring(images.get(h).lastIndexOf("\\")+1) + " ist " + EMDdistance);
 		   	  writeToFile("Distanz zwischen " + images.get(0).substring(images.get(0).lastIndexOf("\\")+1, images.get(0).length() - 4) + " und " + images.get(h).substring(images.get(h).lastIndexOf("\\")+1, images.get(h).length() - 4) + " => " + EMDdistance + "\n" , osw);

 		   	  long endTime = System.currentTimeMillis();
 		   	  long duration = endTime - startingTime;
 		   	  
 		   	  _distDurations.add(duration);
 			
 		   	}
		  
		  writeToFile("___________________________________________________________________________________\n\n", osw);

		  return _listOfDistances;
		  		  
	 }
	 
	 private static double[] concatenateFeaturesG(double[] featuresG, int arrayPos, int featurePos, int descriptor)
	 {
		  double[] features = featuresG;
		  for(int pos = 0; pos < _centeredDescriptors.get(0).get(descriptor).length; pos++)
		  {
			  featurePos = arrayPos + pos;
			  features[featurePos] = _centeredDescriptors.get(0).get(descriptor)[pos];
		  }
		  return features;
	 }
	 
	 private static double[] concatenateFeaturesH(double[] featuresH, int arrayPos, int featurePos, int image, int descriptor)
	 {
		  double[] features = featuresH;
		  for(int pos = 0; pos < _centeredDescriptors.get(image).get(descriptor).length; pos++)
		  {
			  featurePos = arrayPos + pos;
			  features[featurePos] = _centeredDescriptors.get(image).get(descriptor)[pos];
		  }
		  return features;
	 }
	 
	 
	 private static double[] concatenateWeightsG(double[] weightsG, int arrayPos, int weightPos, int descriptor)
	 {
		  double[] weights = weightsG;
		  for(int pos = 0; pos < _centeredDescriptors.get(0).get(descriptor).length; pos++)
		  {
			  weightPos = arrayPos + pos;
			  weights[weightPos] = DBScan._massList.get(0)[descriptor];
		  }
		  return weights;
	 }
	 
	 private static double[] concatenateWeightsH(double[] weightsH, int arrayPos, int weightPos, int image, int descriptor)
	 {
		  double[] weights = weightsH;
		  for(int pos = 0; pos < _centeredDescriptors.get(image).get(descriptor).length; pos++)
		  {
			  weightPos = arrayPos + pos;
			  weights[weightPos] = DBScan._massList.get(image)[descriptor];
		  }
		  return weights;
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
