package menon.cs7910.hw05;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class ImageProcessor {

	static { 
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	
	public static Mat getGrayscaleImageMatrix(String fileName) {
		
		Mat originalImage = Highgui.imread(fileName);
		Mat grayscaleImage = new Mat(originalImage.rows(), originalImage.cols(), CvType.CV_8UC1);
        Imgproc.cvtColor(originalImage, grayscaleImage, Imgproc.COLOR_RGB2GRAY);
        originalImage.release();
		
		return grayscaleImage;
	} 
	
	
}
