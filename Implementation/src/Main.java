import AKAZE.AKAZEMatchDemo;
import keypointdetector.*;
import cluster.*;
import Distanzmasse.*;

import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;


public class Main
{
   public static void main( String[] args )
   {
	 
     System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
     
     //Zu vergleichende Bilder
     String image1 =  "resources/images/HansSarpei.jpg";
     String image2 =  "resources/images/Schalketrikot.jpg";
     
     //KeypointDetector.SurfDetector(image1, image2);    
     
     MatOfKeyPoint desc1 = new MatOfKeyPoint();
     desc1 = KeypointDetector.extractDescriptors(image1, desc1);
     System.out.println(desc1);
     DBScan.cluster(desc1);
     //System.out.println(sig1);
     
   /*   
     // EMD Test     
     MatOfKeyPoint desc1 = KeypointDetector.extractDescriptors(image1, desc1);
     MatOfKeyPoint desc2 = KeypointDetector.extractDescriptors(image1, desc2);

     double[] sig1 = KMeans.cluster(desc1,100);
     double[] sig2 = KMeans.cluster(desc2,100);
     
     
     EarthMoversDistance EMD = new EarthMoversDistance();
     EMD.compute(sig1, sig2);
     
      
      
      System.out.println("Ended....");
      
   */

      
   }
   
  
}