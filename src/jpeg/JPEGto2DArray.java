package jpeg;

import android.graphics.Bitmap;
import android.graphics.Color;
import compression.DCT;



//import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

//import javax.imageio.ImageIO;

public class JPEGto2DArray {
	public static final boolean DEBUG = true;
	
	public static final int[][] create2DArray (Bitmap image){ 
		int width = image.getWidth(); 
		int height = image.getHeight(); 
		int [][] product = new int[height][width];
		
		for (int row = 0; row < height; row++)  { 
			for (int col = 0; col < width; col++)  { 
				product [row][col] = image.getPixel(row, col);
			}
		}
		
		return product; 
	}

	public static void writeToTextFile(int[][] image2DArray, String filename){
		try{  
	           	System.out.println("Saving file " + (System.getProperty("user.dir") + "/data/" + filename + ".csv"));  
				FileWriter fr = new FileWriter((System.getProperty("user.dir") + "/data/") + filename + ".csv");  
				BufferedWriter br = new BufferedWriter(fr);  
				PrintWriter out = new PrintWriter(br);  
				for(int i=0; i < image2DArray.length; i++){  
					for(int j=0; j < image2DArray[1].length;j++){
						out.write(image2DArray[i][j] + ",");  
					}
					out.write("\n");
				}
				out.close();  
	             
	             
	       	}  
	         
	       	catch(IOException e){  
	       		System.out.println(e);     
	       	}  
	}
	
	/*
	public static int[][] getRed(Bitmap in){
		int width = in.getWidth();
		int height = in.getHeight();
		int[][] red = new int[height][width];
		try {
			

			

			int[] data = new int[width * height];
			
			
			in.getRGB(0, 0, width, height, data, 0, width);

			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					red[i][j] = ((data[i*height + j] >> 16) & 0xff);
				}
			}

			writeToTextFile(red,"red");

		} catch (Exception e) {
			System.err.println("Error: " + e);
		}
		
		return red;
	
	}

	public static int[][] getGreen(Bitmap in){
		int width = in.getWidth();
		int height = in.getHeight();
		int[][] green = new int[height][width];
		try {
			

			

			int[] data = new int[width * height];
			
			
			in.getRGB(0, 0, width, height, data, 0, width);

			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					green[i][j] = ((data[i*height + j] >> 16) & 0xff);
				}
			}

			writeToTextFile(green,"green");

		} catch (Exception e) {
			System.err.println("Error: " + e);
		}
		
		return green;
	
	}
	
	public static int[][] getBlue(Bitmap in){
		int width = in.getWidth();
		int height = in.getHeight();
		int[][] blue = new int[height][width];
		try {
			

			

			int[] data = new int[width * height];
			
			
			in.getRGB(0, 0, width, height, data, 0, width); //getRGB put all value into a 1D array data

			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					blue[i][j] = ((data[i*height + j] >> 16) & 0xff);
				}
			}

			writeToTextFile(blue,"blue");

		} catch (Exception e) {
			System.err.println("Error: " + e);
		}
		
		return blue;
	
	}
	
	
	public static int[][][] writeColorImageValueToFile(Bitmap in) {
		int width = in.getWidth();
		int height = in.getHeight();

		System.out.println("width=" + width + " height=" + height);
		int[][][] rgb = new int[3][width][height];
		try {
			int[][] b = new int[height][width];
			int[][] c = new int[height][width];
			int[][] d = new int[height][width];
			

			int[] data = new int[width * height];
			
			
			in.getRGB(0, 0, width, height, data, 0, width);

			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					b[i][j] = ((data[i*height + j] >> 16) & 0xff);
					c[i][j] = ((data[i*height + j] >> 8) & 0xff);
					d[i][j] = (data[i*height + j] & 0xff);
				}
			}

			writeToTextFile(b,"red");
			writeToTextFile(c,"green");
			writeToTextFile(d,"blue");
			int[][][] rgbTemp = {b,c,d};
			rgb = rgbTemp;
		} catch (Exception e) {
			System.err.println("Error: " + e);
		}
		
		return rgb;
	}
	 */
	public static int[][][] getRGBL(Bitmap in){
		if(DEBUG) System.out.println("onActivityResult->jpegCompression.CompressJpeg.start_point->CompressJpeg.processImage->JPEGto2DArray.getRGBL");
		int width = in.getWidth();
		int height = in.getHeight();
		int[][][] rgbl = new int[4][height][width]; //3d array for ([x],[y],[r,g,b])
		try {
			int[] data = new int[width * height];
			//Bitmap data = in.getPixels(0, 0, 0, width, height, data, height);//getRGB(0, 0, width, height, data, 0, width);
//			for (int i = 0; i < height; i++) {
//				for (int j = 0; j < width; j++) {
//					rgbl[0][i][j] = ((data[i*height + j] >> 16) & 0xff);
//					rgbl[1][i][j] = ((data[i*height + j] >> 8) & 0xff);
//					rgbl[2][i][j] = (data[i*height + j] & 0xff);
//					rgbl[3][i][j] = (data[i*height + j] >> 24) & 0xFF;
//				}
//			}
			//loading color from each pixel to arrays
			//rgbl[3][i][j] not sure what's needed
			for(int i=0; i<height; i++){
				for(int j=0; j<width; j++){
					rgbl[0][i][j] = Color.red(in.getPixel(j, i));  //((data[i*height + j] >> 16) & 0xff);
					//if(DEBUG) System.out.print(rgbl[0][i][j] + " ");
					rgbl[1][i][j] = Color.green(in.getPixel(j, i));//((data[i*height + j] >> 8) & 0xff);
					rgbl[2][i][j] = Color.blue(in.getPixel(j, i));//(data[i*height + j] & 0xff);
					rgbl[3][i][j] = Color.blue(in.getPixel(j, i));//(data[i*height + j] >> 24) & 0xFF;
				}
				//if(DEBUG) System.out.println("\n");
			}
			if(DEBUG) print(rgbl,width,height,20);
			
		} catch (Exception e) {
			System.err.println("Error: " + e);
		}
		return rgbl;
	}
	
	
	// print out array with downsampling to check if array are loaded correctly
	public static void print(int[][][] input, int w ,int h, int downsample){
		for(int i=0; i<h; i=i+downsample){
			for(int j=0; j<w; j=j+downsample){
				System.out.print(input[0][i][j]); 
				if(input[0][i][j]<10){
					System.out.print("   ");
				}
				else if(input[0][i][j]>=100){
					System.out.print(" ");
				}
				else{
					System.out.print("  ");
				}
			}
			System.out.println("\n");
		}
	}
	
	
	
