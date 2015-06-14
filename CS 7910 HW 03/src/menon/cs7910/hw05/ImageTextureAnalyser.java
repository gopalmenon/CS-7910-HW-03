package menon.cs7910.hw05;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
	public static final String OUTPUT_FILE = "outPut.txt";
	public static final String HORIZONTAL_WAVELET = "H";
	public static final String VERTICAL_WAVELET = "V";
	public static final String DIAGONAL_WAVELET = "D";
	
	private List<Double> appleSauceFilesAverage = null, pieFilesAverage = null, meatFilesAverage = null, riceFilesAverage = null;
	private PrintWriter out;
	private boolean onlyOneWaveLet, onlyHorizontalWaveLet, onlyVerticalWaveLet, onlyDiagonalWaveLet;

	public ImageTextureAnalyser(String waveletType) {
		try {
			this.out = new PrintWriter(new FileWriter(OUTPUT_FILE));
			if (waveletType != null && waveletType.trim().length() > 0) {
				if (HORIZONTAL_WAVELET.equalsIgnoreCase(waveletType.trim())) {
					this.onlyOneWaveLet = true;
					this.onlyHorizontalWaveLet = true;
				} else if (VERTICAL_WAVELET.equalsIgnoreCase(waveletType.trim())) {
					this.onlyOneWaveLet = true;
					this.onlyVerticalWaveLet = true;
				} else if (DIAGONAL_WAVELET.equalsIgnoreCase(waveletType.trim())) {
					this.onlyOneWaveLet = true;
					this.onlyDiagonalWaveLet = true;
				} else {
					this.onlyOneWaveLet = false;
				}
			}
			
			
		} catch (IOException e) {
			System.err.println("IOException while opening file ");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private void trainOnTextureAnalyserData() {
		
		String appleSauceFilesPath = MAIN_FOLDER + FOLDER_SEPARATOR + APPLE_SAUCE_FOLDER + FOLDER_SEPARATOR + TRAIN_FOLDER + FOLDER_SEPARATOR;
		appleSauceFilesAverage = getAverageHaarTransformVector(appleSauceFilesPath);

		String pieFilesPath = MAIN_FOLDER + FOLDER_SEPARATOR + PIE_FOLDER + FOLDER_SEPARATOR + TRAIN_FOLDER + FOLDER_SEPARATOR;
		pieFilesAverage = getAverageHaarTransformVector(pieFilesPath);

		String meatFilesPath = MAIN_FOLDER + FOLDER_SEPARATOR + MEAT_FOLDER + FOLDER_SEPARATOR + TRAIN_FOLDER + FOLDER_SEPARATOR;
		meatFilesAverage = getAverageHaarTransformVector(meatFilesPath);

		String riceFilesPath = MAIN_FOLDER + FOLDER_SEPARATOR + RICE_FOLDER + FOLDER_SEPARATOR + TRAIN_FOLDER + FOLDER_SEPARATOR;
		riceFilesAverage = getAverageHaarTransformVector(riceFilesPath);

	}
	
	
	private void testImageTextures() {
		
		String appleSauceFilesPath = MAIN_FOLDER + FOLDER_SEPARATOR + APPLE_SAUCE_FOLDER + FOLDER_SEPARATOR + TEST_FOLDER + FOLDER_SEPARATOR;
		printSimilarityMeasures(getHaarTransformVectors(appleSauceFilesPath), APPLE_SAUCE_FOLDER);
		
		String pieFilesPath = MAIN_FOLDER + FOLDER_SEPARATOR + PIE_FOLDER + FOLDER_SEPARATOR + TEST_FOLDER + FOLDER_SEPARATOR;
		printSimilarityMeasures(getHaarTransformVectors(pieFilesPath), PIE_FOLDER);
		
		String meatFilesPath = MAIN_FOLDER + FOLDER_SEPARATOR + MEAT_FOLDER + FOLDER_SEPARATOR + TEST_FOLDER + FOLDER_SEPARATOR;
		printSimilarityMeasures(getHaarTransformVectors(meatFilesPath), MEAT_FOLDER);
		
		String riceFilesPath = MAIN_FOLDER + FOLDER_SEPARATOR + RICE_FOLDER + FOLDER_SEPARATOR + TEST_FOLDER + FOLDER_SEPARATOR;
		printSimilarityMeasures(getHaarTransformVectors(riceFilesPath), RICE_FOLDER);
		
		
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
			if (!file.endsWith(".jpg")) {
				continue;
			}
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
		
		if (this.onlyOneWaveLet) {
			if (this.onlyHorizontalWaveLet) {
				
				returnValue.add(Double.valueOf(transform[0][1]));//H0-8
				returnValue.add(Double.valueOf(0));//V0-8
				returnValue.add(Double.valueOf(0));//D0-8
				
				returnValue.add(Double.valueOf(transform[0][2]));//H0-7
				returnValue.add(Double.valueOf(transform[0][3]));//H1-7
				returnValue.add(Double.valueOf(transform[1][2]));//H2-7
				returnValue.add(Double.valueOf(transform[1][3]));//H3-7
								
				returnValue.add(Double.valueOf(0));//V0-7
				returnValue.add(Double.valueOf(0));//V1-7
				returnValue.add(Double.valueOf(0));//V2-7
				returnValue.add(Double.valueOf(0));//V3-7
				
				returnValue.add(Double.valueOf(0));//D0-7
				returnValue.add(Double.valueOf(0));//D1-7
				returnValue.add(Double.valueOf(0));//D2-7
				returnValue.add(Double.valueOf(0));//D3-7

			} else if (this.onlyVerticalWaveLet) {
				
				returnValue.add(Double.valueOf(0));//H0-8
				returnValue.add(Double.valueOf(transform[1][0]));//V0-8
				returnValue.add(Double.valueOf(0));//D0-8

				returnValue.add(Double.valueOf(0));//H0-7
				returnValue.add(Double.valueOf(0));//H1-7
				returnValue.add(Double.valueOf(0));//H2-7
				returnValue.add(Double.valueOf(0));//H3-7

				returnValue.add(Double.valueOf(transform[2][0]));//V0-7
				returnValue.add(Double.valueOf(transform[2][1]));//V1-7
				returnValue.add(Double.valueOf(transform[3][0]));//V2-7
				returnValue.add(Double.valueOf(transform[3][1]));//V3-7
				
				returnValue.add(Double.valueOf(0));//D0-7
				returnValue.add(Double.valueOf(0));//D1-7
				returnValue.add(Double.valueOf(0));//D2-7
				returnValue.add(Double.valueOf(0));//D3-7
				
			} else {
				
				returnValue.add(Double.valueOf(0));//H0-8
				returnValue.add(Double.valueOf(0));//V0-8
				returnValue.add(Double.valueOf(transform[1][1]));//D0-8

				returnValue.add(Double.valueOf(0));//H0-7
				returnValue.add(Double.valueOf(0));//H1-7
				returnValue.add(Double.valueOf(0));//H2-7
				returnValue.add(Double.valueOf(0));//H3-7
				
				returnValue.add(Double.valueOf(0));//V0-7
				returnValue.add(Double.valueOf(0));//V1-7
				returnValue.add(Double.valueOf(0));//V2-7
				returnValue.add(Double.valueOf(0));//V3-7
				
				returnValue.add(Double.valueOf(transform[2][2]));//D0-7
				returnValue.add(Double.valueOf(transform[2][3]));//D1-7
				returnValue.add(Double.valueOf(transform[3][2]));//D2-7
				returnValue.add(Double.valueOf(transform[3][3]));//D3-7

			}
		} else {
			
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
			
		}
		
		return returnValue;
		
	}
	
	private void printSimilarityMeasures(List<List<Double>> haarTransformVectors, String fileType) {
		
		this.out.println("\n\n#Similarity measures for "+ fileType + getTextIfOnlyOneType());
		
		printSimilarityMeasuresAgainst(haarTransformVectors, fileType, "appleSauceFiles", this.appleSauceFilesAverage);
		printSimilarityMeasuresAgainst(haarTransformVectors, fileType, "pieFiles", this.pieFilesAverage);
		printSimilarityMeasuresAgainst(haarTransformVectors, fileType, "meatFiles", this.meatFilesAverage);
		printSimilarityMeasuresAgainst(haarTransformVectors, fileType, "riceFiles", this.riceFilesAverage);

	}
	
	private void printSimilarityMeasuresAgainst(List<List<Double>> haarTransformVectors, String fileType, String against, List<Double> againstVector) {
		
		this.out.print("\n" + fileType + ".with." + against + "=c(");
		double dotProduct = 0.0;
		boolean firstTime = true;
		
		for (List<Double> vector : haarTransformVectors) {
			
			try {
				dotProduct = getDotProduct(vector, againstVector);
				if (firstTime) {
					firstTime = false;
				} else {
					this.out.print(", ");
				}
				this.out.print(dotProduct);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}

		}
		
		this.out.print(")");
	}
	
	private String getTextIfOnlyOneType() {
		
		if (this.onlyOneWaveLet) {
			if (this.onlyHorizontalWaveLet) {
				return " with only Horizontal wavelets";
			} else if (this.onlyVerticalWaveLet) {
				return " with only Vertical wavelets";
			} else {
				return " with only Diagonal wavelets";
			}
			
		} else {
			return "";
		}
		
	}
	
	private int getExponent(int arraySize) {
		
		return Double.valueOf(Math.log(arraySize)/Math.log(2)).intValue();
		
	}
	
	private double getDotProduct(List<Double> vector1, List<Double> vector2) throws Exception {
		
		double returnValue = 0.0;
		
		if (vector1.size() != vector2.size()) {
			throw new Exception("Dot product can only be done for vectors of equal size.");
		}
		
		int index = 0;
		for (Double coefficient : vector1) {
			
			returnValue += coefficient.doubleValue() * vector2.get(index++).doubleValue();
			
		}

		return returnValue;
		
	}
	
	
	public static final void main(String[] args) {
		
		ImageTextureAnalyser imageTextureAnalyser = null;
		if (args.length > 0 && args[0].trim().length() > 0) {
			imageTextureAnalyser = new ImageTextureAnalyser(args[0].trim());
		} else {
			imageTextureAnalyser = new ImageTextureAnalyser("");
		}
		imageTextureAnalyser.trainOnTextureAnalyserData();
		imageTextureAnalyser.testImageTextures();
		imageTextureAnalyser.out.close();
	}

}
