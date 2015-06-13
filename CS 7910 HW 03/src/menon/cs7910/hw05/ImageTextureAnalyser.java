package menon.cs7910.hw05;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import menon.cs7910.hw04.TwoDHaar;

import org.opencv.core.Mat;

import edu.usu.csatl.OneDHaarEdgeDetection;

public class ImageTextureAnalyser {
	
	public static final String MAIN_FOLDER = "food_textures";
	public static final String APPLE_SAUCE_FOLDER = "appleSauce";
	public static final String PIE_FOLDER = "pie";
	public static final String MEAT_FOLDER = "meat";
	public static final String RICE_FOLDER = "rice";
	public static final String TEST_FOLDER = "test";
	public static final String TRAIN_FOLDER = "train";
	public static final String FOLDER_SEPARATOR = "/";

	private void trainOnTextureAnalyserData() {
		
		String appleSauceFilesPath = MAIN_FOLDER + FOLDER_SEPARATOR + APPLE_SAUCE_FOLDER + FOLDER_SEPARATOR + TRAIN_FOLDER + FOLDER_SEPARATOR;
		List<Double> appleSauceFilesAverage = getAverageHaarTransformVector(appleSauceFilesPath);

		String pieFilesPath = MAIN_FOLDER + FOLDER_SEPARATOR + PIE_FOLDER + FOLDER_SEPARATOR + TRAIN_FOLDER + FOLDER_SEPARATOR;
		List<Double> pieFilesAverage = getAverageHaarTransformVector(pieFilesPath);

		String meatFilesPath = MAIN_FOLDER + FOLDER_SEPARATOR + MEAT_FOLDER + FOLDER_SEPARATOR + TRAIN_FOLDER + FOLDER_SEPARATOR;
		List<Double> meatFilesAverage = getAverageHaarTransformVector(meatFilesPath);

		String riceFilesPath = MAIN_FOLDER + FOLDER_SEPARATOR + RICE_FOLDER + FOLDER_SEPARATOR + TRAIN_FOLDER + FOLDER_SEPARATOR;
		List<Double> riceFilesAverage = getAverageHaarTransformVector(riceFilesPath);

	}
	
	private List<Double> getAverageHaarTransformVector(String path) {
		
		List<Double> returnValue = new ArrayList<Double>();
		
		double overallHorizontalWaveLet = 0.0, overallVerticalWaveLet = 0.0, overallDiagonalWaveLet = 0.0;
		double horizontalWaveLet0 = 0.0, horizontalWaveLet1 = 0.0, horizontalWaveLet2 = 0.0, horizontalWaveLet3 = 0.0;
		double verticalWaveLet0 = 0.0, verticalWaveLet1 = 0.0, verticalWaveLet2 = 0.0, verticalWaveLet3 = 0.0;
		double diagonalWaveLet0 = 0.0, diagonalWaveLet1 = 0.0, diagonalWaveLet2 = 0.0, diagonalWaveLet3 = 0.0;
		
		List<List<Double>> haarTransformVectors = getHaarTransformVectors(path);
		
		for (List<Double> vector : haarTransformVectors) {
		
			overallHorizontalWaveLet += vector.get(0).doubleValue();
			overallVerticalWaveLet += vector.get(1).doubleValue();
			overallDiagonalWaveLet += vector.get(2).doubleValue();

			horizontalWaveLet0 += vector.get(3).doubleValue();
			horizontalWaveLet1 += vector.get(4).doubleValue();
			horizontalWaveLet2 += vector.get(5).doubleValue();
			horizontalWaveLet3 += vector.get(6).doubleValue();
			
			verticalWaveLet0 += vector.get(7).doubleValue();
			verticalWaveLet1 += vector.get(8).doubleValue();
			verticalWaveLet2 += vector.get(9).doubleValue();
			verticalWaveLet3 += vector.get(10).doubleValue();
			
			diagonalWaveLet0 += vector.get(11).doubleValue();
			diagonalWaveLet1 += vector.get(12).doubleValue();
			diagonalWaveLet2 += vector.get(13).doubleValue();
			diagonalWaveLet3 += vector.get(14).doubleValue();

		}
		
		returnValue.add(Double.valueOf(overallHorizontalWaveLet));
		returnValue.add(Double.valueOf(overallVerticalWaveLet));
		returnValue.add(Double.valueOf(overallDiagonalWaveLet));
		
		returnValue.add(Double.valueOf(horizontalWaveLet0));
		returnValue.add(Double.valueOf(horizontalWaveLet1));
		returnValue.add(Double.valueOf(horizontalWaveLet2));
		returnValue.add(Double.valueOf(horizontalWaveLet3));
		
		returnValue.add(Double.valueOf(verticalWaveLet0));
		returnValue.add(Double.valueOf(verticalWaveLet1));
		returnValue.add(Double.valueOf(verticalWaveLet2));
		returnValue.add(Double.valueOf(verticalWaveLet3));
		
		returnValue.add(Double.valueOf(diagonalWaveLet0));
		returnValue.add(Double.valueOf(diagonalWaveLet1));
		returnValue.add(Double.valueOf(diagonalWaveLet2));
		returnValue.add(Double.valueOf(diagonalWaveLet3));

		return returnValue;
		
	}
	
