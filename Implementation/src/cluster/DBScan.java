package cluster;

import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.math3.stat.clustering.*;
import org.opencv.core.MatOfKeyPoint;



public class DBScan {
	
	public static List<double[]> _massList = new ArrayList<double[]>();
	public static MatOfKeyPoint _descriptor;
	/**
	 * 
	 * @param descriptor MatOfKeyPoint jede Zeile muss einzeln augewertet werden bei Clustering
	 * @param eps 
	 * @param minSamples 
	 */
	public static List<double[]> cluster(MatOfKeyPoint descriptor, int minSamples, double eps, OutputStreamWriter osw)
	{
	_descriptor = descriptor;
	List<double[]> centroidlist = new ArrayList<double[]>();
		
	Collection<EuclideanDoublePoint> matOfKeypoints = new ArrayList<EuclideanDoublePoint>();	
	
	//System.out.println("\nKP: " + _descriptor.cols());
	for(int row = 0; row < descriptor.rows(); row++)
	{
		double[] param = new double[_descriptor.cols()];
		for(int col = 0; col < descriptor.row(row).cols(); col++)
		{
			param[col] = descriptor.get(row, col)[0];
		}

		EuclideanDoublePoint edp = new EuclideanDoublePoint(param);
		matOfKeypoints.add(edp);
	}
		/**
		 * @param eps: The maximum distance between two samples for one to be considered as in the 
		 * neighborhood of the other
		 * @param min_samples: The number of samples (or total weight) in a neighborhood for a 
		 * point to be considered as a core point
		 */
		DBSCANClusterer<EuclideanDoublePoint> cls = new DBSCANClusterer<EuclideanDoublePoint>(eps, minSamples);
	    List<Cluster<EuclideanDoublePoint>> list = cls.cluster(matOfKeypoints);
	    System.out.println("\n___________________________Image_______________________________\n: " );
	    double[] masses = new double[list.size()];
	    for(int count = 0; count < list.size(); count++)
	    {
	    	double[] centroid = new double[_descriptor.cols()];
	    	int mass = list.get(count).getPoints().size();
	    	masses[count] = mass;
		    centroid = center(count, list, mass, centroid);
		    centroidlist.add(centroid);
		}
	    
	    _massList.add(masses);
	    
	    return centroidlist;

	}
	

	
    //Center berechnen
	private static double[] center(int count, List<Cluster<EuclideanDoublePoint>> list, int mass, double[] centroid)
	{
			double[] centroids = centroid;
		
			double[] sumOfDescriptor = new double[_descriptor.cols()];
			
			for(int count2 = 0; count2 < list.get(count).getPoints().size(); count2++)
	    	{
				//Aufsummierung der einzelnen Keypoints im Cluster
	    		EuclideanDoublePoint edp = list.get(count).getPoints().get(count2);
	    		double[] newcenter =  edp.getPoint();
	    		for(int i = 0; i < _descriptor.cols(); i++)
	    		{
	    		sumOfDescriptor[i] += newcenter[i];
	    		}
	    	}
			
			//Berechnung des Mittelwerts im Cluster
			for(int i = 0; i < _descriptor.cols(); i++)
    		{
			centroids[i] = sumOfDescriptor[i] / list.get(count).getPoints().size();
    		}
			
			StringBuilder allCentroids = new StringBuilder();
	
			for(int i = 0; i <_descriptor.cols(); i++)
			{
				allCentroids.append(centroids[i] + "; ");
			}
			System.out.println("Centroid("+count+"): [" + allCentroids + "]");
			System.out.println("Mass: "+ mass+"\n");			
			
			return centroids;
		
	}
	
	public static double[][] groundDistances(double[] centroid)
	{
		
		
		return null;
	}
	
	/**private static double[][] addCentroidstoArray(double[] centroid)
	{
		ArrayList<double[]> centroidlist = new ArrayList<double[]>();
		centroidlist.add(centroid);
		
		double[][] newcentroidlist = (double[][]) centroidlist.toArray();
		
		return newcentroidlist;
	}
	*/

}
