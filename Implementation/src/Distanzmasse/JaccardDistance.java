package Distanzmasse;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.MatOfKeyPoint;

import com.telmomenezes.jfastemd.*;

public class JaccardDistance {
	
	// Jaccard-Koeffizient: Zwei Mengen A und B
	// Betrag der Schnittmenge von A und B geteilt durch Vereinigungsmenge von A und B
	// |AnB| / |AuB|
	// Angepasster Jaccard-Index von Fabian:
	// |shared(A,B)| / | shared(A,B) + V(A) + V(B) 
	// shared( (a,b) | a€A, b€B dst(a,b) < Threshold )
	
	private static List<MatOfKeyPoint> _Descriptors;
	private static double _threshold;
	public double jaccardindex;
	
	private static List<double[]> calculateJaccard(List<MatOfKeyPoint> Descriptors, double threshold) {
		
		_Descriptors = Descriptors;
		_threshold = threshold;
		
		/*
		 * 1. Deskriptoren für Inputbild und zu vergleichendes Bild laden, jeweils in set1 und set2
		 * 2. Distanz zwischen einem Deskriptor aus Testbild mit ALLEN des anderen Bilds vergleichen
		 * 3. Diejenigen filtern, die kleiner als der Threshold sind (0,5)
		 * 
		 * Fabian:
		 * 1. Deskriptoren laden, set1 soll die Deskriptoren des Anfangsbildes haben, set2 die Deskriptoren des zu vergleichenden Bilds (heißt, Schleife für set2)
		 * 2. Mit Euklid/Manhatten die Distanzen der Deskriptoren berechnen und in Liste speichern (Listenelemente können so aussehen [DeskriptorBild1, DeskriptorBild2, Distanz])
		 * 3. Mit Schwellwert k die Distanzen filtern, die kleiner sind
		 * 
		 */
		
		// Initialize list to contain the jaccard indexes
		List<double[]> jaccardList = new ArrayList<double[]>();
		
		// Define 2 sets of Descriptors to compare
		List<double[]> set1 = new ArrayList<double[]>();
		List<double[]> set2 = new ArrayList<double[]>();
		
		double numerator;
		double denominator;
		
		// First, split the descriptors to compare them: This is a call for the first image, so the iterator is 0
		// The second set will be set dynamically in a loop in each comparison
		set1 = setDescriptor(set1, _Descriptors, 0);
		
		// Loop to calculate the distances
		for(int i=1; i < _Descriptors.get(0).rows(); i++) {
			set2 = setDescriptor(set2, _Descriptors, i);
			calculateEuclidDistances(jaccardList, set1, set2);
			set2.clear();
		}	
		
		return jaccardList;
	}
		
	public static List<double[]> setDescriptor(List<double[]> set, List<MatOfKeyPoint> descriptors, int iterator) {
		for (int h=0; h < _Descriptors.get(0).cols(); h++) {				
			set.add(_Descriptors.get(0).get(iterator, h));
		}
		return set;
	}
				
//				// call function to check the distance between single descriptors in the sets
//				numerator = checkDistances(set1, set2, threshold);
//				denominator = numerator + ((set1.cols() * set1.rows()) - numerator) + ((set2.cols() * set2.rows()) - numerator);
//				
//				jaccardList.add(numerator/denominator);
//				
//				set1 = null;
//				set2 = null;
//				numerator = 0;
//				denominator = 0;
//				
//
//		return jaccardList;
//		
//	}
//	
//	
//	//______________________________________________________________________________________________________
//
//	
//	
	private static List<double[]> calculateEuclidDistances(List<double[]> list, List<double[]> set1, List<double[]> set2) {
		// Set up loops to compare descriptors
		for(int g=0; g < set1.size(); g++) {
			for(int h=0; h < set2.size(); h++) {
				// Adds an array of the compared descriptors and their distance in the form [d1, d2, distance(d1,d2)]
				double temp[] = {g+1, h};
//				list.add([g+1, h+1, norm(set1.get(g),set2.get(h),NORM_L2));	
				
			}
		}
		return list;
			
		}
	
}
//
//
//	
//	
//	
//
//
