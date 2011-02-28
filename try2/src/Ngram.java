import java.io.*;
import java.util.*;

public class Ngram {
	private static Trie trie_;
	final private int N;
	
	private String lastInserted_ ;
	private HashMap<String, Boolean> seen_;
	
	Stopwatch a = new Stopwatch();
	Stopwatch b = new Stopwatch();
	Stopwatch c = new Stopwatch();
	Stopwatch d = new Stopwatch();
	
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
	
	public void loadfile(String filename, Boolean allowUnknown) {
		try {
			Scanner in;
			in = new Scanner(new File(filename));
			while(in.hasNext("\\S+")) {
				String word = in.next("\\S+");
				this.insert(word, allowUnknown);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
	}

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
				predecessor += predecessor + " " + trie_.genSuccWord(predecessor);
			}
		}
		// generate next word and update predecessor
		while(!nextWord.equals("</S>")) {
			nextWord = trie_.genSuccWord(predecessor);
			if (nextWord.equals("</S>")||nextWord.equals("<S>"))
				continue;
			sentence += " " + nextWord;
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
	
	public void trainFile (String filename, String newFilename, Boolean allowUnknown ) {
		String tokenList = null;
		Scanner in;
		Writer out;
		try {
			in = new Scanner(new File(filename));
			out = new BufferedWriter(new FileWriter(new File(newFilename)));
			while(in.hasNext("\\S+")) {
				a.start();
				String word = in.next("\\S+");
				a.stop();
				b.start();
				tokenList = new Tokenizer(word).tokenize();
				b.stop();
				out.write(tokenList);
				c.start();
				insert(tokenList, allowUnknown);
				c.stop();
			}
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public double perplexityFile (String filename, int modelN, int smoothingAlgo, Double lambda ) {
		String tokenList = null;
		String pred = "";
		String succ = "";
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
						denomAcc += trie_.perplexityGram(pred, succ, smoothingAlgo, lambda);
					} else if (modelN == 2){
						pred = succ;
						succ = token;
						denomAcc += trie_.perplexityGram(pred, succ, smoothingAlgo, lambda);
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
		System.out.println("\nTotal time spent in pasrsing is: " + a.getElapsedTime());
		System.out.println("Total time spent in tokenizing is: " + b.getElapsedTime());
		System.out.println("Total time spent in insert func call is: " + c.getElapsedTime());
	}
	public static void main(String[] args) {
		Ngram ngram_ = new Ngram(2);
		int smoothingAlgo = 2;
		int perplexityModelN = 1;
		double lambda = 1;
		double perplexity;
		Boolean allowUnknown = true;
		ngram_.trainFile("test/test.txt", "test/tokens.txt", true);
		ngram_.trainFile("test/sample.txt", "test/tokens.txt", true);
		ngram_.trainFile("test/fbis_full.train", "test/tokens.txt", true);
		
		perplexity = ngram_.perplexityFile("test/sample.txt", perplexityModelN, smoothingAlgo, lambda);
		
		System.out.println("Perplexity is: " + perplexity);
		
		trie_.print("test/trie.txt");
		trie_.printTimeAnalysis();
		ngram_.printTimeAnalysis();
//		Tokenizer.printTimeAnalysis();
		//trie_.checkChildCount();
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

