import java.awt.geom.Point2D;

import org.apache.commons.math3.ml.distance.EarthMoversDistance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.math3.stat.clustering.*;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.KeyPoint;

public class DBScan {

        static double[] _centroid = new double[64];

        /**
         *
         * @param descriptor MatOfKeyPoint jede Zeile muss einzeln augewertet  
werden bei Clustering
         */
        public static void cluster(MatOfKeyPoint descriptor)
        {

        Collection<EuclideanDoublePoint> matOfKeypoints = new  
        ArrayList<EuclideanDoublePoint>();
        Collection<KeyPoint> colkp = descriptor.toList();

        for(int row = 0; row < descriptor.rows(); row++)
        {
                double[] param = new double[64];
                for(int col = 0; col < descriptor.row(row).cols(); col++)
                {
                        param[col] = descriptor.get(row, col)[0];
                }

                EuclideanDoublePoint edp = new EuclideanDoublePoint(param);
                matOfKeypoints.add(edp);
        }
                DBSCANClusterer<EuclideanDoublePoint> cls = new  
                DBSCANClusterer<EuclideanDoublePoint>(0.1, 4);
            List<Cluster<EuclideanDoublePoint>> list = cls.cluster(matOfKeypoints);
            System.out.println("\nListe: " );
            for(int count = 0; count < list.size(); count++)
            {
            System.out.println(list.get(count).getPoints() + "Nr: " + list.get(count).getPoints().size() + "\n");
            }

            center(list);
        }


     //Center berechnen
        private static void center(List<Cluster<EuclideanDoublePoint>> list)
        {
                for(int count = 0; count < list.size(); count++)
            {
                        double[] sumOfDescriptor = new double[64];

                        for(int count2 = 0; count2 < list.get(count).getPoints().size(); count2++)
                  {
                                //Aufsummierung der einzelnen Keypoints im Cluster
                          EuclideanDoublePoint edp = list.get(count).getPoints().get(count2);
                          double[] newcenter =  edp.getPoint();

                          for(int i = 0; i < 64; i++)
                          {
                          sumOfDescriptor[i] += newcenter[i];
                          }
                  }

                        //Berechnung des Mittelwerts im Cluster
                        for(int i = 0; i < 64; i++)
                 {
                        _centroid[i] = sumOfDescriptor[i] / list.get(count).getPoints().size();
                 }
                        //System.out.println("Centroid("+count+"): "+ _centroid [1]);
                        //addCentroidstoArray(_centroid);
                }


        }

        /**private static double[][] addCentroidstoArray(double[] centroid)
        {
                ArrayList<double[]> centroidlist = new ArrayList<double[]>();
                centroidlist.add(centroid);

                double[][] newcentroidlist = (double[][]) centroidlist.toArray();

                return newcentroidlist;
        }
        */







        //TODO: Distanzmaﬂe berechnen
}
