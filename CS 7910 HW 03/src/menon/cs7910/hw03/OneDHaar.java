package menon.cs7910.hw03;

import java.util.Arrays;

public class OneDHaar {
	
	private static double[] orderedFastHaarWaveletTransform;
	private static double[] inPlaceFastHaarWaveletTransform;
	private static boolean orderedTransformComplete;
	private static boolean inPlaceTransformComplete;
	
	private static double[] orderedInverseFastHaarWaveletTransform;
	private static double[] inPlaceInverseFastHaarWaveletTransform;
	private static boolean orderedInverseTransformComplete;
	private static boolean inPlaceInverseTransformComplete;
	
	/**
	 * Initialize class variables
	 */	
	static {
		orderedTransformComplete = false;
		inPlaceTransformComplete = false;
		orderedInverseTransformComplete = false;
		inPlaceInverseTransformComplete = false;
	}
	
	/**
	 * Generate the ordered Haar wavelet transform in an array
	 * @param sample
	 */
	public static void orderedFastHaarWaveletTransform(final double[] sample, boolean truncateData) {
		
		double[] transform = null;
		if (truncateData) {
			transform = getTruncatedArray(sample);
		} else {
			transform = getPaddedArray(sample);
		}
		
		int numberOfIterations = Double.valueOf(Math.log10(transform.length)/Math.log10(2.0)).intValue(), numberOfTransformCoefficients = 0;
		
		//Do number of iterations equal to the power of 2
		for (int iterationCounter = 0; iterationCounter < numberOfIterations; ++iterationCounter) {
			
			//Do Ordered FHWT on the step coefficients part of the array
			numberOfTransformCoefficients = Double.valueOf(Math.pow(2.0, numberOfIterations - iterationCounter)).intValue();
			double stepCoefficient = 0.0, waveletCoefficient = 0.0;
			double[] currentTransform = new double[numberOfTransformCoefficients];
			int coefficientJumpFactor = currentTransform.length/2;
			for (int transformCoefficientsCounter = 0, currentTransformIndex = 0; transformCoefficientsCounter < numberOfTransformCoefficients; transformCoefficientsCounter += 2, ++currentTransformIndex) {
			
				stepCoefficient = (transform[transformCoefficientsCounter] + transform[transformCoefficientsCounter + 1])/2.0;
				currentTransform[currentTransformIndex] = stepCoefficient;
				
				waveletCoefficient = (transform[transformCoefficientsCounter] - transform[transformCoefficientsCounter + 1])/2.0;
				currentTransform[currentTransformIndex + coefficientJumpFactor] = waveletCoefficient;
				
			}
			
			//Copy the current transform back into main array
			int currentTransformCounter = 0;
			for (double doubleValue : currentTransform) {
				transform[currentTransformCounter++] = doubleValue;
			}
			
		}
		
		orderedFastHaarWaveletTransform = transform;
		orderedTransformComplete = true;
	} 
	
	/**
	 * Generate the in place Haar wavelet transform in an array
	 * @param sample
	 */
	public static void inPlaceFastHaarWaveletTransform(double[] sample, boolean truncateData) {
			
		double[] transform = null;
		if (truncateData) {
			transform = getTruncatedArray(sample);
		} else {
			transform = getPaddedArray(sample);
		}
		
		int numberOfIterations = Double.valueOf(Math.log10(transform.length)/Math.log10(2.0)).intValue();
		int indexIncrement = 1;
		int coefficientJumpFactor = 2;
		int numberOfSampleValues = transform.length;
		int currentStepIndex = 0, currentWaveletIndex = 0;
		double currentStepValue = 0.0, currentWaveletValue = 0.0;
		
		for (int iterationCounter = 0; iterationCounter < numberOfIterations; ++iterationCounter) {
			
			numberOfSampleValues /= 2;
			
			for (int transformCoefficientsCounter = 0; transformCoefficientsCounter < numberOfSampleValues; ++transformCoefficientsCounter) {
				
				currentStepIndex = coefficientJumpFactor * transformCoefficientsCounter;
				currentWaveletIndex = currentStepIndex + indexIncrement;

				currentStepValue = (transform[currentStepIndex] + transform[currentWaveletIndex])/2.0;
				currentWaveletValue = (transform[currentStepIndex] - transform[currentWaveletIndex])/2.0;
				
				transform[currentStepIndex] = currentStepValue;
				transform[currentWaveletIndex] = currentWaveletValue;
			
			}
			
			indexIncrement = coefficientJumpFactor;
			coefficientJumpFactor *= 2;
			
		}
		
		inPlaceFastHaarWaveletTransform = transform;
		inPlaceTransformComplete = true;
	}
	
