import java.io.*;
import java.util.Scanner;

public class Ngram {
	public static Trie trie_;
	final public int N;
	
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
	
	public static void main(String[] args) {
		Ngram ngram_ = new Ngram(2);
		ngram_.loadfile("src/words2.txt");
		System.err.println("Done 1");
		//ngram_.loadfile("src/fbis.test");
		System.err.println("Done 2");
		//trie_.print("src/out.txt");
		trie_.printTimeAnalysis();
		trie_.checkChildCount();
		ngram_.genSentence(1);
	}
	
}

