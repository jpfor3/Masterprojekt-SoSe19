package Distanzmasse;

import java.lang.reflect.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
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
	private static int _threshold;
	private static Mat set1;
	private static Mat set2;
	private static int numerator;
	private static int denominator;
	public double jaccardindex;
	
	public static List<Double> calculateJaccard(List<MatOfKeyPoint> Descriptors, int threshold) {
		
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
		
		// Initialize list to collect the jaccard indexes
		List<Double> jaccardList = new ArrayList<Double>();


		List<double[]> distanceList = new ArrayList<double[]>();
		double[] temp = new double[3];
		
		double sharedDescriptors;
		double uniqueDescriptors;

		for(int bild=1; bild < _Descriptors.size(); bild++) {
			
			for(int i1=0; i1 < _Descriptors.get(0).rows(); i1++) {
				set1 = setDescriptor(_Descriptors.get(0), i1);
	
				for(int i2=0; i2 < _Descriptors.get(bild).rows(); i2++) {
					
					// set1 is the descriptor at position i1 of the first image, set2 the descriptor at position i2 of the next images
	
					set2 = setDescriptor(_Descriptors.get(1), i2);
	
					// distanceList elements contain arrays in the form [descriptor first image, descriptor of compared image, distance between both]
					temp[0] = i1;
					temp[1] = i2;
					temp[2] = calcL2distance(set1, set2);
					distanceList.add(temp);
					
					sharedDescriptors = calculateShared(distanceList, _threshold);
					uniqueDescriptors = sharedDescriptors + (_Descriptors.get(0).rows() + _Descriptors.get(1).rows() - sharedDescriptors);
					
					jaccardList.add(sharedDescriptors / uniqueDescriptors);
					
					System.out.println("The NORM-L2 Distance between descriptors " + temp[0] + " of chosen image and " + temp[1] + " of image " + bild + " is: " + temp[2]);

					sharedDescriptors = 0;
					uniqueDescriptors = 0;
					
					set2.dump();
				
				}
				set1.dump();
			}
		}

		return jaccardList;
	}
		
	public static Mat setDescriptor(MatOfKeyPoint descriptors, int actualRow) {
		Mat set = new Mat(1, descriptors.cols(), CvType.CV_32FC1);
		for (int h=0; h < descriptors.cols(); h++) {	
			set.put(actualRow, h, descriptors.get(actualRow, h));
//			System.out.println(Arrays.toString(set.get(actualRow, h)));
		}
		return set;
	} 

	
	public static double calcL2distance(Mat desc1, Mat desc2) {
		double dist_l2  = Core.norm(desc1, desc2, 4); // NORM_L2 = 4, see https://docs.opencv.org/3.4/javadoc/org/opencv/core/Core.html#NORM_L2
		return dist_l2;
	}
	

//__________________________________

	
	
	private static int calculateShared(List<double[]> distances, int threshold){
		List<double[]> temp1 = new ArrayList<double[]>();
		// loop to go through all elements
		for(int i = 0; i < distances.size(); i++) {
			if(distances.get(i)[2] < threshold) {
				temp1.add(distances.get(i));
			}
		}
		int countedDescriptors = 0;
		// now that all distances smaller than the threshold are filtered there has to be a function that iterates through the first column and counts all different once, 
		// same for column 2 so that all descriptors that appear are only counted once. To identify all unique descriptors we have to subtract the amount of distanceList 
		// from how many descriptors are there in total . So i
		List<double[]> temp2 = new ArrayList<double[]>();
		for(int j = 0; j < temp1.size(); j++) {
			if(!temp2.contains(temp1.get(j)[0])) {
				temp2.add(temp1.get(j));
				countedDescriptors++;
			}
		}
		List<double[]> temp3 = new ArrayList<double[]>();
		for(int k = 0; k < temp2.size(); k++) {
			if(!temp3.contains(temp2.get(k)[1])) {
				temp3.add(temp2.get(k));
				countedDescriptors++;
			}
		}
		temp1.clear();
		temp2.clear();
		temp3.clear();		
		
		return countedDescriptors;		
	}
	
	
}