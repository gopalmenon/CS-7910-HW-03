package menon.cs7910.hw03;

public class OneDHaarClient {
	
	public static final String ORIGINAL_SAMPLE_LABEL = "Original sample: ";
	public static final String ORDERED_FHWT_LABEL = "Ordered FHWT: ";
	public static final String IN_PLACE_FHWT_LABEL = "InPlace FHWT: ";
	public static final String ORDERED_INVERSE_FHWT_LABEL = "Ordered Inverse FHWT: ";
	public static final String IN_PLACE_INVERSE_FHWT_LABEL = "InPlace Inverse FHWT: ";
	public static final String NEW_LINE = "\n";
	public static final String INPUT_DATA_FILE = "beehive_temperatures.txt";
	public static final String WHITE_SPACE = "\\s+";
	public static final String TRUNCATE_DATA = "T";
	
	private static void testOrderedFHWT(double[] sample, boolean truncateData) {
		
		//Print the input sample
		System.out.print(NEW_LINE + ORIGINAL_SAMPLE_LABEL);
		for (double doubleValue : sample) {
			System.out.print(doubleValue + " ");
		}
		
		//Run the transform
		OneDHaar.orderedFastHaarWaveletTransform(sample, truncateData);
		
		try {
			
			//Get the transform
			double[] transform = OneDHaar.getOrderedFastHaarWaveletTransform();
			
			//Print the transform values
			System.out.print(NEW_LINE + ORDERED_FHWT_LABEL);
			for (double doubleValue : transform) {
				System.out.print(doubleValue + " ");
			}
			
		} catch (Exception e) {
			System.err.println("Error while calling OneDHaar.getOrderedFastHaarWaveletTransform()");
			e.printStackTrace();
		}
		
	}
	
	private static void testInPlaceFHWT(double[] sample, boolean truncateData) {
		
		//Print the input sample
		System.out.print(NEW_LINE + ORIGINAL_SAMPLE_LABEL);
		for (double doubleValue : sample) {
			System.out.print(doubleValue + " ");
		}
		
		//Run the transform
		OneDHaar.inPlaceFastHaarWaveletTransform(sample, truncateData);
		
		try {
			
			//Get the transform
			double[] transform = OneDHaar.getInPlaceFastHaarWaveletTransform();
			
			//Print the transform values
			System.out.print(NEW_LINE + IN_PLACE_FHWT_LABEL);
			for (double doubleValue : transform) {
				System.out.print(doubleValue + " ");
			}
			
		} catch (Exception e) {
			System.err.println("Error while calling OneDHaar.getInPlaceFastHaarWaveletTransform()");
			e.printStackTrace();
		}
		
	}
	
	
	private static void testOrderedInverseFHWT(double[] sample) {
		
		//Print the input sample
		System.out.print(NEW_LINE + ORIGINAL_SAMPLE_LABEL);
		for (double doubleValue : sample) {
			System.out.print(doubleValue + " ");
		}
		
		//Run the transform
		try {
			OneDHaar.orderedInverseFastHaarWaveletTransform(sample);
			
			//Get the transform
			double[] transform = OneDHaar.getOrderedInverseFastHaarWaveletTransform();
			
			//Print the transform values
			System.out.print(NEW_LINE + ORDERED_INVERSE_FHWT_LABEL);
			for (double doubleValue : transform) {
				System.out.print(doubleValue + " ");
			}

		} catch (Exception e) {
			System.err.println("Exception thrown when trying to run ordered inverse FHWT");
			e.printStackTrace();
		}
		
	}

	
	public static void main(String[] args) {
		
		boolean truncateData = false;
		//Check input parameter
		if (args != null && args.length > 0) {
			if (TRUNCATE_DATA.equalsIgnoreCase(args[0])) {
				truncateData = true;
			}
		}
		
        double[] sample_00 = {5, 1, 2, 8};
        double[] sample_01 = {3, 1, 0, 4, 8, 6, 9, 9};
        double[] sample_02 = {4.0, -1.0, 2.0, -3.0};
        double[] sample_03 = {5.0, -3.0, 0.0, -1.0, 1.0, -2.0, 1.0, 0.0};
        
        testOrderedFHWT(sample_00, truncateData);
        testOrderedFHWT(sample_01, truncateData);
        
        testInPlaceFHWT(sample_00, truncateData);
        testInPlaceFHWT(sample_01, truncateData);
        
        testOrderedInverseFHWT(sample_02);
        testOrderedInverseFHWT(sample_03);

	}

}