import java.io.*;
import java.util.Scanner;

public class Ngram {
	private static Trie trie_;
	final private int N;
	
	public Ngram(int n) {
		trie_ = new Trie();
		N = n;
	}
	//TODO: remove check before submission
	public void checkLength(String str) {
		String[] list = str.split(" ");
		if (list.length != this.N) {
			System.err.println("EXITING");
			System.exit(0);
		}
	}
	
	public void loadfile(String filename) {
		Scanner in;
		int nWord = 0;
		String wordbuf = "";
		try {
			in = new Scanner(new File(filename));
			while(in.hasNext("\\S+")) {
				String word = in.next("\\S+");
				nWord++;
				if (N == 1){
					wordbuf = word;
					checkLength(wordbuf);
					trie_.insert(wordbuf);
				} else if (nWord == 1) {
					wordbuf = word;
				} else if (nWord < N) {
					wordbuf = wordbuf + " " + word;
				} else if (nWord == N) {
					wordbuf = wordbuf + " " + word;
					checkLength(wordbuf);
					trie_.insert(wordbuf);
				} else {
					int idx = wordbuf.indexOf(' ');
					String substr = wordbuf.substring(idx+1); 
					wordbuf = substr + " " + word;
					checkLength(wordbuf);
					trie_.insert(wordbuf);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public void genSentence (int n) {
		/* n = 1 => Unigrams, n=2 => bigrams and so on */
		if (n>N) {
			System.err.println("Ngram model does not have enough information, exiting...");
			return;
		}
		trie_.genNgram(n);
	}
	
	public void preprocessFile (String filename, String newFilename) {
		String[] tokenList = null;
		Scanner in;
		Writer out;
		try {
			in = new Scanner(new File(filename));
			out = new BufferedWriter(new FileWriter(new File(newFilename)));
			while(in.hasNext("\\S+")) {
				String word = in.next("\\S+");
				tokenList = new Tokenizer(word).tokenize();
				for( int i = 0; i< tokenList.length; i++) {
					out.write(tokenList[i]+ " ");
				}
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Ngram ngram_ = new Ngram(2);
		
		ngram_.preprocessFile("test/sample.txt", "test/tokens.txt");
		ngram_.preprocessFile("test/test.txt", "test/tokens.txt");
//		ngram_.preprocessFile("test/fbis.train", "test/tokens.txt");
		
		ngram_.loadfile("test/tokens.txt");
		//ngram_.loadfile("src/fbis.test");
		trie_.print("test/out.txt");
		trie_.printTimeAnalysis();
		trie_.checkChildCount();
		//ngram_.genSentence(1);
		
		
//		String str = "(we) 'got' hud'nt 18.5 would've don't; 18?. i- saw 23. 23.0";
//		Tokenizer et = new Tokenizer(str);
//		et.tokenize();
//		et.printTokens();
	}
	
}

