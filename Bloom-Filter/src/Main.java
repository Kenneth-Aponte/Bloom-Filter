import java.io.File;
import java.util.Scanner;

/**
 * @author Kenneth-Aponte
 *
 */

public class Main {
	public static BloomFilter bF;

	public static void main(String[] args) {
		File inputFile = getInputFile();
		bF = createAndPopulateBloomFilter(inputFile);

	}

	public static File getInputFile() {
		Scanner input = new Scanner(System.in);
		File iF = null;
		boolean validFile = false;

		System.out.println("\n----------------------------------------------------------------------------------------------------------");
		System.out.print("\nPlease input the path to the input file: "); //TODO: remove this: /Users/kennethaponte/Downloads/emails.csv

		while(!validFile){
				iF = new File(input.nextLine());
				if(!iF.exists()){
					System.out.print("Please enter a valid file path: ");
					continue;
				}
			validFile = true;
		}

		input.close();
		return iF;
	}


	public static BloomFilter createAndPopulateBloomFilter(File inputFile) {
		long dC = getDataCount(inputFile);
		long fS = (long)Math.ceil((dC* Math.log(0.0000001)) / Math.log(1 / Math.pow(2, Math.log(2)))); //m = ceil((n * log(p)) / log(1 / pow(2, log(2))));
		long hC = Math.round( (fS/dC) * Math.log(2)); //k = round((m / n) * log(2));

		BloomFilter bF =  new BloomFilter(dC,fS,hC);

		System.out.println("\n----------------------------------------------------------------------------------------------------------");
		System.out.println("\n\nCalculated parameters for the Bloom Filter \n");
		System.out.println("Number of items in the filter: " + Long.toString(dC));
		System.out.println("\nProbability of false positives: 0.0000001");
		System.out.println("\nSize of filter in bits: " + Long.toString(fS));
		System.out.println("\nNumber of hash functions: " + Long.toString(hC));
		System.out.println("\n----------------------------------------------------------------------------------------------------------");

		System.out.println("\nBloom Filter is being populated please wait...");

		//TODO: populate the bloom filter

		return bF;
	}


	public static long getDataCount(File inputFile) {
		Scanner data = null;
		try {
			data = new Scanner(inputFile);
		}
		catch(Exception e){
			System.out.println("An unexpected error has occurred!");
		}

		long dataCount = 0;
		while(data.hasNextLine()){
			if(data.nextLine().compareTo("Email") == 0){
				continue;
			}
			dataCount++;
		}
		return dataCount;
	}
}
