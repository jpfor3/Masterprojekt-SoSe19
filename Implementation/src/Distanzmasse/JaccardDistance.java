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
	
	private static List<Double> calculateJaccard(List<MatOfKeyPoint> Descriptors, double threshold) {
		
		_Descriptors = Descriptors;
		
		/*
		 * 1. Deskriptoren für Inputbild und zu vergleichendes Bild laden, jeweils in set1 und set2
		 * 2. Distanz zwischen einem Deskriptor aus Testbild mit ALLEN des anderen Bilds vergleichen
		 * 3. Diejenigen filtern, die kleiner als der Threshold sind (0,5)
		 * 
		 * 
		 * 
		 */
		
		// Initialize list to contain the jaccard indexes
		List<Double> jaccardList = new ArrayList<Double>();
		
		// Define 2 sets of Descriptors to compare
		MatOfKeyPoint set1 = null;
		MatOfKeyPoint set2 = null;
		
		double numerator;
		double denominator;
		
		/* For-Loop to split the descriptor matrix
		  set1 should contain all descriptors from the first picture
		  set2 should contain all descriptors from the next picture
		*/
		
		// First loop to put 
		for (int g=0; g < 1; g++) {
			for (int h=1; h < _Descriptors.size(); h++) {
				
				set1 = _Descriptors.get(g);
				set2 = _Descriptors.get(h);
				
				// call function to check the distance between single descriptors in the sets
				numerator = checkDistances(set1, set2, threshold);
				denominator = numerator + ((set1.cols() * set1.rows()) - numerator) + ((set2.cols() * set2.rows()) - numerator);
				
				jaccardList.add(numerator/denominator);
				
				set1 = null;
				set2 = null;
				numerator = 0;
				denominator = 0;
				
				
			}
		}
		return jaccardList;
		
	}
	
	
	//______________________________________________________________________________________________________

	
	
	private static double checkDistances(MatOfKeyPoint set1, MatOfKeyPoint set2, double threshold) {
		double x = 0;
		// For-loops to compare the keypoints of set1 with each of set2 and keep all those 
		// whose distance is smaller than the threshold
		for(int c1=0; c1 < set1.cols(); c1++) {
			for(int r1=0; r1 < set1.rows(); r1++) {
				for(int c2=0; c2 < set2.cols(); c2++) {
					for(int r2=0; r2 < set2.rows(); r2++) {
						// 

						// Denkfehler? Die Deskriptoren, bei denen die Distanz kleiner als der Threshold ist
						// sind sowieso sehr ähnlich. Warum dann eine Menge erstellen, in der diese jeweils
						// drin sind und damit weiterrechnen?
						
						//		
						double[] kp1;
						double[] kp2;
						
						kp1 = set1.get(r1, c1);
						kp2 = set2.get(r2, c2);
						
						if((kp1[0] - kp2[0]) < threshold) {
							x += 1;
							}
						else {
							x += 0;
							}
						
						kp1 = null;
						kp2 = null;
						}
					}
				} 
			}
		// x contains the total distances;
		return x;
		
		}
	}


	
	
	


