package Distanzmasse;

import java.util.List;

import cluster.DBScan;

public class GewichteteDistanz {

	 
	 public static double calcDistances(List<List<double[]>> imageList)
	 {
		  double sumDist = 0; 
		  for(int i=0; i < imageList.get(0).size() ; i++)
		  {
		   	 for(int j=0; j < imageList.get(1).size(); j++)
		   	 {
		   		 double euclDist = euclDistance(imageList.get(0).get(i), imageList.get(1).get(j));
		   		 System.out.println("\n Distance between Cluster " + i + " of Image 1 and Cluster " + j + " of Image 2: " + euclDist);
		   		 
		   		 System.out.println("Mass for Cluster " + i + " and " + j + ": " + (DBScan._massList.get(0)[i] + DBScan._massList.get(1)[j]));
		   		 double weightDist = euclDist * (DBScan._massList.get(0)[i] + DBScan._massList.get(1)[j]);
		   		 
		   		 sumDist += weightDist;
		   	 } 
		
		  }
		  double overAllMass = 0;
		  
		  for(int i = 0; i < DBScan._massList.size(); i++)
		  {
			  for(int j = 0; j < DBScan._massList.get(i).length; j++)
			  {
				  overAllMass += DBScan._massList.get(i)[j];
			  }
		  }
		  
		  double normDist = sumDist / overAllMass;
		  
		  return normDist;
		  
		  //Probleme mit EMD: Optimierungsprobleme bei Zuweisung der Erdhaufen zu Löchern
		  //				  Penalty, falls Erde übrig bleibt
	 }
	  
	  private static double euclDistance(double[] centroid1, double[] centroid2)
	   {
		double sumsquaredistances = 0;
		for(int i = 0; i < centroid1.length; i++)
		{
			double squaredistances = (centroid1[i] - centroid2[i]) *  (centroid1[i] - centroid2[i]);
			sumsquaredistances += squaredistances;
		}
		
		double euclideanDist = Math.sqrt(sumsquaredistances);
		
		return euclideanDist;
		   
	   }
}
