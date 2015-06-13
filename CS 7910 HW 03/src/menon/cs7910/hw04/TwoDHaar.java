package menon.cs7910.hw04;

import java.util.Arrays;

public class TwoDHaar {
	
	private static double[][] ordered2DFastHaarWaveletTransform;
	private static boolean ordered2DTransformComplete;
	
	private static double[][] orderedInverse2DFastHaarWaveletTransform;
	private static boolean orderedInverse2DTransformComplete;

	/**
	 * Initialize class variables
	 */	
	static {
		ordered2DTransformComplete = false;
		orderedInverse2DTransformComplete = false;
	}


	/**
	 * Generate the ordered 2D Haar wavelet transform and put it in an array
	 * @param sample
	 * @param samepleSizeExponent
	 * @param numberOfSweepsForward
	 * @throws Exception 
	 */
	public static void orderedFastHaarWaveletTransformForNumSweeps(double[][] sample, int sampleSizeExponent, int numberOfSweepsForward) throws Exception {
		
		ordered2DTransformComplete = false;
		
		String testMessage = checkForValidParameters(sample.length, sample[0].length, sampleSizeExponent, numberOfSweepsForward);
		if (testMessage.trim().length() != 0) {
			throw new Exception(testMessage);
		}
		
		ordered2DFastHaarWaveletTransform = Arrays.copyOf(sample, sample.length);
		
		for (int sweepCounter = 0; sweepCounter < numberOfSweepsForward; ++sweepCounter) {
			
			int numberOfColumns = sample.length / Double.valueOf(Math.pow(2, sweepCounter)).intValue();
			int numberOfCells = numberOfColumns * numberOfColumns / 4;
			
			double[][] individualTransforms = new double[numberOfColumns][numberOfColumns];
			
			//Do the transforms for each cell of 4 values
			for (int cellCounter = 0; cellCounter < numberOfCells; ++cellCounter) {
			
				double[] currentTransform = do2DTransform(sampleSizeExponent, sweepCounter, cellCounter);
				int topLeftRow = ((cellCounter * 2) / individualTransforms.length) * 2;
				int topLeftColumn = (cellCounter * 2) % individualTransforms.length;
				
				individualTransforms[topLeftRow][topLeftColumn] = currentTransform[0];
				individualTransforms[topLeftRow][topLeftColumn + 1] = currentTransform[1];
				individualTransforms[topLeftRow + 1][topLeftColumn] = currentTransform[2];
				individualTransforms[topLeftRow + 1][topLeftColumn + 1] = currentTransform[3];
			
			}
			
			//Put the transforms back into the array to be returned
			for (int rowCounter = 0; rowCounter < numberOfColumns; ++rowCounter) {
				for (int columnCounter = 0; columnCounter < numberOfColumns; ++columnCounter) {
					ordered2DFastHaarWaveletTransform[rowCounter][columnCounter] = individualTransforms[rowCounter][columnCounter];
				}
			}
			
			if (sweepCounter < numberOfSweepsForward - 1) {
				double[][] averages = getAverages(sampleSizeExponent, sweepCounter);
				double[][] horizontalWavelets = gethorizontalWavelets(sampleSizeExponent, sweepCounter);
				double[][] verticalWavelets = getverticalWavelets(sampleSizeExponent, sweepCounter);
				double[][] diagonalWavelets = getdiagonalWavelets(sampleSizeExponent, sweepCounter);
				
				reaArrangeTransformArray(averages, horizontalWavelets, verticalWavelets, diagonalWavelets);
			}

		}
		
		ordered2DTransformComplete = true;
		
	}
	
