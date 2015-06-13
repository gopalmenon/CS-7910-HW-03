package menon.cs7910.hw04;

public class TwoDHaarClient {

		
	public static double[][] sample_2x2 = {{255, 100},
		                                    {50, 250}
	                                       };
	public static double[][] sample_4x4 = {{9, 7, 6, 2},
										   {5, 3, 4, 4},
										   {8, 2, 4, 0},
										   {6, 0, 2, 2}
										  };

	public static double[][] sample_8x8 = {{255, 0, 0, 0, 0, 0, 0, 100},
										   {0, 255, 0, 0, 0, 0, 100, 0},
										   {0, 0, 255, 0, 0, 100, 0, 0},
										   {0, 0, 0, 255, 100, 0, 0, 0},
										   {0, 0, 0, 120, 150, 0, 0, 0},
										   {0, 0, 120, 0, 0, 150, 0, 0},
										   {0, 120, 0, 0, 0, 0, 150, 0},
										   {120, 0, 0, 0, 0, 0, 0, 150}
										   
										  };

	public static double[][] diagonal_1_8x8 = {{100, 0, 0, 0, 0, 0, 0, 0},
											   {0, 100, 0, 0, 0, 0, 0, 0},
											   {0, 0, 100, 0, 0, 0, 0, 0},
											   {0, 0, 0, 100, 0, 0, 0, 0},
											   {0, 0, 0, 0, 100, 0, 0, 0},
											   {0, 0, 0, 0, 0, 100, 0, 0},
											   {0, 0, 0, 0, 0, 0, 100, 0},
											   {0, 0, 0, 0, 0, 0, 0, 100}
		   
		  };
	
	public static double[][] diagonal_2_8x8 = {{0, 0, 0, 0, 0, 0, 0, 100},
											   {0, 0, 0, 0, 0, 0, 100, 0},
											   {0, 0, 0, 0, 0, 100, 0, 0},
											   {0, 0, 0, 0, 100, 0, 0, 0},
											   {0, 0, 0, 100, 0, 0, 0, 0},
											   {0, 0, 100, 0, 0, 0, 0, 0},
											   {0, 100, 0, 0, 0, 0, 0, 0},
											   {100, 0, 0, 0, 0, 0, 0, 0}
		   
		  };	
	
	public static void main(String[] args) {
		testOrdered2DFHWT(sample_8x8, 3, 3);
	}
	
	public static void testOrdered2DFHWT(double[][] sample, int n, int num_sweeps) {
		
		try {
			TwoDHaar.orderedFastHaarWaveletTransformForNumSweeps(sample, n, num_sweeps);
			System.out.println("Transformed Sample:");
			TwoDHaar.displaySample(TwoDHaar.getOrderedFastHaarWaveletTransform());
			TwoDHaar.orderedInverseFastHaarWaveletTransformForNumSweeps(sample, n, num_sweeps);
			System.out.println("\n\nInverted Sample:");
			TwoDHaar.displaySample(TwoDHaar.getOrderedInverse2DFastHaarWaveletTransform());
		} catch (Exception e) {
			System.out.println("Exception thrown in testOrdered2DFHWT()");
			e.printStackTrace();
			System.exit(0);
		}	}

}
