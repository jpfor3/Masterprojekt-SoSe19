
package keypointdetector;
import org.opencv.calib3d.Calib3d;





import org.opencv.core.*;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d; 
import org.opencv.highgui.HighGui; 
import org.opencv.highgui.Highgui; 
import org.opencv.imgproc.Imgproc; 
//import org.opencv.xfeatures2d.PCTSignatures;

import cluster.DBScan;
import cluster.KMeans;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class KeypointDetector {

	public static void SurfDetector(String image1, String image2){

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

        String refImage = image1;
        String cmpImage = image2;

        System.out.println("Started....");
        System.out.println("Loading images...");
        Mat referenceImg = Highgui.imread(refImage, Highgui.CV_LOAD_IMAGE_COLOR);
        Mat compareImg = Highgui.imread(cmpImage, Highgui.CV_LOAD_IMAGE_COLOR);



        MatOfKeyPoint refKeyPoints = new MatOfKeyPoint();

        //TODO: Try other Algorithms
        FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.SURF);
        System.out.println("Detecting key points...");
        featureDetector.detect(referenceImg, refKeyPoints);
        KeyPoint[] keypoints = refKeyPoints.toArray();
        System.out.println(keypoints);

        MatOfKeyPoint refDescriptors = new MatOfKeyPoint();
        DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SURF);
        System.out.println("Computing descriptors...");
        descriptorExtractor.compute(referenceImg, refKeyPoints, refDescriptors);

        // Create the matrix for output image.
        Mat outputRefImage = new Mat(referenceImg.rows(), referenceImg.cols(), Highgui.CV_LOAD_IMAGE_COLOR);
        Scalar refKeypointColor = new Scalar(255, 0, 0);

        System.out.println("Drawing key points on reference image...");
        Features2d.drawKeypoints(referenceImg, refKeyPoints, outputRefImage, refKeypointColor, 0);

        // Match reference image with the compare image
        MatOfKeyPoint cmpKeyPoints = new MatOfKeyPoint();
        MatOfKeyPoint cmpDescriptors = new MatOfKeyPoint();
        System.out.println("Detecting key points in compare image...");
        featureDetector.detect(compareImg, cmpKeyPoints);
        System.out.println("Computing descriptors in compare image...");
        descriptorExtractor.compute(compareImg, cmpKeyPoints, cmpDescriptors);

        Mat outputCmpImage = new Mat(compareImg.rows() ,
compareImg.cols() , Highgui.CV_LOAD_IMAGE_COLOR);
        Scalar cmpKeypointColor = new Scalar(0, 255, 0);

        System.out.println("Drawing key points on compare image...");
        Features2d.drawKeypoints(compareImg, cmpKeyPoints, outputCmpImage, cmpKeypointColor, 0);

        Highgui.imwrite("resources/images/outputRefImage.jpg", outputRefImage);
        Highgui.imwrite("resources/images/outputCmpImage.jpg", outputCmpImage);


        //TODO: Signaturen und Distanzmaß, Indizierung


        //DBScan; WEKA; JavaML als mögliche Library
        //Cure als möglicher Algorithmus

        List<MatOfKeyPoint> clusterList = new ArrayList<MatOfKeyPoint>();
        clusterList.add(refKeyPoints);
        clusterList.add(cmpKeyPoints);


        System.out.println("Creating clusters on Keypoints...");
        for(MatOfKeyPoint kp : clusterList)
        {
     	   DBScan.cluster(kp);
        }


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

            Mat homography = Calib3d.findHomography(objMatOfPoint2f,
scnMatOfPoint2f, Calib3d.RANSAC, 3);

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


