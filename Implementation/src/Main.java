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
     
     KeypointDetector.SurfDetector(image1, image2);    
   }
   
  
}