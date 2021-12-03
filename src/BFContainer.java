import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

/**
 *
 * @author Kenneth-Aponte
 *
 */
public class BFContainer {

	public static class Main {
		public static BloomFilter bF;//the bloom filter object
		public static Scanner input = new Scanner(System.in);
		public static final double FALSE_POSITIVE_PROBABILITY =  0.0000001;//final as this value is predetermined

		//TODO: remove these
		// /Users/kennethaponte/Downloads/emails.csv
		// /Users/kennethaponte/Documents/emails2.csv
		// /Users/kennethaponte/Documents/emails2_test.csv

		//Runs the project
		public static void main(String[] args) {
			File inputFile_1 = getInputFile("\nTo create the Bloom Filter, please provide the path to the input file: ");
			bF = createAndPopulateBloomFilter(inputFile_1);
			File inputFile_2 = getInputFile("\nTo test the Bloom Filter, please provide the path to the input file: ");
			File resultFile = testBloomFilter(inputFile_2);

			input.close();
		}

		//Returns the input file once it has been validated. Loops until user inputs a valid file
		public static File getInputFile(String msg) {
			File iF = null;
			boolean validFile = false;

			System.out.println("\n----------------------------------------------------------------------------------------------------------");
			System.out.print(msg);

			while(!validFile){
				iF = new File(input.nextLine());
				if(!iF.exists()){
					System.out.print("Please enter a valid file path: ");
					continue;
				}
				validFile = true;
			}

			return iF;//returns the input file once its valid
		}

		//Calculates all the parameters based on a given input File and created a respective Bloom Filter with those parameters.
		public static BloomFilter createAndPopulateBloomFilter(File inputFile) {
			long dC = getDataCount(inputFile);
			long fS = (long)Math.ceil((dC* Math.log(FALSE_POSITIVE_PROBABILITY)) / Math.log(1 / Math.pow(2, Math.log(2)))); //m = ceil((n * log(p)) / log(1 / pow(2, log(2))));
			long hC = Math.round( ((float)fS/dC) * Math.log(2)); //k = round((m / n) * log(2));

			System.out.println("\n----------------------------------------------------------------------------------------------------------");
			System.out.println("\nCalculated parameters for the Bloom Filter \n");
			System.out.println("\tNumber of items in the filter: " + Long.toString(dC));
			System.out.println("\n\tProbability of false positives: 0.0000001");
			System.out.println("\n\tSize of filter in bits: " + Long.toString(fS));
			System.out.println("\n\tNumber of hash functions: " + Long.toString(hC));
			System.out.println("\n----------------------------------------------------------------------------------------------------------");

			System.out.println("\nBloom Filter is being populated please wait...");

			BloomFilter bF = new BloomFilter(dC, fS, hC);//creates the bloom filter object

			//populates the bloom filter
			try {
				Scanner	data = new Scanner(inputFile);
				while(data.hasNextLine()){
					bF.addToFilter(data.nextLine());
				}
				System.out.println("Bloom filter has been populated with the data!");
				data.close();
			}
			catch(Exception e){
				System.out.println("An unexpected error has occurred:\n" + e);
			}

//		bF.printFilter();//debugging purposes
			return bF;
		}

		//Tests the bloom filter with the passed file.
		public static File testBloomFilter(File inputFile){
			File res = null;
			if(getDataCount(inputFile) == 0){
				System.out.println("Empty File provided!");
				return res;//if the file is empty it returns null TODO: make an edge case for this scenario
			}

			try {
				Scanner	data = new Scanner(inputFile);
				String parentDir = System.getProperty("user.dir");
				res = new File(parentDir,"Result.csv");
				FileWriter writer = new FileWriter(res);

				writer.write("Email,Result\n");//first line of the file

				while(data.hasNextLine()){
					String email = data.nextLine();
					if(email.compareTo("Email,Reult") == 0){//avoids the first line ^
						continue;
					}
					if(bF.isProbablyOnFilter(email)){
						writer.write(email + ",Probably in the DB\n");
					}
					else{
						writer.write(email + ",Not in the DB\n");
					}
				}

				data.close();
				writer.close();
				//finished populating the bloom filter
			}
			catch(Exception e){
				System.out.println("An unexpected error has occurred:\n" + e);
			}

			System.out.println("\n----------------------------------------------------------------------------------------------------------");
			System.out.println("Result saved in :" + res.getAbsolutePath());
			return res;
		}

		//Counts the data on the input file.
		public static long getDataCount(File inputFile) {
			Scanner data = null;
			long dataCount = 0;
			try {
				data = new Scanner(inputFile);
				while(data.hasNextLine()){
					if(data.nextLine().compareTo("Email") == 0){
						continue;
					}
					dataCount++;
				}
				data.close();
			}
			catch(Exception e){
				System.out.println("An unexpected error has occurred!");
			}

			return dataCount;
		}
	}//end of class Main



	public static class BloomFilter {
		long dataCount;
		long filterSize;
		long hashCount;
		int[] filter;
		int[] hashFunctionSeeds;

		//constructor
		public BloomFilter(long dataCount, long filterSize, long hashCount){
			this.dataCount = dataCount;
			this.filterSize = filterSize;
			this.hashCount = hashCount;
			filter = new int[(int)this.filterSize];
			hashFunctionSeeds = new int[(int)this.hashCount];

			prepareHashFunctionSeeds();
			prepareFilter();
		}

		//Prepares the filter by filling it with 0s
		public void prepareFilter(){
			for(int i = 0; i < filter.length;i++){
				filter[i] = 0;
			}
		}

		//Returns the position that will be set to 1 in the bloom filter.
		public int getHashPos(String dataToHash, int seed){
			int hashedFunction = Math.abs(dataToHash.hashCode()*seed);
			return hashedFunction % (int)filterSize;// % filter size so that the number doesn't go above the filter's sie
		}

		//Prepares the array of seeds that will be used with the hash function.
		public void prepareHashFunctionSeeds(){
			for(int i = 0;i < hashFunctionSeeds.length;i++) {
				hashFunctionSeeds[i] = (i+1) * 3;
			}
		}

		//Sets all the positions to 0
		public void addToFilter(String data){
			for(int i = 0;i < hashFunctionSeeds.length;i++) {
				int hashedDataPos = getHashPos(data,hashFunctionSeeds[i]);
				filter[hashedDataPos] = 1;
			}
		}

		//Goes through all of the hash functions and decides whether it could be in the Bloom Filter or is not
		public boolean isProbablyOnFilter(String data){
			for(int i = 0;i < hashFunctionSeeds.length;i++) {
				int hashedDataPos = getHashPos(data, hashFunctionSeeds[i]);
				if(filter[hashedDataPos] == 0){
					return false;
				}
			}
			return true;
		}

		//Prints the Bloom Filter, takes a long time if there's a lot of data.
		public void printFilter(){
			for(int bit: filter){
				System.out.println(bit);
			}
		}
	}//end of class BloomFilter

}//end of class BFContainer

