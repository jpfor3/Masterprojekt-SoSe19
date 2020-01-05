package Distanzmasse;

import java.util.ArrayList;

import java.util.List;
import com.telmomenezes.jfastemd.*;



import cluster.DBScan;

public class FastEMD {

	private static List<List<double[]>> _centeredDescriptors;
 	private static List<Double> _listOfDistances = new ArrayList<Double>();

	 /**
	  * Method to which calculates the distances between every image based on EMD.
	  * To perform the EMD algorithm an external .jar file from GitHub has been used. In order to use
	  * this external .jar the descriptors and corresponding weights of every image have to be transformed into
	  * Signatur class.
	  * 
	  * @param centeredDescriptors
	  */
	 public static List<Double> calcDistances(List<List<double[]>> centeredDescriptors)
	 { 	  	  
		 _centeredDescriptors = centeredDescriptors;
	  	  /**
	  	   * Creation of double arrays which contain every descriptor of an image. Therefore all features of all 
	  	   * descriptors have to be aggregated in one array of the size of the number of features (in our case 64) 
	  	   * times the number of descriptors. 
	  	   */
		 
		 /**
		  * Creating of array for input image
		  */
		  //Creating the feature array with all features
	  	  double[] featuresG = new double[centeredDescriptors.get(0).size() * 64];
	  	  
	  	  //Creating the array with the corresponding weights of every feature
	  	  double[] weightsG = new double[DBScan._massList.get(0).length * 64];
	  	  
	  	  int arrayPosG = 0;
		  int weightPosG = 0;
		  int featurePosG = 0;
		  
		  //Determining the features and weights for image g
		  for(int i=0; i < centeredDescriptors.get(0).size() ; i++)
		  {				  	  

			  //Copying the features from i into the feature array
			  featuresG = concatenateFeaturesG(featuresG, arrayPosG, featurePosG, i);
			  
			  //Inserting the weights of every descriptor i 64 times in a row 
			  weightsG = concatenateWeightsG(weightsG, arrayPosG, weightPosG, i);		
			  
			  arrayPosG += 64;			 
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
			  //Creating the feature array with all features
		  	  double[] featuresH = new double[centeredDescriptors.get(h).size() * 64];

		  	  //Creating the array with the corresponding weights of every feature
		  	  double[] weightsH = new double[DBScan._massList.get(h).length * 64];

			 
			  int arrayPosH = 0;
			  int weightPosH = 0;
			  int featurePosH = 0;
			  
			//Determining the features and weights for image h
			  for(int j=0; j < centeredDescriptors.get(h).size() ; j++)
			  {				  	  
				  
				  //Copying the features from j into the feature array
				  featuresH = concatenateFeaturesH(featuresH, arrayPosH, featurePosH, h, j);
				  
				  //Inserting the weights of every descriptor j 64 times in a row 
				  weightsH = concatenateWeightsH(weightsH, arrayPosH, weightPosH, h, j);		
				  
				  arrayPosH += 64;			 
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
		   	  
		   	  //Calculate distance between image g and h
		   	  double EMDdistance = JFastEMD.distance(sigG, sigH, -1);
		   	  System.out.println("\nDistance between image 0 and image " + h + " is " + EMDdistance);
		   	  _listOfDistances.add(EMDdistance);
			   	  
			  }
		  
		 
		  return _listOfDistances;
		  		  
		  //Probleme mit EMD: Optimierungsprobleme bei Zuweisung der Erdhaufen zu L�chern
		  //				  Penalty, falls Erde �brig bleibt
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
}
