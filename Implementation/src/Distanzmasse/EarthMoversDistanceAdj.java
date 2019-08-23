package Distanzmasse;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import cluster.DBScan;
import cluster.KMeans;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collections; 
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

/**
 * Calculates the Earh Mover's distance (also known as Wasserstein metric) between two distributions.
 * Given objects are double[][] which contains Clustercentroids.
 * 
 * Example: a has 3 centroids, b has 2 -> 
 * 			a = [[2, 4, 7, 9.5], 
 * 				 [4, 5, 1, 12], 
 * 				 [13, 6.5, 8, 4]]
 * 			b = [[19, 2, 8, 4], 
 * 				 [6, 12, 11.5, 1]]
 * 
 * The algorithm works like this:
 * 			a[0][1] (=2) - b[0][1] (=19) = |-17| + a[0][2] (=4) + lD - b[0][2] (=5) = |-1| + ... +
 * now the second cluster in b is checked. Therefore j is needed as iterator, because i is still set on 0 because the second loop hasnt finished yet:
 * 	... + 	a[0][1] (=2) - b[1][1] (=6) = |-4| + ...
 *

 */
public class EarthMoversDistanceAdj {
	
	// berechnet die totale Distanz zwischen zwei für Cluster repräsentative Deskriptoren anhand der Dimension
	public double computeGrounddistance(double[] desc1, double[] desc2)
		    throws DimensionMismatchException {
		        MathArrays.checkEqualLength(desc1, desc2);
		        double lastDistance = 0;
		        double totalDistance = 0;
		        for (int i = 0; i < desc1.length - 1; i++) {
		            final double currentDistance = (desc1[i] + lastDistance) - desc2[i];
		            totalDistance += FastMath.abs(currentDistance);
		            lastDistance = currentDistance;
		        }
		        return totalDistance;
		    }

// -------------------------------------------------------------------------------------------

    public double[][] computeDistanceLists(double[][] a, double[][] b)
    throws DimensionMismatchException {
    	double[] aPart = new double[a[0].length];
    	double[] bPart = new double[b[0].length];
    	// initialize new double array to get all distances 
    	double[][] allDistances = new double[b.length][a.length];
        for (int i = 0; i < b.length; i++) {
        	for (int j = 0; j < a.length; j++) {
        		aPart = a[j];
        		bPart = b[i];
        		allDistances[i][j] = computeGrounddistance(aPart, bPart);
        	}
        }
        return allDistances;
    }
    
 // -------------------------------------------------------------------------------------------

    public double[][] computeMassLists(double[][] a, double[][] b)
    throws DimensionMismatchException {
    	// initialize new double array to get all masses 
    	double[][] allMasses = new double[b.length][a.length];
        for (int i = 0; i < b.length; i++) {
        	allMasses[i][64] = b[i][64];
        	for (int j = 1; j < a.length; j++) {
        		allMasses[i][j] = a[i][j];
        	}
        }
        return allMasses;
    }

// ------------------------------------------------------------------------------------------

    
    
    
    
    
    
    
    
    
    /*    
    public double compute2(double[] a, double[] b)
    throws DimensionMismatchException {
    	// initialize ArrayList to add all distances
    	ArrayList<Double> allDistances = new ArrayList<Double>();
        MathArrays.checkEqualLength(a, b);
        double totalDistance = 0;
        for (int i = 0; i < a.length; i++) {
        	for (int j = 0; j < b.length; j++) {
        		double currentDistance = FastMath.abs((a[j] - b[j])) * FastMath.abs((i-j));
        		allDistances.add(currentDistance);
        	}

    		totalDistance += findMin(allDistances);
    		allDistances.clear();
        }
        totalDistance = totalDistance / a.length;
        return totalDistance;
    }
*/   
 // ------------------------------------------------------------------------------------------

    
    /**computes a double[] which only contains the chosen attribute in the correct order
     * 
     * Cluster structure: [[x1, y1, R1, G1, B1], [x2, y2, R2, G2, B2], ... [xN, yN, RN, GN, BN]]
     * 
     * If for example the cluster has to be prepared with the Red color values, the value for 
     * 'attribute' has to be 2 because the Red color values are at the 3rd position in every array
     * 
     * Result structure: [R1, R2, ... , RN]
     */
	
/*


	public double[] prepareHolesNHills(double[][] clusterList, int attribute) {
		
		// initalize double[] to the length of all elements in the given list of clusters
		double[] holesORhills = new double[clusterList.length];
		
		for (int e = 0; e < clusterList[0].length ;e++) {
			holesORhills[e] = clusterList[e][attribute];
		}
		return holesORhills;
	}
*/
// ------------------------------------------------------------------------------------------
/*
	
	// function to find minimum value in an unsorted 
    // list in Java using Collection 
    public static Double findMin(ArrayList<Double> list) 
    { 
  
        // check list is empty or not 
        if (list == null || list.size() == 0) { 
            return Double.MAX_VALUE; 
        } 
  
        // create a new list to avoid modification  
        // in the original list 
        ArrayList<Double> sortedlist = new ArrayList<>(list); 
  
        // sort list in natural order 
        Collections.sort(sortedlist); 
  
        // first element in the sorted list 
        // would be minimum 
        return sortedlist.get(0); 
    } 
  
    // function return maximum value in an unsorted 
    // list in Java using Collection 
    public static Double findMax(ArrayList<Double> list) 
    { 
  
        // check list is empty or not 
        if (list == null || list.size() == 0) { 
            return Double.MIN_VALUE; 
        } 
  
        // create a new list to avoid modification 
        // in the original list 
        ArrayList<Double> sortedlist = new ArrayList<>(list); 
  
        // sort list in natural order 
        Collections.sort(sortedlist); 
  
        // last element in the sorted list would be maximum 
        return sortedlist.get(sortedlist.size() - 1); 
    } 
  
*/
	
}
