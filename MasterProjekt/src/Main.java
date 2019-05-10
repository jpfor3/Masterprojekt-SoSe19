
import java.util.Arrays;


import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class Main
{
   public static void main( String[] args )
   {
	   System.load("C:\\opencv\\build\\java\\x64\\opencv_java410.dll");

	   Mat img = Imgcodecs.imread("resources/images/Angelina_Jolie_009.jpg", Imgcodecs.IMREAD_GRAYSCALE);
       
       byte[] imgData = new byte[(int) (img.total() * img.channels())];
       Arrays.fill(imgData, (byte) 0);
       img.put(0, 0, imgData);
       Imgcodecs.imwrite("resources/images/Angelina_Jolie_010.jpg", img);
       System.out.println("test");
   }
}