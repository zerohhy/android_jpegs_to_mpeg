package jpegCompression;
//import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

//import javax.imageio.ImageIO;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import jpeg.JPEGto2DArray;
import compression.DCT;


public class CompressJpeg {
	public static final boolean DEBUG = true;
	public static final int blockSize = 8;
	public static Bitmap getImage(String fileName) throws IOException{
		File file = new File(fileName); 
		//BufferedImage image = ImageIO.read(file);
		Bitmap image = BitmapFactory.decodeFile(fileName);
		return image;
	}
	
	
	
	
	public static int[][][] processImage(String fileName) throws IOException{
		if(DEBUG) System.out.println("onActivityResult->jpegCompression.CompressJpeg.start_point->CompressJpeg.processImage");
		if(DEBUG) System.out.println("Processing " + fileName);
		Bitmap in = getImage(fileName);
		int height = in.getHeight();
		int width = in.getWidth();
		if(DEBUG) System.out.println("Height: " + height + "\nWidth: " + width);
		//int blockSize = 8; // set each block size
		int vertBlocks = height / blockSize; //find how many blocks
		int horzBlocks = width / blockSize;
		if(DEBUG) System.out.println("#Vertical Blocks: " + vertBlocks + "\n#Horizontal Blocks: " + horzBlocks + "\n");
		int[][][] rgba = JPEGto2DArray.getRGBL(in);
		//in.get
		int[][] red = rgba[0];
		int[][] green = rgba[1];
		int[][] blue = rgba[2];
		int[][] alpha = rgba[3];
		JPEGto2DArray.writeToTextFile(red,"red");
		JPEGto2DArray.writeToTextFile(green,"green");
		JPEGto2DArray.writeToTextFile(blue,"blue");
		JPEGto2DArray.writeToTextFile(alpha,"alpha");
		
		return rgba;
		
	}
	
	public static void compressComponent(int[][] component, int componentCode) throws IOException{
		if(DEBUG) System.out.println("onActivityResult -> jpegCompression.CompressJpeg.start_point->CompressJpeg.processImage->JPEGto2DArray.getRGBL->CompressJpeg.compressComponent");
		String fileName;
		if(componentCode == 0)
			fileName = "redCompressed";
		else if(componentCode == 1)
			fileName = "greenCompressed";
		else if(componentCode == 2)
			fileName = "blueCompressed";
		else
			fileName = "alphaCompressed";
		
		System.out.println("Saving file " + System.getProperty("user.dir") + "/data/" + fileName + ".csv");  
		FileWriter fr = new FileWriter(System.getProperty("user.dir") + "/data/" + fileName + ".csv");  
        BufferedWriter br = new BufferedWriter(fr);  
        PrintWriter out = new PrintWriter(br);
        
        
        int i1,j1;
		int [][] temp = new int[blockSize][blockSize];
		int vBlocks = component.length/blockSize;
		int hBlocks = component[1].length/blockSize;
		for(int i = 0; i < vBlocks; i++){
			for(int j = 0; j < hBlocks;j++){
				i1 = i * blockSize;
				j1 = j * blockSize;
				if(DEBUG) System.out.println(i1 + " " + j1);
				temp = DCT.quantize(DCT.DCT2(component,i1,j1));
				
				for(int row = 0; row < blockSize;row++){
					for(int col = 0; col < blockSize; col++){
						if(DEBUG) System.out.print("[" + i + "," + j + "]" + temp[row][col]+" ");
						if(temp[row][col]!=0)
							out.write(temp[row][col] +",");
					}
					out.write("\n");
				}
				out.write("---\n");
			}
		}
        out.close();
	}
	
	public static void start_point(String args) throws IOException{
		String fileName = args;//System.getProperty("user.dir") + "/data/img2.jpg";
		System.out.println("Getting raw RGBA components for " + fileName);
		
		long startTime = System.nanoTime(); 
		int[][][] rgba = processImage(fileName);	
		long endTime = System.nanoTime(); 
		
		// find duration in milliseconds (need to convert from nanoseconds)
		double duration = (endTime - startTime)/1000000;
		System.out.printf("%n Time to process image into RGBA matrix: %.2f milliseconds %n", duration); 
		
		
		startTime = System.nanoTime(); 
		System.out.println("\nCompressing RGBA components...\n");
		for(int i = 0; i < 4; i++){
			compressComponent(rgba[i],i);
		}
		endTime = System.nanoTime(); 
		duration = (endTime - startTime)/1000000; 
		System.out.printf("%n Time to compress RBGA components: %.2f milliseconds %n", duration); 
		
		
		System.out.println("\nDone...");
		
	}
	
	
//	public static void main(String[] args) throws IOException{
//		String fileName = System.getProperty("user.dir") + "/data/img2.jpg";
//		System.out.println("Getting raw RGBA components for " + fileName);
//		
//		long startTime = System.nanoTime(); 
//		int[][][] rgba = processImage(fileName);	
//		long endTime = System.nanoTime(); 
//		
//		// find duration in milliseconds (need to convert from nanoseconds)
//		double duration = (endTime - startTime)/1000000; 
//		System.out.printf("%n Time to process image into RGBA matrix: %.2f milliseconds %n", duration); 
//		
//		
//		startTime = System.nanoTime(); 
//		System.out.println("\nCompressing RGBA components...\n");
//		for(int i = 0; i < 4; i++){
//			compressComponent(rgba[i],i);
//		}
//		endTime = System.nanoTime(); 
//		duration = (endTime - startTime)/1000000; 
//		System.out.printf("%n Time to compress RBGA components: %.2f milliseconds %n", duration); 
//		
//		
//		System.out.println("\nDone...");
//		
//	}
	
}