	private List<List<Double>> getHaarTransformVectors(String path) {
		
		List<List<Double>> returnValue = new ArrayList<List<Double>>();
		
		Collection<String> appleSauceFiles = FileReader.getFileNames(path);
		Mat imageMatrix = null;
		int imageRows = 0, imageColumns = 0;
		double[][] imagePixels = null;
		for (String file : appleSauceFiles) {
			imageMatrix = ImageProcessor.getGrayscaleImageMatrix(path + file);
			imageRows = imageMatrix.rows();
			imageColumns = imageMatrix.cols();
			imagePixels = new double[imageRows][imageColumns];
			for (int rowIndex = 0; rowIndex < imageRows; ++rowIndex) {
				for (int columnIndex = 0; columnIndex < imageColumns; ++columnIndex) {
					imagePixels[rowIndex][columnIndex] = imageMatrix.get(rowIndex, columnIndex)[0];
				}
			}
			
			returnValue.add(getHaarTransformVector(imagePixels));
			
		}
		
		return returnValue;
		
	}
 	
	private List<Double> getHaarTransformVector(double[][] imageMatrix) {
		
		int arrayExponent = getExponent(imageMatrix.length);
		double[][] transform = null;
		try {
			TwoDHaar.orderedFastHaarWaveletTransformForNumSweeps(imageMatrix, arrayExponent, arrayExponent);
			transform = TwoDHaar.getOrderedFastHaarWaveletTransform();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		List<Double> returnValue = new ArrayList<Double>();
		
		returnValue.add(Double.valueOf(transform[0][1]));//H0-8
		returnValue.add(Double.valueOf(transform[1][0]));//V0-8
		returnValue.add(Double.valueOf(transform[1][1]));//D0-8
		
		returnValue.add(Double.valueOf(transform[0][2]));//H0-7
		returnValue.add(Double.valueOf(transform[0][3]));//H1-7
		returnValue.add(Double.valueOf(transform[1][2]));//H2-7
		returnValue.add(Double.valueOf(transform[1][3]));//H3-7
		
		returnValue.add(Double.valueOf(transform[2][0]));//V0-7
		returnValue.add(Double.valueOf(transform[2][1]));//V1-7
		returnValue.add(Double.valueOf(transform[3][0]));//V2-7
		returnValue.add(Double.valueOf(transform[3][1]));//V3-7
		
		returnValue.add(Double.valueOf(transform[2][2]));//D0-7
		returnValue.add(Double.valueOf(transform[2][3]));//D1-7
		returnValue.add(Double.valueOf(transform[3][2]));//D2-7
		returnValue.add(Double.valueOf(transform[3][3]));//D3-7
		
		return returnValue;
		
	}
	
	private void testImageTextures() {
		
	}
	
	private int getExponent(int arraySize) {
		
		return Double.valueOf(Math.log(arraySize)/Math.log(2)).intValue();
		
	}
	
	
	public static final void main(String[] args) {
		
		ImageTextureAnalyser imageTextureAnalyser = new ImageTextureAnalyser();
		imageTextureAnalyser.trainOnTextureAnalyserData();
		imageTextureAnalyser.testImageTextures();
		
	}

}
