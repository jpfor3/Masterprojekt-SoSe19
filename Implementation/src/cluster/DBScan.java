
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

public class DBScan {



	/**
	 *
	 * @param kp MatOfKeyPoint jede Zeile muss einzeln augewertet werden bei Clustering
	 */
	@SuppressWarnings("deprecation")
	public static void cluster(MatOfKeyPoint kp)
	{

	Collection<EuclideanDoublePoint> matOfKeypoints = new ArrayList<EuclideanDoublePoint>();
	Collection<KeyPoint> colkp = kp.toList();

	int i = 0;
	for(KeyPoint KP : colkp)
	{
		i++;
		double[] param = {KP.pt.x, KP.pt.y, KP.size, KP.angle, KP.response, KP.octave, KP.class_id};
		EuclideanDoublePoint edp = new EuclideanDoublePoint(param);
		matOfKeypoints.add(edp);
	}


     DBSCANClusterer<EuclideanDoublePoint> cls = new DBSCANClusterer<EuclideanDoublePoint>(40, 4);
     List<Cluster<EuclideanDoublePoint>> list = cls.cluster(matOfKeypoints);
     System.out.println("\nListe: " );
     for(int count = 0; count < list.size(); count++)
     {
     System.out.println(list.get(count).getPoints() + "\n");
     createCentroids(list);
     //System.out.println("List of x,y,response" + createCentroids(list) + "\n");
     }
     
	}

	@SuppressWarnings("deprecation")
     // Center f¸r einzelne Cluster berechnen
     public static List<double[]> createCentroids(List<Cluster<EuclideanDoublePoint>> clusters) {
	
    	 List<Double> centroids = new ArrayList<Double>();
    	 double xMid = 0.0; double yMid = 0.0; double reMid = 0.0;
    	 
    	 for(Cluster<EuclideanDoublePoint> c : clusters) {
    		 for(int i = 0; i < c.getPoints().size(); i++) {
    			xMid += c.getPoints().get(i).getPoint()[0];
    			yMid += c.getPoints().get(i).getPoint()[1];
    			reMid += c.getPoints().get(i).getPoint()[4];
    		 }
    		 // Mittelwert der x-,y- und des Response-Werts aller 
    		 // EuclidianDoublePoints innerhalb des Clusters c
    		 xMid = xMid/c.getPoints().size();
    		 yMid = yMid/c.getPoints().size();
    		 reMid = reMid/c.getPoints().size();
    		 
    		 System.out.println(new double[] {xMid, yMid, reMid});
    		 
    		 centroids.addAll(new ArrayList<Double>());
    		 centroids.get(clusters.get(c.))
    		 xMid = 0.0; yMid = 0.0; reMid = 0.0;
    	 }
    	 
    	 return centroids;
     }
    		 
    		 
     
     
     //TODO: Distanzmaﬂe berechnen


}


