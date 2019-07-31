package xfeatures2d_experiment;

import org.opencv.xfeatures2d.*;


public class versuch {
	
	private PCTSignatures pctsig;
	
	public void compSig() {
		
		pctsig = PCTSignatures.create();
		
	}
	
	public PCTSignatures getSig() {
		
		System.out.println(pctsig.toString());
		
		return pctsig;
		
	}

}
