import org.opencv.core.Core;

import xfeatures2d_experiment.versuch;

public class Versuch_Main {

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		versuch v1 = new versuch();
		
		v1.compSig();
		v1.getSig();

	}

}
