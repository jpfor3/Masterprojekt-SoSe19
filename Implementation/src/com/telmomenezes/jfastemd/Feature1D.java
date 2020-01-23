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
    
    public double hammingDist(Feature f) {
		Feature1D fG = (Feature1D)f;
		if(x == fG.x)
		{
			return 0;
		} 
		else 
		{
			return 1;
		}
	}
	
	public double getValue()
	{
		return this.x;
	}
}