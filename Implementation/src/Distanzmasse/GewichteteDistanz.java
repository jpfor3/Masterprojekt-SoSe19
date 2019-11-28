package Distanzmasse;

import java.util.List;

import cluster.DBScan;

public class GewichteteDistanz {

	 
	 public static List<Double> calcDistances(List<List<double[]>> centeredDescriptors)
	 {
		  double sumDist = 0; 
		  List <Double> listOfDistances = null;
		  
		  for(int g=0; g < centeredDescriptors.size(); g++)
		  {
			  for(int h=0; h < centeredDescriptors.size(); h++)
			  {
				  for(int i=0; i < centeredDescriptors.get(g).size() ; i++)
				  {
				   	 for(int j=0; j < centeredDescriptors.get(h).size(); j++)
				   	 {
				   		 double euclDist = euclDistance(centeredDescriptors.get(g).get(i), centeredDescriptors.get(h).get(j));
				   		 //System.out.println("\n Distance between Cluster " + i + " of Image 1 and Cluster " + j + " of Image 2: " + euclDist);
				   		 
				   		 //System.out.println("Mass for Cluster " + i + " and " + j + ": " + (DBScan._massList.get(g)[i] + DBScan._massList.get(h)[j]));
				   		 double weightDist = euclDist * (DBScan._massList.get(g)[i] + DBScan._massList.get(h)[j]);
				   		 
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
				  listOfDistances.add(normDist);
				  System.out.println("\nDistance between image " + g + " and " + h);
			  }
		  }
		 
		  
		  
		  return listOfDistances;
		  
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