	/**
	 * Do the 2D FHWT for the cell consisting of 4 elements
	 * @param cellCounter
	 */
	private static double[] do2DTransform(int sampleSizeExponent, int sweepCounter, int cellCounter) {
		
		int numberOfCellsInARow = Double.valueOf(Math.pow(2, sampleSizeExponent - (sweepCounter + 1))).intValue();
		int topLeftElementRow = (cellCounter / numberOfCellsInARow) * 2;
		int topLeftElementColumn = (cellCounter % numberOfCellsInARow) * 2;
		
		//Compute average and wavelet components
		double averageValue = (ordered2DFastHaarWaveletTransform[topLeftElementRow][topLeftElementColumn]     + ordered2DFastHaarWaveletTransform[topLeftElementRow][topLeftElementColumn + 1] + 
							   ordered2DFastHaarWaveletTransform[topLeftElementRow + 1][topLeftElementColumn] + ordered2DFastHaarWaveletTransform[topLeftElementRow + 1][topLeftElementColumn + 1]) /4;
		
		double horizontalWaveletValue = ((ordered2DFastHaarWaveletTransform[topLeftElementRow][topLeftElementColumn]     + ordered2DFastHaarWaveletTransform[topLeftElementRow + 1][topLeftElementColumn]) -
										 (ordered2DFastHaarWaveletTransform[topLeftElementRow][topLeftElementColumn + 1] + ordered2DFastHaarWaveletTransform[topLeftElementRow + 1][topLeftElementColumn + 1])) / 4;
		
		double verticalWaveletValue = ((ordered2DFastHaarWaveletTransform[topLeftElementRow][topLeftElementColumn]     + ordered2DFastHaarWaveletTransform[topLeftElementRow][topLeftElementColumn + 1]) -
				                       (ordered2DFastHaarWaveletTransform[topLeftElementRow + 1][topLeftElementColumn] + ordered2DFastHaarWaveletTransform[topLeftElementRow + 1][topLeftElementColumn + 1])) / 4;
		
		double diagonalWaveletValue = ((ordered2DFastHaarWaveletTransform[topLeftElementRow][topLeftElementColumn]     + ordered2DFastHaarWaveletTransform[topLeftElementRow + 1][topLeftElementColumn + 1]) -
									   (ordered2DFastHaarWaveletTransform[topLeftElementRow][topLeftElementColumn + 1] + ordered2DFastHaarWaveletTransform[topLeftElementRow + 1][topLeftElementColumn])) / 4;
		
		//Replace original values with the computed values
		double[] returnValue = new double[4];
		returnValue[0] = averageValue;
		returnValue[1] = horizontalWaveletValue;
		returnValue[2] = verticalWaveletValue;
		returnValue[3] = diagonalWaveletValue;
		
		return returnValue;
		
	}
	
	/**
	 * @param sweepCounter
	 * @return an array containing just the average components
	 */
	private static double[][] getAverages(int sampleSizeExponent, int sweepCounter) {
		
		int returnArraySize = Double.valueOf(Math.pow(2, sampleSizeExponent - (sweepCounter + 1))).intValue();
		double[][] returnValue = new double[returnArraySize][returnArraySize];
		
		for (int rowIndex = 0; rowIndex < returnArraySize; ++rowIndex) {
			for (int columnIndex = 0; columnIndex < returnArraySize; ++columnIndex) {
		
				returnValue[rowIndex][columnIndex] = ordered2DFastHaarWaveletTransform[rowIndex * 2][columnIndex * 2];
	
			}
		}
		
		return returnValue;

	}
	
	/**
	 * @param sweepCounter
	 * @return an array containing just the horizontal wavelet components
	 */
	private static double[][] gethorizontalWavelets(int sampleSizeExponent, int sweepCounter) {
		
		int returnArraySize = Double.valueOf(Math.pow(2, sampleSizeExponent - (sweepCounter + 1))).intValue();
		double[][] returnValue = new double[returnArraySize][returnArraySize];
		
		for (int rowIndex = 0; rowIndex < returnArraySize; ++rowIndex) {
			for (int columnIndex = 0; columnIndex < returnArraySize; ++columnIndex) {
		
				returnValue[rowIndex][columnIndex] = ordered2DFastHaarWaveletTransform[rowIndex * 2][columnIndex * 2 + 1];
	
			}
		}
		
		return returnValue;

	}
	
	/**
	 * @param sweepCounter
	 * @return an array containing just the vertical wavelet components
	 */
	private static double[][] getverticalWavelets(int sampleSizeExponent, int sweepCounter) {
		
		int returnArraySize = Double.valueOf(Math.pow(2, sampleSizeExponent - (sweepCounter + 1))).intValue();
		double[][] returnValue = new double[returnArraySize][returnArraySize];
		
		for (int rowIndex = 0; rowIndex < returnArraySize; ++rowIndex) {
			for (int columnIndex = 0; columnIndex < returnArraySize; ++columnIndex) {
		
				returnValue[rowIndex][columnIndex] = ordered2DFastHaarWaveletTransform[rowIndex * 2 + 1][columnIndex * 2];
	
			}
		}
		
		return returnValue;

		
	}	
	
	/**
	 * @param sweepCounter
	 * @return an array containing just the diagonal wavelet components
	 */
	private static double[][] getdiagonalWavelets(int sampleSizeExponent, int sweepCounter) {
		
		int returnArraySize = Double.valueOf(Math.pow(2, sampleSizeExponent - (sweepCounter + 1))).intValue();
		double[][] returnValue = new double[returnArraySize][returnArraySize];
		
		for (int rowIndex = 0; rowIndex < returnArraySize; ++rowIndex) {
			for (int columnIndex = 0; columnIndex < returnArraySize; ++columnIndex) {
		
				returnValue[rowIndex][columnIndex] = ordered2DFastHaarWaveletTransform[rowIndex * 2 + 1][columnIndex * 2 + 1];
	
			}
		}
		
		return returnValue;

	}
	
