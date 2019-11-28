package keypointdetector;
import org.opencv.calib3d.Calib3d;





import org.opencv.core.*;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.HighGui;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.xfeatures2d.PCTSignatures;

import cluster.DBScan;
import cluster.KMeans;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



public class KeypointDetector {

    public static List<MatOfKeyPoint> _descriptorList = new ArrayList<MatOfKeyPoint>();

	public static void SurfDetector(List<String> images){
	 
	   File lib = null;
       String os = System.getProperty("os.name");
       String bitness = System.getProperty("sun.arch.data.model");

       if (os.toUpperCase().contains("WINDOWS")) {
           if (bitness.endsWith("64")) {
               lib = new File("libs//x64//" + System.mapLibraryName("opencv_java2411"));
           } else {
               lib = new File("libs//x86//" + System.mapLibraryName("opencv_java2411"));
           }
       }

       System.out.println(lib.getAbsolutePath());
       System.load(lib.getAbsolutePath());


       System.out.println("Started....");
       System.out.println("Loading images...");
       
       int i = 0;
       
       for(String image : images)
       {

       Mat img = Highgui.imread(image, Highgui.CV_LOAD_IMAGE_COLOR);    
       MatOfKeyPoint kp = new MatOfKeyPoint();
       
       //TODO: Try other Algorithms
       FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.SURF);
       System.out.println("Detecting key points...");
       featureDetector.detect(img, kp);
       KeyPoint[] keypoints = kp.toArray();
       System.out.println(keypoints);

       MatOfKeyPoint descriptors = new MatOfKeyPoint();
       DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SURF);
       System.out.println("Computing descriptors...");
       descriptorExtractor.compute(img, kp, descriptors);

       // Create the matrix for output image.
       Mat outputImage = new Mat(img.rows(), img.cols(), Highgui.CV_LOAD_IMAGE_COLOR);
       Scalar KeypointColor = new Scalar(255, 0, 0);

       System.out.println("Drawing key points on reference image...");
       Features2d.drawKeypoints(img, kp, outputImage, KeypointColor, 0);

       
       Highgui.imwrite("resources/output_images/image(" + i + ").jpg", outputImage);

       _descriptorList.add(descriptors);
       
       i +=1;
       }
       
       System.out.println("KP Detection Ended....");

       //TODO: Signaturen und Distanzmaß, Indizierung
       
       
       //DBScan; WEKA; JavaML als mögliche Library
       //Cure als möglicher Algorithmus
       

       

      
       

 	  
       /**List<Mat> clusterList = new ArrayList<Mat>();
       Mat clusterinput1 = (Mat) refDescriptors;
       Mat clusterinput2 = (Mat) cmpDescriptors;
       clusterList.add(clusterinput1);
       clusterList.add(clusterinput2);


       System.out.println("Creating clusters on Keypoints...");
       for(Mat mat : clusterList)
       {
    	   KMeans.cluster(mat, 3);
       }
       */
       
       
       
       /**List<Mat> clusterList2 = new ArrayList<Mat>();
       clusterList2.add(referenceImg);
       clusterList2.add(compareImg);

       PCTSignatures signature = PCTSignatures.create();
       signature.computeSignatures(clusterList2, new ArrayList<Mat>(2));
       
       System.out.println("Drawing clusters on compare image...");
       for(Mat mat : clusterList2)
       {

    	   PCTSignatures.drawSignature(mat, new Mat(), new Mat());
       }
       */
       
       
       /**
       List<MatOfDMatch> matches = new LinkedList<MatOfDMatch>();
       DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);
       System.out.println("Matching ref and cmp images...");
       descriptorMatcher.knnMatch(refDescriptors, cmpDescriptors, matches, 2);

       System.out.println("Calculating good match list...");
       LinkedList<DMatch> goodMatchesList = new LinkedList<DMatch>();

       float nndrRatio = 0.7f;

       for (int i = 0; i < matches.size(); i++) {
           MatOfDMatch matofDMatch = matches.get(i);
           DMatch[] dmatcharray = matofDMatch.toArray();
           DMatch m1 = dmatcharray[0];
           DMatch m2 = dmatcharray[1];

           if (m1.distance <= m2.distance * nndrRatio) {
               goodMatchesList.addLast(m1);

           }
       }

       if (goodMatchesList.size() >= 7) {
           System.out.println("Object Found!!!");

           List<KeyPoint> objKeypointlist = refKeyPoints.toList();
           List<KeyPoint> scnKeypointlist = cmpKeyPoints.toList();

           LinkedList<Point> objectPoints = new LinkedList<>();
           LinkedList<Point> scenePoints = new LinkedList<>();

           for (int i = 0; i < goodMatchesList.size(); i++) {
               objectPoints.addLast(objKeypointlist.get(goodMatchesList.get(i).queryIdx).pt);
               scenePoints.addLast(scnKeypointlist.get(goodMatchesList.get(i).trainIdx).pt);
           }

           MatOfPoint2f objMatOfPoint2f = new MatOfPoint2f();
           objMatOfPoint2f.fromList(objectPoints);
           MatOfPoint2f scnMatOfPoint2f = new MatOfPoint2f();
           scnMatOfPoint2f.fromList(scenePoints);

           Mat homography = Calib3d.findHomography(objMatOfPoint2f, scnMatOfPoint2f, Calib3d.RANSAC, 3);

           Mat obj_corners = new Mat(4, 1, CvType.CV_32FC2);
           Mat scene_corners = new Mat(4, 1, CvType.CV_32FC2);

           obj_corners.put(0, 0, new double[]{0, 0});
           obj_corners.put(1, 0, new double[]{referenceImg.cols(), 0});
           obj_corners.put(2, 0, new double[]{referenceImg.cols(), referenceImg.rows()});
           obj_corners.put(3, 0, new double[]{0, referenceImg.rows()});

           System.out.println("Transforming object corners to scene corners...");
           Core.perspectiveTransform(obj_corners, scene_corners, homography);

           Mat img = Highgui.imread(cmpImage, Highgui.CV_LOAD_IMAGE_COLOR);

          
           System.out.println("Drawing matches image...");
           MatOfDMatch goodMatches = new MatOfDMatch();
           goodMatches.fromList(goodMatchesList);

           Features2d.drawMatches(referenceImg, refKeyPoints, compareImg, cmpKeyPoints, goodMatches, matchoutput, matchestColor, newKeypointColor, new MatOfByte(), 2);

           Highgui.imwrite("resources/images/outputImage2.jpg", outputImage);
           Highgui.imwrite("resources/images/matchoutput.jpg", matchoutput);
           Highgui.imwrite("resources/images/img.jpg", img);
       } else {
           System.out.println("Object Not Found");
       }
       */



   }
	

}
