import AKAZE.AKAZEMatchDemo;
import keypointdetector.*;
import cluster.*;
import Distanzmasse.*;

import org.opencv.core.Core;
import org.opencv.core.MatOfKeyPoint;


public class Main
{
   public static void main( String[] args )
   {
	 
     System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
     
     //Zu vergleichende Bilder
     String image1 =  "resources/images/HansSarpei.jpg";
     String image2 =  "resources/images/Schalketrikot.jpg";
     
     KeypointDetector.SurfDetector(image1, image2);    
      
      
     // EMD Test
     MatOfKeyPoint desc1 = new MatOfKeyPoint();
     MatOfKeyPoint desc2 = new MatOfKeyPoint();
     
     desc1 = KeypointDetector.extractDescriptors(image1, desc1);
     desc2 = KeypointDetector.extractDescriptors(image1, desc2);

     /*
     // Type Mismatch
     double[] sig1 = DBScan.cluster(desc1);
     double[] sig2 = DBScan.cluster(desc2);
     */
     
     EarthMoversDistance EMD = new EarthMoversDistance();
     EMD.compute(sig1, sig2);
     
      
      
      System.out.println("Ended....");
      


      
   }
   
  
}