	/**
	 * Rearrange the transform array to form an in order transform
	 * @param averages
	 * @param horizontalWavelets
	 * @param verticalWavelets
	 * @param diagonalWavelets
	 */
	private static void reaArrangeTransformArray(double[][] averages, double[][] horizontalWavelets, double[][] verticalWavelets, double[][] diagonalWavelets) {
		
		int numberOfValues = averages.length;
		for (int rowIndex = 0; rowIndex < numberOfValues; ++rowIndex) {
			for (int columnIndex = 0; columnIndex < numberOfValues; ++columnIndex) {
				
				ordered2DFastHaarWaveletTransform[rowIndex][columnIndex] = averages[rowIndex][columnIndex];
				ordered2DFastHaarWaveletTransform[rowIndex][columnIndex + numberOfValues] = horizontalWavelets[rowIndex][columnIndex];
				ordered2DFastHaarWaveletTransform[rowIndex + numberOfValues][columnIndex] = verticalWavelets[rowIndex][columnIndex];
				ordered2DFastHaarWaveletTransform[rowIndex + numberOfValues][columnIndex + numberOfValues] = diagonalWavelets[rowIndex][columnIndex];
		
			}
		}
		
	}
	
	/**
	 * @return a copy of the internal array for the inverse transform
	 * @throws Exception 
	 */
	public static double[][] getOrderedFastHaarWaveletTransform() throws Exception {

		if (!ordered2DTransformComplete) {
			throw new Exception("Ordered 2D Fast Haar Wavelet Transform has not been done yet.");
		}
		
		//Return a copy so as to protect the class variable
		return Arrays.copyOf(ordered2DFastHaarWaveletTransform, ordered2DFastHaarWaveletTransform.length);
	}
	
	/**
	 * Generate the inverse ordered 2D Haar wavelet transform and put it in an array
	 * @param sample
	 * @param samepleSizeExponent
	 * @param numberOfSweepsBack
	 * @throws Exception 
	 */
	public static void orderedInverseFastHaarWaveletTransformForNumSweeps(double[][] sample, int sampleSizeExponent, int numberOfSweepsBack) throws Exception {
		
		orderedInverse2DTransformComplete = false;
		
		String testMessage = checkForValidParameters(sample.length, sample[0].length, sampleSizeExponent, numberOfSweepsBack);
		if (testMessage.trim().length() != 0) {
			throw new Exception(testMessage);
		}
		
		orderedInverse2DFastHaarWaveletTransform = Arrays.copyOf(sample, sample.length);;
		
		for (int sweepCounter = 0; sweepCounter < numberOfSweepsBack; ++sweepCounter) {
		
			double[][] rearrangedMatrix = getRearrangedMatrix(sweepCounter);
			
			//Put the rearranged matrix into the reverse transform array
			int rearrangedMatrixSize = rearrangedMatrix.length;
			for (int rowCounter = 0; rowCounter < rearrangedMatrixSize; ++rowCounter) {
				for (int columnCounter = 0; columnCounter < rearrangedMatrixSize; ++columnCounter) {		
					orderedInverse2DFastHaarWaveletTransform[rowCounter][columnCounter] = rearrangedMatrix[rowCounter][columnCounter];
				}
			}
			
			//Loop through the rearranged matrix and do the inverse transform
			int numberOfCells = (rearrangedMatrixSize * rearrangedMatrixSize) / 4, topLeftRow = 0, topLeftColumn = 0;;
			double cellAverage = 0.0, cellHorizontalWavelet = 0.0, cellVerticalWavelet = 0.0, cellDiagonalWavelet = 0.0;
			for (int cellCounter = 0; cellCounter < numberOfCells; ++cellCounter) {
				
				topLeftRow = (cellCounter / (rearrangedMatrixSize / 2)) * 2;
				topLeftColumn = (cellCounter % (rearrangedMatrixSize / 2)) * 2;
				
				cellAverage = orderedInverse2DFastHaarWaveletTransform[topLeftRow][topLeftColumn];
				cellHorizontalWavelet = orderedInverse2DFastHaarWaveletTransform[topLeftRow][topLeftColumn + 1];
				cellVerticalWavelet = orderedInverse2DFastHaarWaveletTransform[topLeftRow + 1][topLeftColumn];
				cellDiagonalWavelet = orderedInverse2DFastHaarWaveletTransform[topLeftRow + 1][topLeftColumn + 1];
				
				orderedInverse2DFastHaarWaveletTransform[topLeftRow][topLeftColumn] = cellAverage + cellHorizontalWavelet + cellVerticalWavelet + cellDiagonalWavelet;
				orderedInverse2DFastHaarWaveletTransform[topLeftRow][topLeftColumn + 1] = cellAverage - cellHorizontalWavelet + cellVerticalWavelet - cellDiagonalWavelet;
				orderedInverse2DFastHaarWaveletTransform[topLeftRow + 1][topLeftColumn] = cellAverage + cellHorizontalWavelet - cellVerticalWavelet - cellDiagonalWavelet;
				orderedInverse2DFastHaarWaveletTransform[topLeftRow + 1][topLeftColumn + 1] = cellAverage - cellHorizontalWavelet - cellVerticalWavelet + cellDiagonalWavelet;				
				
			}
			
		}
		
		orderedInverse2DTransformComplete = true;
		
	}
	
