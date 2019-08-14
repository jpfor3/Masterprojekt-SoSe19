package cluster;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;

public class KMeans {
	
	public static List<Mat> cluster(Mat cutout, int k) {
		Mat samples = cutout.reshape(1, cutout.cols() * cutout.rows());
		Mat samples32f = new Mat();
		samples.convertTo(samples32f, CvType.CV_32F, 1.0 / 255.0);
		
		Mat labels = new Mat();
		TermCriteria criteria = new TermCriteria(TermCriteria.COUNT, 100, 1);
		Mat centers = new Mat();
		Core.kmeans(cutout, k, labels, criteria, 1, Core.KMEANS_PP_CENTERS, centers);		
		return showClusters(cutout, labels, centers);
		
	}
	
	private static List<Mat> showClusters (Mat cutout, Mat labels, Mat centers) {
		centers.convertTo(centers, CvType.CV_8UC1, 255.0); //8 Bit Char Type der Mat
		centers.reshape(3); //3 Channels z.B. RGB
		
		List<Mat> clusters = new ArrayList<Mat>();
		for(int i = 0; i < centers.rows(); i++) {
			clusters.add(Mat.zeros(cutout.size(), cutout.type())); //Erstellt leere Mat mit gegebener Größe und Typ
		}
		Map<Integer, Integer> counts = new HashMap<Integer, Integer>();
		for(int i = 0; i < centers.rows(); i++) counts.put(i, 0);

		
		System.out.println("\n Zeilen Centers: " + centers.rows()+ "\n Spalten Centers: " + centers.cols()) ;
		System.out.println("\n Zeilen Keypoints: " + cutout.rows() + "\n Spalten Keypoints: " + cutout.cols());
		
		int rows = 0;
		for(int y = 0; y < cutout.rows(); y++) {
			for(int x = 0; x < cutout.cols(); x++) {
				int label = (int)labels.get(rows, 0)[0];
				int a = (int)centers.get(label, 6)[0];
				int b = (int)centers.get(label, 5)[0];
				int c = (int)centers.get(label, 4)[0];
				int d = (int)centers.get(label, 3)[0];
				int e = (int)centers.get(label, 2)[0];
				int f = (int)centers.get(label, 1)[0];
				int g = (int)centers.get(label, 0)[0];
				counts.put(label, counts.get(label) + 1); //Counter für Schreibzugriff auf Cluster geht um +1 hoch
				
				//System.out.println("\n Counts: " + counts);
				
				clusters.get(label).put(y, x, a, b, c, d, e, f, g);
				rows++;
			}
		}
		System.out.println(counts);
		return clusters;
	}

}
