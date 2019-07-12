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

import com.sun.tools.javac.code.Attribute.Array;

public class DBScan {
	


	/**
	 * 
	 * @param kp MatOfKeyPoint jede Zeile muss einzeln augewertet werden bei Clustering
	 */
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
		
	
    DBSCANClusterer<EuclideanDoublePoint> cls = new DBSCANClusterer<EuclideanDoublePoint>(80, 4);
    List<Cluster<EuclideanDoublePoint>> list = cls.cluster(matOfKeypoints);
    System.out.println("\nListe: " );
    for(int count = 0; count < list.size(); count++)
    {
    System.out.println(list.get(count).getPoints() + "\n");
    }
    
    //TODO: Distanzmaße berechnen
    
	}
    
}
