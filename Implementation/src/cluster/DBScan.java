package cluster;

import java.awt.geom.Point2D;




import org.apache.commons.math3.ml.distance.EarthMoversDistance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.math3.stat.clustering.*;
import org.opencv.core.KeyPoint;
import org.opencv.core.MatOfKeyPoint;



public class DBScan {
	
	public static List<double[]> _massList = new ArrayList<double[]>();

	/**
	 * 
	 * @param descriptor MatOfKeyPoint jede Zeile muss einzeln augewertet werden bei Clustering
	 */
	public static List<double[]> cluster(MatOfKeyPoint descriptor)
	{
	List<double[]> centroidlist = new ArrayList<double[]>();
		
	Collection<EuclideanDoublePoint> matOfKeypoints = new ArrayList<EuclideanDoublePoint>();	
	
	for(int row = 0; row < descriptor.rows(); row++)
	{
		double[] param = new double[64];
		for(int col = 0; col < descriptor.row(row).cols(); col++)
		{
			param[col] = descriptor.get(row, col)[0];
		}

		EuclideanDoublePoint edp = new EuclideanDoublePoint(param);
		matOfKeypoints.add(edp);
	}
		DBSCANClusterer<EuclideanDoublePoint> cls = new DBSCANClusterer<EuclideanDoublePoint>(0.1, 4);
	    List<Cluster<EuclideanDoublePoint>> list = cls.cluster(matOfKeypoints);
	    System.out.println("\n___________________________Image_______________________________\n: " );
	    double[] masses = new double[list.size()];
	    for(int count = 0; count < list.size(); count++)
	    {
	    	double[] centroid = new double[64];
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
		
			double[] sumOfDescriptor = new double[64];
			
			for(int count2 = 0; count2 < list.get(count).getPoints().size(); count2++)
	    	{
				//Aufsummierung der einzelnen Keypoints im Cluster
	    		EuclideanDoublePoint edp = list.get(count).getPoints().get(count2);
	    		double[] newcenter =  edp.getPoint();
	    		for(int i = 0; i < 64; i++)
	    		{
	    		sumOfDescriptor[i] += newcenter[i];
	    		}
	    	}
			
			//Berechnung des Mittelwerts im Cluster
			for(int i = 0; i < 64; i++)
    		{
			centroids[i] = sumOfDescriptor[i] / list.get(count).getPoints().size();
    		}
			
			StringBuilder allCentroids = new StringBuilder();
	
			for(int i = 0; i <64; i++)
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
