import AKAZE.AKAZEMatchDemo;
import keypointdetector.KeypointDetector;

import org.opencv.core.Core;


public class Main
{
   public static void main( String[] args )
   {
	 
     System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
     
     //Zu vergleichende Bilder
     String image1 =  "resources/images/HansSarpei.jpg";
     String image2 =  "resources/images/Schalketrikot.jpg";
     
      KeypointDetector.SurfDetector(image1, image2);    
      System.out.println("Ended....");


      
   }
   
  
}