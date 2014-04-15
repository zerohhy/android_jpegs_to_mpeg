package compression;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class DCT {
	public static final int[][] quantMat2 = {{16,11,10,16,24,40,51,61},
											{12,12,14,19,26,58,60,55},
											{14,13,16,24,40,57,69,56},
											{14,17,22,29,51,87,80,62},
											{18,22,37,56,68,109,103,77},
											{24,35,55,64,81,104,113,92},
											{49,64,78,87,103,121,120,101},
											{72,92,95,98,112,100,103,99}};
	
	
	public static final int[][] quantMat = {{32,22,20,32,48,80,102,122},
		{24,24,28,38,52,116,120,110},
		{28,26,32,28,80,114,128,112},
		{28,34,22,58,51,87,80,62},
		{16,44,37,112,68,218,203,150},
		{48,70,55,128,81,208,213,200},
		{98,128,78,164,103,240,220,201},
		{144,184,95,186,112,200,203,200}};
	
	public static double[][] DCT(int[][] b){
		double[][] G = new double[8][8];
		int[][] g = shift(b);
		double a=1,v=1;
		double s = 1 / Math.sqrt(2);
		int i,j,x,y;
		int pix;
		double coeff,sum,val1,val2;
		for(i = 0; i < 8 ; i++){
			for(j = 0; j < 8 ;j++){
				
				if(i == 0){
					a = s;
				}else{
					a = 1;
				}
				
				if(j == 0){
					v = s;
				}else{
					v = 1;
				}
				
				coeff = (1 / (Math.sqrt(2*8)));
				coeff = coeff * a * v;
				sum = 0;
				for(x = 0; x < 8; x++ ){
					for(y = 0; y < 8 ; y++){
						pix = g[x][y];
						val1 = Math.cos(((2*x + 1) * i * Math.PI) / 16);
						val2 = Math.cos(((2*y + 1) * j * Math.PI) / 16);
						sum += pix * val1 * val2;
					}	
				}
			
				G[i][j]	= coeff * sum;
			}
		}
		return G;
	}

	
	public static double[][] DCT2(int[][] b,int rowStart,int colStart){
		double[][] G = new double[8][8];
		int[][] g = shift(b,rowStart,colStart);
		double a=1,v=1;
		double s = 1 / Math.sqrt(2);
		int i,j,x,y;
		int pix;
		double coeff,sum,val1,val2;
		for(i = 0; i < 8 ; i++){
			for(j = 0; j < 8 ;j++){
				
				if(i == 0){
					a = s;
				}else{
					a = 1;
				}
				
				if(j == 0){
					v = s;
				}else{
					v = 1;
				}
				
				coeff = (1 / (Math.sqrt(2*8)));
				coeff = coeff * a * v;
				sum = 0;
				for(x = 0; x < 8; x++ ){
					for(y = 0; y < 8 ; y++){
						pix = g[x][y];
						val1 = Math.cos(((2*x + 1) * i * Math.PI) / 16);
						val2 = Math.cos(((2*y + 1) * j * Math.PI) / 16);
						sum += pix * val1 * val2;
					}	
				}
			
				G[i][j]	= coeff * sum;
			}
		}
		return G;
	}
	
	public static int[][] quantize(double[][] g){
		int[][] Q = new int[8][8];
		for(int i = 0; i < 8 ; i++){
			for(int j = 0; j < 8 ;j++){
				Q[i][j] = (int) Math.round((g[i][j] / quantMat[i][j]));
			}
		}
		

		return Q;
	}

	public static int[][] IDCT(int[][] Q){
		int[][] F = new int[8][8];
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				F[i][j] = Q[i][j] * quantMat[i][j];
			}
		}
		printI(F);
		System.out.println("------------");
		int[][] f = new int[8][8];
		int x,y,u,v,pix;
		double coeff,sum,c1,c2,temp1,temp2;
		
		for(x = 0; x < 8; x++){
			for(y = 0; y < 8; y++){
				sum = 0;
				for(u = 0; u < 8; u++){
					for(v = 0; v < 8; v++){
						if(u == 0){
							c1 = 1 / Math.sqrt(2);
						}else{
							c1 = 1;
						}
						
						if(v == 0){
							c2 = 1 / Math.sqrt(2);
						}else{
							c2 = 1;
						}
						pix = F[u][v];
						temp1 = Math.cos( ( (2*x + 1) * u * Math.PI ) / 16);
						temp2 = Math.cos( ( (2*y + 1) * v * Math.PI ) / 16);
						
						sum += c1 * c2 * pix * temp1 * temp2;
					}
				}
				f[x][y] =(int) Math.round(.25 * sum) + 128;
			}
		}
		
		
		
		return f;
	}
	
	public static double getError(int[][] original, int[][] decomp){
		double sum = 0;
		for(int i = 0; i < original.length; i++){
			for(int j = 0; j < original.length; j++){
				sum += Math.abs(original[i][j] - decomp[i][j]);
			}
		}
		return sum / (Math.pow(original.length, 2));
	}
	
	
	public static int[][] getSampleData(){
		int[][] data = new int[32][32];
		int i,j;
		String dir = System.getProperty("user.dir");
		String fileName = dir + "/data/sample.txt";
		
		BufferedReader br = null;
		try{
			String sCurrentLine;
			br = new BufferedReader(new FileReader(fileName));
			i = 0;
			while((sCurrentLine = br.readLine()) != null){
				List<String> cList = Arrays.asList(sCurrentLine.split(","));
				//System.out.println(cList);
				for(j = 0; j < 32; j++){
					data[i][j] = Integer.parseInt(cList.get(j));
				}
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
 		
		return data;
		
	}
	
	
	public static int[][][] parseArray(int[][] a){
		int dimension = a.length / 8;

		
		int[][][] arrays = new int[dimension][8][8];
		
		int i,j,k;
		
		//scans 8x8 blocks from top to bottom, left to right
		
		
		
		
		return arrays;
		
	}
	
	
	public static void main(String[] args) {
		int[][] a = { 	{52,55,61,66,70,61,64,73}, 
						{63,59,55,90,109,85,69,72},
						{62,59,68,113,144,104,66,73},
						{63,58,71,122,154,106,70,69},
						{67,61,68,104,126,88,68,70},
						{79,65,60,70,77,68,58,75},
						{85,71,64,59,55,61,65,83},
						{87,79,69,68,65,76,78,94}};
		

		int[][] data = getSampleData();
		int[][][] arrays = parseArray(data);
		
		
		printI(a);
		double[][] G = DCT(a);
		System.out.println("-----------");
		printD(G);
		int[][] Q = quantize(G);
		System.out.println("-----------");
		printI(Q);
		System.out.println("-----------");
		int[][] f = IDCT(Q);
		printI(f);
		System.out.println("The average absolute error per pixel is " + getError(a,f) + "%");
		
		
	}
	
	
	
	
	
	public static void printD(double[][] a){
		for(int i = 0; i < a.length;i++){
			for(int j = 0; j < a.length;j++){
				System.out.printf("%.2f\t", a[i][j]);
			}
			System.out.println();
		}
	}

	public static void printI(int[][] a){
		for(int i = 0; i < a.length;i++){
			for(int j = 0; j < a.length;j++){
				System.out.printf("%d\t", a[i][j]);
			}
			System.out.println();
		}
	}
	
	
	
	
	@SuppressWarnings("unused")
	public static int[][] shift(int[][] a){
		int[][] g = new int[8][8];
		for(int i = 0; i < 8;i++){
			for(int j = 0; j < 8;j++){
				g[i][j] = a[i][j] - 128;
			}
		}
		if(false){
			for(int i = 0; i < 8 ; i++){
				for(int j = 0; j < 8 ;j++){
					System.out.printf("%d ", g[i][j]);
				}
				System.out.println();
			}
		}
		
		return g;
	}
	
	public static int[][] shift(int[][] a, int rowStart,int colStart){
		int[][] g = new int[8][8];
		for(int i = 0; i < 8;i++){
			for(int j = 0; j < 8;j++){
				g[i][j] = a[rowStart + i][colStart + j] - 128;
			}
		}
		
		return g;
	}
	
	

}