	public static void main(String args[]) throws IOException{ 
		

		
	}


}
	
/*
		
		int width=437,height=370;
		
		int hBlocks = width/8;
		int vBlocks = height/8;

		System.out.println("Height: " + height + " Width: " + width);

		
		
		String fileName = System.getProperty("user.dir") + "/src/jpeg/redball.jpeg";

		File file = new File(fileName); 
		BufferedImage image = ImageIO.read(file); 
		
		int i1,j1;
		int [][] temp = new int[8][8];
		int[][] red = getRed(image);
		System.out.println("Red height: " + red.length);
		System.out.println("Red width: " + red[0].length);
		for(int i = 0; i < vBlocks; i++){
			for(int j = 0; j < hBlocks;j++){
				i1 = i * 8;
				j1 = j * 8;
				//System.out.println("(i1,j1): " + i1 + " " + j1);
				
				temp = DCT.quantize(DCT.DCT2(red,i1,j1));
				
				for(int row = 0; row < 8;row++){
					for(int col = 0; col < 8; col++){
						//System.out.print("[" + i + "," + j + "]" + temp[row][col]+" ");
						System.out.print(temp[row][col]+" ");
					}
					System.out.println();
				}
				System.out.println("--------------------");
			}
		}
		
	}
	
}

for(int i = 0; i < vBlocks; i++){
	for(int j = 0; j < hBlocks;j++){
		for(int i1 = i*8; i1 < (i+1)*8; i1++){
			for(int j1= j*8; j1 < (j+1)*8; j1++){
				System.out.print(red[i1][j1] + " ");
			}
			System.out.println();
		}
		System.out.println("----");
	}
}
*/