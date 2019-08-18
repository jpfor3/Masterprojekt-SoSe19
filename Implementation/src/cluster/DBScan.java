package cluster;

import java.awt.geom.Point2D;


import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.math3.stat.clustering.*;
import org.opencv.core.KeyPoint;
import org.opencv.core.MatOfKeyPoint;

//import com.sun.tools.javac.code.Attribute.Array;

public class DBScan {
	


	/**
	 * 
	 * @param descriptor MatOfKeyPoint jede Zeile muss einzeln augewertet werden bei Clustering
	 */
	public static void cluster(MatOfKeyPoint descriptor)
	{
		
	Collection<EuclideanDoublePoint> matOfKeypoints = new ArrayList<EuclideanDoublePoint>();	
	Collection<KeyPoint> colkp = descriptor.toList();
	
	int i = 0;
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
	    System.out.println("\nListe: " );
	    for(int count = 0; count < list.size(); count++)
	    {
	    //System.out.println(list.get(count).getPoints()+ "\n");
	    }
    
	    center(list);
	}
	
	
    //Center berechnen
	private static void center(List<Cluster<EuclideanDoublePoint>> list)
	{
		for(int count = 0; count < list.size(); count++)
	    {
			double[] sumOfDescriptor = new double[64];
			double[] centroid = new double[64];
			
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
			centroid[i] = sumOfDescriptor[i] / list.get(count).getPoints().size();
    		}
			System.out.println("Centroid("+count+"): "+centroid [1]);
		}
	}
	
	
	
	
    //Center berechnen
	private static void centering(List<Cluster<EuclideanDoublePoint>> list)
	{
	    List<double[]> centroids = new ArrayList<double[]>();

	    for(int count = 0; count < list.size(); count++)
	    {
	    	double sum_x = 0;
	    	double sum_y = 0;
	    	double sum_re = 0;

 
	    	for(int count2 = 0; count2 < list.get(count).getPoints().size(); count2++)
	    	{
	    		//Aufsummierung der einzelnen Keypoints im Cluster
	    		EuclideanDoublePoint edp = list.get(count).getPoints().get(count2);

		    	double[] newcenter =  edp.getPoint();
		    	
		    	sum_x += newcenter[0];
		    	sum_y += newcenter[1];
		    	sum_re += newcenter[4];
		    	
	    	}
	        
	    	//Berechnung des Mittelwerts im Cluster
	    	double x = sum_x / list.get(count).getPoints().size();
	    	double y = sum_y / list.get(count).getPoints().size();
	    	double re = sum_re / list.get(count).getPoints().size();

	    	
	    	
	    	double[] center = {x,y,re}; 
	    	System.out.println("\n" + count + ") Center: " + center[0] + "; " + center[1] + "; " + center[2]);
	    
	    	//Erstellen einer ArrayList mit den gegebenen Centroids
	    	centroids.add(center);
	    }
	    
	    
	}
	
	//TODO: Distanzmaße berechnen
}