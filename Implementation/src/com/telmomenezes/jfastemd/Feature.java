package com.telmomenezes.jfastemd;

public interface Feature {
    public double groundDist(Feature f);
    public double hammingDist(Feature f);
	public double getValue();
}