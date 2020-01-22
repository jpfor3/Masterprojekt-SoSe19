package com.telmomenezes.jfastemd;

/**
 * @author Telmo Menezes (telmo@telmomenezes.com)
 *
 */
public class Feature1D implements Feature {
    private double x;

    public Feature1D(double x) {
        this.x = x;
        
    }
    
    public double groundDist(Feature f) {
        Feature1D f2d = (Feature1D)f;
        double deltaX = x - f2d.x;
       
        return Math.sqrt((deltaX * deltaX));
    }
}