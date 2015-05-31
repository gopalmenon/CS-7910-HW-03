package edu.usu.csatl;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import menon.cs7910.hw03.OneDHaar;

public class OneDHaarEdgeDetection {
	
	// change these variables accordingly.
	static final String SOURCE_PATH = "";
	
	static { 
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	public static void grayscale_image(String infile, String outfile) {
		Mat original = Highgui.imread(SOURCE_PATH + infile);
		Mat grayscale = new Mat(original.rows(), original.cols(), CvType.CV_8UC1);
        Imgproc.cvtColor(original, grayscale, Imgproc.COLOR_RGB2GRAY);
        Highgui.imwrite(SOURCE_PATH + outfile, grayscale);
		
		original.release();
		grayscale.release();
	}
	
	public static void test_01() {
		grayscale_image("uglanov_awakening.jpg", "uglanov_awakening_grayscaled.jpg");
	}
	
	public static void get_pix_values(String file) {
		Mat orig = Highgui.imread(SOURCE_PATH + file);
        System.out.println(SOURCE_PATH + file);
		System.out.println(orig.rows());
		for(int row = 0; row < orig.rows(); row++) {
			for(int col = 0; col < orig.cols(); col++) {
				double[] pix = orig.get(row, col);
				System.out.print("[" + pix[0] + "," + pix[1] + "," + pix[2] + "]" + " ");
			}
			System.out.println();
		}
		orig.release();
	}
	
	public static void test_02() {
		get_pix_values("test_00.jpg");
	}
	
	public static void get_pix_vals_from_1c_img(String file) {
		Mat orig = Highgui.imread(SOURCE_PATH + file);
        System.out.println(SOURCE_PATH + file);
		System.out.println(orig.rows());
		double[] row_pix = new double[orig.rows()];
		for(int row = 0; row < orig.rows(); row++) {
			for(int col = 0; col < orig.cols(); col++) {
				row_pix[col] = orig.get(row, col)[0];
			}
			for(int col = 0; col < orig.cols(); col++) {
				System.out.print(row_pix[col] + " ");
			}
			System.out.println();
		}	
		orig.release();
	}
	
	public static void test_03() {
		get_pix_vals_from_1c_img("test_00.jpg");
	}
	
	public static void apply_row_based_1D_FHWT(String file) {
		Mat orig = Highgui.imread(SOURCE_PATH + file);
        System.out.println(SOURCE_PATH + file);
		System.out.println(orig.rows());
		double[] row_pix = new double[orig.rows()];
		for(int row = 0; row < orig.rows(); row++) {
			for(int col = 0; col < orig.cols(); col++) {
				row_pix[col] = orig.get(row, col)[0];
			}
			System.out.print("Orig: ");
			for(int col = 0; col < orig.cols(); col++) {
				System.out.print(row_pix[col] + "\t");
			}
			OneDHaar.inPlaceFastHaarWaveletTransform(row_pix, true);
			try {
				row_pix = OneDHaar.getInPlaceFastHaarWaveletTransform();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
			System.out.println();
			System.out.print("FHWT: ");
			for(int col = 0; col < orig.cols(); col++) {
				System.out.print(row_pix[col] + "\t");
			}
			System.out.println();
		}
		
		orig.release();
	}
	
	public static void test_04() {
		apply_row_based_1D_FHWT("test_00.jpg");
	}
	
	public final static double WAVELET_COEFF_THRESH = 20.0;
	public final static double EDGE_MARK = 255.0;
	public final static double NO_EDGE_MARK = 0.0;
	public final static double[] NO_EDGE_PIX = { NO_EDGE_MARK, NO_EDGE_MARK, NO_EDGE_MARK };
	public final static double[] YES_EDGE_PIX = { EDGE_MARK, EDGE_MARK, EDGE_MARK };
	
	public static Mat markRowBasedHillTops(String in_file, String out_file) {
		Mat orig = Highgui.imread(SOURCE_PATH + in_file);
		Mat marked = Highgui.imread(SOURCE_PATH + in_file);
        System.out.println(SOURCE_PATH + in_file);
		System.out.println(orig.rows());
		final int num_rows = orig.rows();
		final int num_cols = orig.cols();
		double[] fhwt_row_pix = new double[num_rows];
		for(int row = 0; row < num_rows; row++) {
			for(int col = 0; col < num_cols; col++) {
				fhwt_row_pix[col] = orig.get(row, col)[0];
			}
			System.out.print("Orig: ");
			for(int col = 0; col < num_cols; col++) {
				System.out.print(fhwt_row_pix[col] + "\t");
			}
			OneDHaar.inPlaceFastHaarWaveletTransform(fhwt_row_pix, true);
			try {
				fhwt_row_pix = OneDHaar.getInPlaceFastHaarWaveletTransform();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
			System.out.println();
			System.out.print("FHWT: ");
			for(int col = 0; col < num_cols; col++) {
				System.out.print(fhwt_row_pix[col] + "\t");
			}
			System.out.println();

			for(int col = 1; col < num_cols; col += 2) {
				if ( Math.abs(fhwt_row_pix[col]) >=  WAVELET_COEFF_THRESH ) {
					if ( fhwt_row_pix[col] < 0 ) {
						marked.put(row, col, YES_EDGE_PIX);
						marked.put(row, col-1, NO_EDGE_PIX);
					}
					else if ( fhwt_row_pix[col] > 0 ) {
						marked.put(row,  col, NO_EDGE_PIX);
						marked.put(row, col-1, YES_EDGE_PIX);
					}
				}
				else {
					marked.put(row, col, NO_EDGE_PIX);
					marked.put(row, col-1, NO_EDGE_PIX);
				}
			}
			System.out.print("MRKD: ");
			for(int col = 0; col < num_cols; col++) {
				System.out.print(marked.get(row, col)[0] + "\t");
			}
			System.out.println();
		}
		
		orig.release();
		return marked;
	}
	
	
	public static Mat markColumnBasedHillTops(String in_file, String out_file) {
		Mat orig = Highgui.imread(SOURCE_PATH + in_file);
		Mat marked = Highgui.imread(SOURCE_PATH + in_file);
        System.out.println(SOURCE_PATH + in_file);
		System.out.println(orig.rows());
		final int num_rows = orig.rows();
		final int num_cols = orig.cols();
		double[] fhwt_col_pix = new double[num_cols];
		for(int col = 0; col < num_cols; col++) {
			for(int row = 0; row < num_rows; row++) {
				fhwt_col_pix[row] = orig.get(row, col)[0];
			}
			System.out.print("Orig: ");
			for(int row = 0; row < num_rows; row++) {
				System.out.print(fhwt_col_pix[col] + "\t");
			}
			OneDHaar.inPlaceFastHaarWaveletTransform(fhwt_col_pix, true);
			try {
				fhwt_col_pix = OneDHaar.getInPlaceFastHaarWaveletTransform();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
			System.out.println();
			System.out.print("FHWT: ");
			for(int row = 0; row < num_rows; row++) {
				System.out.print(fhwt_col_pix[row] + "\t");
			}
			System.out.println();

			for(int row = 1; row < num_rows; row += 2) {
				if ( Math.abs(fhwt_col_pix[row]) >=  WAVELET_COEFF_THRESH ) {
					if ( fhwt_col_pix[row] < 0 ) {
						marked.put(row, col, YES_EDGE_PIX);
						marked.put(row, col-1, NO_EDGE_PIX);
					}
					else if ( fhwt_col_pix[row] > 0 ) {
						marked.put(row,  col, NO_EDGE_PIX);
						marked.put(row, col-1, YES_EDGE_PIX);
					}
				}
				else {
					marked.put(row, col, NO_EDGE_PIX);
					marked.put(row, col-1, NO_EDGE_PIX);
				}
			}
			System.out.print("MRKD: ");
			for(int row = 0; row < num_rows; row++) {
				System.out.print(marked.get(row, col)[0] + "\t");
			}
			System.out.println();
		}
		
		orig.release();
		return marked;
	}
	
	public static void markColumnAndRowBasedHillTops(String in_file, String out_file) {
		
		Mat rowBasedMatrix = markRowBasedHillTops(in_file, out_file);
		Mat columnBasedMatrix = markColumnBasedHillTops(in_file, out_file);
		
		Mat orig = Highgui.imread(SOURCE_PATH + in_file);
		Mat output = Highgui.imread(SOURCE_PATH + in_file);
		final int num_rows = orig.rows();
		final int num_cols = orig.cols();

		System.out.println("Creating combined image.");
		
		for(int col = 0; col < num_cols; col++) {
			System.out.println();
			for(int row = 0; row < num_rows; row++) {
				if (rowBasedMatrix.get(row, col)[0] == EDGE_MARK || columnBasedMatrix.get(row, col)[0] == EDGE_MARK) {
					output.put(row, col, YES_EDGE_PIX);
				} else {
					output.put(row, col, NO_EDGE_PIX);
				}
				System.out.print(output.get(row, col)[0] + "\t");
			}
		}

		Highgui.imwrite(SOURCE_PATH +  out_file, output);
		output.release();

	}
	

	public static void main(String[] args) {
		
		if (args.length != 2) {
			System.err.println("Need two parameters - In file name and out file name");
		} else {
			markColumnAndRowBasedHillTops(args[0], args[1]);
		}
		
	}

}
