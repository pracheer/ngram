import java.io.*;
import java.util.Scanner;

public class Ngram {
	public static Trie trie_;
	final public int N;
	
	public Ngram(int n) {
		trie_ = new Trie();
		N = n;
	}
	
	public String[] strToList(String str) {
		String[] list = str.split(" ");
		if (list.length != this.N) {
			System.err.println("EXITING");
			System.exit(0);
		}
		return list;
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
					strToList(wordbuf);
					trie_.insert(wordbuf);
				} else if (nWord == 1) {
					wordbuf = word;
				} else if (nWord < N) {
					wordbuf = wordbuf + " " + word;
				} else if (nWord == N) {
					wordbuf = wordbuf + " " + word;
					strToList(wordbuf);
					trie_.insert(wordbuf);
				} else {
					int idx = wordbuf.indexOf(' ');
					String substr = wordbuf.substring(idx+1); 
					wordbuf = substr + " " + word;
					strToList(wordbuf);
					trie_.insert(wordbuf);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Ngram ngram_ = new Ngram(2);
		ngram_.loadfile("src/words2.txt");
		trie_.print();
	}
}

