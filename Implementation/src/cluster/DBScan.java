package cluster;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.math3.stat.clustering.*;
import org.opencv.core.KeyPoint;
import org.opencv.core.MatOfKeyPoint;

public class DBScan {
	
	public static Collection<EuclideanDoublePoint> matOfKeypoints;

	
	public static void cluster(MatOfKeyPoint kp)
	{
		
	Collection<KeyPoint> colkp = kp.toList();
	for(KeyPoint KP : colkp)
	{
		double[] param = {KP.pt.x, KP.pt.y, KP.angle};
		
		System.out.println("\n" + param);
		
		EuclideanDoublePoint edp = new EuclideanDoublePoint(param);
		matOfKeypoints.add(edp);
	}
	
	
	//Collection<EuclideanDoublePoint> matOfKeypoints =  colkp;

    DBSCANClusterer<EuclideanDoublePoint> cls = new DBSCANClusterer<EuclideanDoublePoint>(20, 4);
    List<Cluster<EuclideanDoublePoint>> list = cls.cluster(matOfKeypoints);
    System.out.println("\n" + list);
	}
    
}
