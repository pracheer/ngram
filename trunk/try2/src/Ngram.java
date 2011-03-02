import java.io.*;
import java.util.*;

public class Ngram {
	private static Trie trie_;
	final private int N;
	
	private String lastInserted_ ;
	private HashMap<String, Boolean> seen_;
	
	Stopwatch watch1 = new Stopwatch();
	Stopwatch watch2 = new Stopwatch();
	Stopwatch watch3 = new Stopwatch();
	Stopwatch watch4 = new Stopwatch();
	
	public Ngram(int n) {
		trie_ = new Trie();
		N = n;
		seen_ = new HashMap<String, Boolean>(100000);
		
		lastInserted_ = "<S>";
		for (int i = 0; i<N-1; i++) {
			lastInserted_ += " <S>";
		}
	}
	public void checkLength(String str) {
		String[] list = str.split(" ");
		if (list.length != this.N) {
			System.err.println("EXITING");
			System.exit(0);
		}
	}
//	public void loadfile(String filename, Boolean allowUnknown) {
//		try {
//			Scanner in;
//			in = new Scanner(new File(filename));
//			while(in.hasNext("\\S+")) {
//				String word = in.next("\\S+");
//				this.insert(word, allowUnknown);
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}	
//	}

	public void insert(String list, Boolean allowUnknown) {
		String[] words = list.trim().split(" ");
		for(String word:words){
			if(word.equals(""))
				continue;
			if(allowUnknown && !seen_.containsKey(word)) {
				seen_.put(word, true);
				word = "</UNK>";
			}
			if (N == 1){
				lastInserted_ = word;
				checkLength(lastInserted_);
				trie_.insert(lastInserted_);
			} else {
				int idx = lastInserted_.indexOf(' ');
				String substr = lastInserted_.substring(idx+1); 
				lastInserted_ = substr + " " + word;
				checkLength(lastInserted_);
				trie_.insert(lastInserted_);
			}
		}
	}
	public String genSentence (int n) {
		/* n = 1 => Unigrams, n=2 => bigrams and so on */
		String sentence = "";
		String predecessor = "";
		String nextWord = "";
		if (n>N || n<= 0) {
			System.err.println("Ngram model does not have enough information, exiting...");
			return null;
		}
		//Initialise predecessor
		for (int i = 0; i<n-1; i++) {
			if(i==0){
				predecessor += "<S>";
			} else {
				String next = trie_.genSuccWord(predecessor);
				predecessor += predecessor + " " + next;
				sentence += next + " ";
			}
		}
		// generate next word and update predecessor
		while(!nextWord.equals("</S>")) {
			nextWord = trie_.genSuccWord(predecessor);
			if (nextWord.equals("</S>")||nextWord.equals("<S>"))
				continue;
			sentence += nextWord + " ";
			if (n==1) { //update predecessor
				predecessor = "";
			} else if (n==2) {
				predecessor = nextWord;
			} else if(n>2) { 
				predecessor = predecessor.trim();
				int idx = predecessor.indexOf(' ');
				String substr = predecessor.substring(idx+1); 
				predecessor = substr + " " + nextWord;
			}
		}
		return sentence;
	}
	
	public void trainFile (String filename, String tokenFile, Boolean allowUnknown ) {
		String tokenList = null;
		Scanner in;
		Writer out;
		try {
			in = new Scanner(new File(filename));
			out = new BufferedWriter(new FileWriter(new File(tokenFile)));
			while(in.hasNext("\\S+")) {
				watch1.start();
				String word = in.next("\\S+");
				watch1.stop();
				watch2.start();
				tokenList = new Tokenizer(word).tokenize();
				watch2.stop();
				out.write(tokenList);
				watch3.start();
				insert(tokenList, allowUnknown);
				watch3.stop();
			}
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public double perplexityFile (String filename, int modelN, int smoothingAlgo, Double lambda, int variant ) {
		String tokenList = null;
		String pred = "<S>";
		String succ = "<S>";
		double denomAcc = 0;
		double nTokens = 0;
		Scanner in;
		try {
			in = new Scanner(new File(filename));
			while(in.hasNext("\\S+")) {
				String word = in.next("\\S+");
				tokenList = new Tokenizer(word).tokenize();
				String[] tokens = tokenList.trim().split(" ");
				for(String token:tokens){
					if(token.equals(""))
						continue;
					nTokens++;
					if (modelN == 1) {
						pred = "";
						succ = token;
						denomAcc += trie_.perplexityGram(pred, succ, smoothingAlgo, lambda, variant);
					} else if (modelN == 2){
						pred = succ;
						succ = token;
						denomAcc += trie_.perplexityGram(pred, succ, smoothingAlgo, lambda, variant);
					} else {
						System.err.println("Perp not supported");
					}					
				}
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		denomAcc = denomAcc/nTokens;
		return (Math.pow(10, -denomAcc));
	}
	public void printTimeAnalysis () {
		System.out.println("\nTotal time spent in pasrsing is: " + watch1.getElapsedTime());
		System.out.println("Total time spent in tokenizing is: " + watch2.getElapsedTime());
		System.out.println("Total time spent in insert func call is: " + watch3.getElapsedTime());
	}
	public void menu(Ngram ngram_) {
		while(true) {
			int opt = 1;
			switch (opt){
			case 1:
				System.out.println("Unigram:");
				System.out.println(ngram_.genSentence(1));
				System.out.println("Bigram:");
				System.out.println(ngram_.genSentence(2));
				System.out.println("Trigram:");
				System.out.println(ngram_.genSentence(3));
				break;
			case 2:
				
			}
		}
	}
	public static void  perpExperimentLambda(Ngram ngram_, String validationfile, String testfile) {
		int smoothingAlgo = 2;
		int modelN;
//		double[] lambda = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1};
		double[] lambda = { 0.2, 0.4, 0.6, 0.8, 1};
		double[] perplexityVal = new double[lambda.length];
		double[] perplexityTest = new double[lambda.length];
		
		System.out.println("********* Bigram Perplexity Lambda Experiment ******");
		System.out.println("Validation File:" + validationfile );
		System.out.println("Test File:" + testfile );
		System.out.println("Lambda : Perplexity_Validation : Perplexity_test ");
		modelN = 2;
		for(int i = 0 ; i<lambda.length; i++) {
			perplexityTest[i] = ngram_.perplexityFile(testfile, 
									modelN, smoothingAlgo, lambda[i],1);
			perplexityVal[i] = ngram_.perplexityFile(validationfile, 
					modelN, smoothingAlgo, lambda[i],1);
		}
		for(int i = 0 ; i<lambda.length; i++) {
			System.out.println(lambda[i] + " : " 
							+ perplexityVal[i] + " : "
							+ perplexityTest[i] );
		}
		System.out.println("********* END Bigram Perplexity Lambda Experiment ******");
		
		System.out.println("********* Unigram Perplexity Lambda Experiment ******");
		System.out.println("Validation File:" + validationfile );
		System.out.println("Test File:" + testfile );
		System.out.println("Lambda : Perplexity_Validation : Perplexity_test ");
		modelN = 1;
		for(int i = 0 ; i<lambda.length; i++) {
			perplexityTest[i] = ngram_.perplexityFile(testfile, 
									modelN, smoothingAlgo, lambda[i],1);
			perplexityVal[i] = ngram_.perplexityFile(validationfile, 
					modelN, smoothingAlgo, lambda[i],1);
		}
		for(int i = 0 ; i<lambda.length; i++) {
			System.out.println(lambda[i] + " : " 
							+ perplexityVal[i] + " : "
							+ perplexityTest[i] );
		}
		System.out.println("********* END Unigram Perplexity Lambda Experiment ******");
	}
	public static void  perpExperimentTuring(Ngram ngram_, String validationfile, String testfile) {
		int smoothingAlgo = 3;
		int modelN = 2;
		double lambda = 1;
		double perplexityVal = 0;
		double perplexityTest = 0;
		
		System.out.println("********* START Bigram Perplexity Good Turing Experiment ******");
		System.out.println("Validation File:" + validationfile );
		System.out.println("Test File:" + testfile );
		perplexityTest = ngram_.perplexityFile(testfile, 
				modelN, smoothingAlgo, lambda, 1);
		perplexityVal = ngram_.perplexityFile(validationfile, 
				modelN, smoothingAlgo, lambda, 1);
		System.out.println("Perp Val file(Variant 1): " + perplexityVal);
		System.out.println("Perp Test file(Variant 1): " + perplexityTest);
		
		perplexityTest = ngram_.perplexityFile(testfile, 
				modelN, smoothingAlgo, lambda, 2);
		perplexityVal = ngram_.perplexityFile(validationfile, 
				modelN, smoothingAlgo, lambda, 2);
		System.out.println("Perp Val file(Variant 2): " + perplexityVal);
		System.out.println("Perp Test file(Variant 2): " + perplexityTest);
		
		System.out.println("********* END Bigram Perplexity Good Turing Experiment ******");		
	}
	
	public static void main(String[] args) {
		int modelN = 2;
		Boolean allowUnknown = false;
		String trainfile = "test/fbis.train";
		String validationfile = "test/fbis.validation";
		String testfile = "test/fbis.test";
		String turingfile = "test/turing.txt";
		
		try {
			String thisLine;
			BufferedReader br;
			br = new BufferedReader(new FileReader (new File("options.txt")));
			while ((thisLine = br.readLine()) != null) {
				if (thisLine.startsWith("#") || thisLine.equals(""))
					continue;
				String[] comp = thisLine.trim().split("\\s+");
				if (comp.length != 2) {
					System.err.println("Incorrect options file format. Exiting ...");
					System.exit(0);
				}
				if (comp[0].equals("Train_File")) {
					trainfile = comp[1];
				} else if (comp[0].equals("Test_File")) {
					testfile = comp[1];
				} else if (comp[0].equals("Val_File")) { 
					validationfile = comp[1];
				} else if (comp[0].equals("Count_File")) { 
					turingfile = comp[1];
				} else if (comp[0].equals("Model_N")) {
					modelN = Integer.parseInt(comp[1]);
				} else if (comp[0].equals("Perplexity_Test")) {
					allowUnknown = comp[1].equals("yes") ? true :false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Ngram ngram_ = new Ngram(modelN);
		ngram_.trainFile(trainfile, "test/tokens.txt", allowUnknown);
		
		if (allowUnknown){
			perpExperimentLambda(ngram_,validationfile,testfile );
			perpExperimentTuring(ngram_,validationfile,testfile );
			trie_.printTuringMaps(turingfile);
		}
//		trie_.print("test/trie.txt");
//		ngram_.printTimeAnalysis();
		if (!allowUnknown) {
			for (int i = 0; i<10; i++) {
				System.out.println("Unigram:");
				System.out.println(ngram_.genSentence(1));
				System.out.println("Bigram:");
				System.out.println(ngram_.genSentence(2));
				System.out.println("Trigram:");
				System.out.println(ngram_.genSentence(3));
			}
		}
	}
	
}