	public static double[] getOrderedFastHaarWaveletTransform() throws Exception {
		
		if (!orderedTransformComplete) {
			throw new Exception("Ordered Fast Haar Wavelet Transform has not been done yet.");
		}
		
		//Return a copy so as to protect the class variable
		return Arrays.copyOf(orderedFastHaarWaveletTransform, orderedFastHaarWaveletTransform.length);
		
	}
	
	
	public static double[] getInPlaceFastHaarWaveletTransform() throws Exception {
		
		if (!inPlaceTransformComplete) {
			throw new Exception("In Place Fast Haar Wavelet Transform has not been done yet.");
		}
		
		//Return a copy so as to protect the class variable
		return Arrays.copyOf(inPlaceFastHaarWaveletTransform, inPlaceFastHaarWaveletTransform.length);

	}
	
	/**
	 * Generate the ordered inverse Haar wavelet transform in an array
	 * @param sample
	 * @throws Exception 
	 */
	public static void orderedInverseFastHaarWaveletTransform(double[] sample) throws Exception {
		
		if (!isPowerOfTwo(sample.length)) {
			throw new Exception("Ordered Inverse Fast Haar Wavelet Transform can only be done on a sample that is an integral power of 2.");
		}
		
		double [] sampleCopy = Arrays.copyOf(sample, sample.length), coefficients = null;;
		double firstStepCoeff = 0.0, secondStepCoeff = 0.0;
		int gap = 0, j = 0, numberOfSweeps = Double.valueOf(Math.log10(sample.length)/Math.log10(2.0)).intValue();
		
		for (int coefficientCounter = 1; coefficientCounter <= numberOfSweeps; ++coefficientCounter) {
			
			gap = Double.valueOf(Math.pow(2, coefficientCounter - 1)).intValue();
			j = 0;
			
			coefficients = new double[2 * gap];
			
			for (int i = 0; i < gap; ++i) {
				
				firstStepCoeff = sampleCopy[i] + sampleCopy[gap + i];
				secondStepCoeff = sampleCopy[i] - sampleCopy[gap + i];
				coefficients[j] = firstStepCoeff;
				coefficients[j + 1] = secondStepCoeff;
				
				j += 2;
			}
			
			for (int counter = 0; counter < coefficients.length; ++ counter) {
				sampleCopy[counter] = coefficients[counter];
			}
			
		}
		
		orderedInverseFastHaarWaveletTransform = sampleCopy;
		orderedInverseTransformComplete = true;
		
	} 
	
