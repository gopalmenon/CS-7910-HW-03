package menon.cs7910.hw03;

public class OneDHaarClient {
		
	private static void testOrderedFHWT(double[] sample) {
		System.out.println("\nOriginal sample:"); 
		OneDHaar.displaySample(sample); 
		OneDHaar.orderedFastHaarWaveletTransform(sample, false); 
		System.out.println("\nOrdered FHWT:"); 
		try {
			OneDHaar.displaySample(OneDHaar.getOrderedFastHaarWaveletTransform());
			OneDHaar.orderedInverseFastHaarWaveletTransform(OneDHaar.getOrderedFastHaarWaveletTransform()); 
			System.out.println("\nOrdered Inverse FHWT:"); 
			OneDHaar.displaySample(OneDHaar.getOrderedInverseFastHaarWaveletTransform());
		} catch (Exception e) {
			System.err.println("Exception thrown in testOrderedFHWT()");
			e.printStackTrace();
		} 
	}
	
	private static void testInverseFHWT(double[] sample) {
		
		System.out.println("\nOriginal sample:"); 
		OneDHaar.displaySample(sample); 
		OneDHaar.inPlaceFastHaarWaveletTransform(sample, false); 
		System.out.println("\nIn-Place FHWT:"); 
		try {
			OneDHaar.displaySample(OneDHaar.getInPlaceFastHaarWaveletTransform()); 
			System.out.println("\nIn-Place Inverse FHWT:"); 
			OneDHaar.inPlaceFastInverseHaarWaveletTransform(OneDHaar.getInPlaceFastHaarWaveletTransform());
			OneDHaar.displaySample(OneDHaar.getInPlaceInverseFastHaarWaveletTransform());
		} catch (Exception e) {
			System.err.println("Exception thrown in testInverseFHWT()");
			e.printStackTrace();
		} 
	}
	
	public static void main(String[] args) {
		
        double[] sample01 = {11, 3}; 
        double[] sample02 = {5, 1, 2, 8}; 
        double[] sample03 = {3, 1, 0, 4, 8, 6, 9, 9};
        testOrderedFHWT(sample01); 
        testOrderedFHWT(sample02); 
        testOrderedFHWT(sample03);
        
        double[] sample04 = {11, 3}; 
        double[] sample05 = {5, 1, 2, 8};
        double[] sample06 = {3, 1, 0, 4, 8, 6, 9, 9};
        
        testInverseFHWT(sample04); 
        testInverseFHWT(sample05); 
        testInverseFHWT(sample06);
        
	}

}