	/**
	 * Rearrange the average and wavelet values so that individual cells can be inverse transformed
	 * @param sweepCounter
	 * @return
	 */
	private static double[][] getRearrangedMatrix(int sweepCounter) {
		
		int sizeOfAverageMatrixSize = Double.valueOf(Math.pow(2, sweepCounter)).intValue();
		int returnMatrixSize = sizeOfAverageMatrixSize * 2;
		double[][] returnValue = new double[returnMatrixSize][returnMatrixSize];
		
		//Fill in the rearranged array
		boolean rowCounterEven = false, columnCounterEven = false;
		for (int rowCounter = 0; rowCounter < returnMatrixSize; ++rowCounter) {
			for (int columnCounter = 0; columnCounter < returnMatrixSize; ++columnCounter) {
				
				rowCounterEven = rowCounter % 2 == 0;
				columnCounterEven = columnCounter % 2 == 0;
				
				if (rowCounterEven && columnCounterEven) {
					returnValue[rowCounter][columnCounter] = orderedInverse2DFastHaarWaveletTransform[rowCounter / 2][columnCounter / 2];//Average
				} else if (rowCounterEven && !columnCounterEven) {
					returnValue[rowCounter][columnCounter] = orderedInverse2DFastHaarWaveletTransform[rowCounter / 2][sizeOfAverageMatrixSize + columnCounter / 2];//Horizontal wavelet
				} else if (!rowCounterEven && columnCounterEven) {
					returnValue[rowCounter][columnCounter] = orderedInverse2DFastHaarWaveletTransform[sizeOfAverageMatrixSize + rowCounter / 2][columnCounter / 2];//Vertical wavelet
				} else {
					returnValue[rowCounter][columnCounter] = orderedInverse2DFastHaarWaveletTransform[sizeOfAverageMatrixSize + rowCounter / 2][sizeOfAverageMatrixSize + columnCounter / 2];//Diagonal wavelet
				}
			
			}
		}
		
		return returnValue;
		
	}
	
	/**
	 * @return a copy of the internal array for the inverse transform
	 * @throws Exception 
	 */
	public static double[][] getOrderedInverse2DFastHaarWaveletTransform() throws Exception {

		if (!orderedInverse2DTransformComplete) {
			throw new Exception("Ordered Inverse 2D Fast Haar Wavelet Transform has not been done yet.");
		}
		
		//Return a copy so as to protect the class variable
		return Arrays.copyOf(orderedInverse2DFastHaarWaveletTransform, orderedInverse2DFastHaarWaveletTransform.length);

	}
	
	/**
	 * Print the sample
	 * @param sample
	 */
	public static void displaySample(double[][] sample) {
		
		System.out.print("\nSample: ");
		for (double[] row : sample) {
			System.out.print("\n[");
			for (double sampleValue : row) {
				System.out.print(sampleValue);
				System.out.print('\t');
			}
			System.out.print("]");
		}
		
	}
	
	private static String checkForValidParameters(int numberOfRows, int numberOfColumns, int sampleSizeExponent, int numberOfSweeps) {
		
		StringBuffer returnValue = new StringBuffer();
		
		if (numberOfSweeps > sampleSizeExponent) {
			returnValue.append("Cannot do " + numberOfSweeps + " forward sweeps as it is greater than the sample size exponent of " + sampleSizeExponent + ".");
		} else if (numberOfColumns != numberOfRows) {
			returnValue.append("The sample must have the same number of rows and columns.");
		} else if(!isPowerOfTwo(numberOfColumns)) {
			returnValue.append("The sample must have dimensions that are an integral multiple of two.");
		}
		
		return returnValue.toString();
		
	}
	
	/**
	 * @param sampleSize
	 * @return true if input is an integral power of 2
	 */
	private static boolean isPowerOfTwo(int sampleSize) {
		
		double sampleSizeLg = Math.log10(sampleSize)/Math.log10(2.0);
		int sampleSizeLgInt = Double.valueOf(sampleSizeLg).intValue();
		
		if (sampleSizeLgInt == sampleSizeLg) {
			return true;
		} else {
			return false;
		}

	}

}