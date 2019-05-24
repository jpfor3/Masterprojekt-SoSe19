import AKAZE.AKAZEMatch;
import AKAZE.AKAZEMatchDemo;
import SURF.KeypointDetector;

import org.opencv.core.Core;


public class Main
{
   public static void main( String[] args )
   {
	 
     System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
     
     //Zu vergleichende Bilder
     String image1 =  "resources/images/HansSarpei.jpg";
     String image2 =  "resources/images/Hanssarpei.jpg";
     
      KeypointDetector.SurfDetector(image1, image2);    
      //AKAZEMatch Akaze = new AKAZEMatch();
      //Akaze.run(image1, image2);

      
   }
   
  
}