	/**
	 * Generate the in place inverse Haar wavelet transform in an array
	 * @param sample
	 * @throws Exception 
	 */
	public static void inPlaceInverseFastHaarWaveletTransform(double[] sample) throws Exception {
		
		if (!isPowerOfTwo(sample.length)) {
			throw new Exception("In Place Inverse Fast Haar Wavelet Transform can only be done on a sample that is an integral power of 2.");
		}
		
		double [] sampleCopy = Arrays.copyOf(sample, sample.length);

		int numberOfSweeps = Double.valueOf(Math.log10(sample.length)/Math.log10(2.0)).intValue();
		int gap = Double.valueOf(Math.pow(2, numberOfSweeps - 1)).intValue();
		int j = 2 * gap;
		int m  = 1;
		double firstStepCoeff = 0.0, secondStepCoeff = 0.0;

		//Do number of sweeps equal to the log base 2 of the sample size
		for (int sweepCounter = 0; sweepCounter < numberOfSweeps; ++sweepCounter) {
		
			for (int k = 0; k <= m - 1; ++k) {
		
				firstStepCoeff = sampleCopy[j * k] + sampleCopy[j * k + gap];
				secondStepCoeff = sampleCopy[j * k] - sampleCopy[j * k + gap];
			
				sampleCopy[j * k] = firstStepCoeff;
				sampleCopy[j * k + gap] = secondStepCoeff;
				
			}
			
			j = gap;
			gap /= 2;
			m *= 2;
			
		}

		inPlaceInverseFastHaarWaveletTransform = sampleCopy;
		inPlaceInverseTransformComplete = true;		
		
	}
	
	
	public static double[] getOrderedInverseFastHaarWaveletTransform() throws Exception {
		
		if (!orderedInverseTransformComplete) {
			throw new Exception("Ordered Inverse Fast Haar Wavelet Transform has not been done yet.");
		}
		
		//Return a copy so as to protect the class variable
		return Arrays.copyOf(orderedInverseFastHaarWaveletTransform, orderedInverseFastHaarWaveletTransform.length);

	}
	
	
	public static double[] getInPlaceInverseFastHaarWaveletTransform() throws Exception {
		
		if (!inPlaceInverseTransformComplete) {
			throw new Exception("In Place Inverse Fast Haar Wavelet Transform has not been done yet.");
		}
		
		//Return a copy so as to protect the class variable
		return Arrays.copyOf(inPlaceInverseFastHaarWaveletTransform, inPlaceInverseFastHaarWaveletTransform.length);

	}
	
	/**
	 * @param sample
	 * @return array that is the next integral multiple of a power of 2. Return the same array if it is already
	 * of size that is a power of 2.
	 */
	private static double[] getPaddedArray(final double[] sample) {
		
		int sampleSize = sample.length;
		double sampleSizeLg = Math.log10(sampleSize)/Math.log10(2.0);
		int sampleSizeLgInt = Double.valueOf(sampleSizeLg).intValue();
		
		//Return input if already the size is an integral power of 2
		if (sampleSizeLgInt == sampleSizeLg) {
			return Arrays.copyOf(sample, sample.length);
		}
		
		//Create an array of size the next integral power of 2
		int paddedSize = Double.valueOf(Math.pow(2, Math.ceil(sampleSizeLg))).intValue();
		double[] returnValue = new double[paddedSize];
		
		//Fill the initial part of the array with the input sample
		int arrayIndex = 0;
		for (double doubleValue : sample) {
			returnValue[arrayIndex++] = doubleValue;
		}
		
		return returnValue;
		
	}
	
	/**
	 * @param sample
	 * @return array that is the next lower integral multiple of a power of 2. Return the same array if it is already
	 * of size that is a power of 2.
	 */
	private static double[] getTruncatedArray(final double[] sample) {
		
		int sampleSize = sample.length;
		double sampleSizeLg = Math.log10(sampleSize)/Math.log10(2.0);
		
		//Return input if already the size is an integral power of 2
		if (isPowerOfTwo(sample.length)) {
			return Arrays.copyOf(sample, sample.length);
		}
		
		//Create an array of size the next integral power of 2
		int truncatedSize = Double.valueOf(Math.pow(2, Math.ceil(sampleSizeLg) - 1)).intValue();
		double[] returnValue = new double[truncatedSize];
		
		//Fill the initial part of the array with the input sample
		int arrayIndex = 0;
		for (double doubleValue : sample) {
			
			if (arrayIndex == returnValue.length) {
				break;
			}
			returnValue[arrayIndex] = doubleValue;
			arrayIndex++;
		}
		
		return returnValue;
		
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