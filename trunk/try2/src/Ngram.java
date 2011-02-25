import java.io.*;
import java.util.Scanner;

public class Ngram {

	public static Trie trie_;
	final public int N = 1;

	public static void main(String[] args) {
		Ngram trieLoader = new Ngram();
		trieLoader.load();
		//String str = "abc def .";
		//String[] arr = str.split(" ");
		//for (int i = 0; i< arr.length; i++) 
		//	System.out.println(arr[i]);
		trieLoader.loadfile("src/words2.txt");
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
				} else if (nWord == 1) {
					wordbuf = word;
				} else if (nWord < N) {
					wordbuf = wordbuf + " " + word;
				} else if (nWord == N) {
					wordbuf = wordbuf + " " + word;
				} else {
					int idx = wordbuf.indexOf(' ');
					String substr = wordbuf.substring(idx+1); 
					wordbuf = substr + " " + word;
				}
				System.out.println(wordbuf);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println(nWord);
	}
	public void load(){
		trie_ = new Trie();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File("src/words2.txt")));
			String eachLine = null;
			while((eachLine=br.readLine())!=null){
				trie_.insert(eachLine);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			trie_.print();
			System.out.println("DONE");
			System.out.println("Count " + "AA :" + trie_.giveCount("AA") );
			System.out.println("Count " + "A :" + trie_.giveCount("A") );
			System.out.println("Count " + "aa :" + trie_.giveCount("aa") );
			System.out.println("Count " + "Aar :" + trie_.giveCount("Aar") );
			System.out.println("Count " + " :" + trie_.giveCount("") );
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}


//	public void loadfile2(String filename) {
//		StreamTokenizer st = null;
//		int wordCount = 0;
//		String wordBuff = "";
//		try {
//			st = new StreamTokenizer(new FileReader(filename));
//			st.wordChars('\'', '\'');
//			st.wordChars('`', '`');
//			st.wordChars('1', '9');
//			st.whitespaceChars(' ', ' ');
//			st.wordChars('(', ')');
//			st.wordChars('<', '>');
//			st.wordChars('?', '?');
//			st.wordChars('/', '/');
//			st.wordChars('.', '.');
//			while (st.nextToken() != StreamTokenizer.TT_EOF) {
//				String nextToken = null;				
//				if(st.ttype == StreamTokenizer.TT_WORD)
//					nextToken = st.sval;
//				else if (st.ttype == StreamTokenizer.TT_NUMBER)
//					nextToken = Integer.toString((int)st.nval);
//				else					
//					continue;				
//				wordCount++;
//				System.out.println(nextToken);
//				
//			System.out.println(wordBuff);
//			System.out.println("Number of words in file: " + wordCount);
//		} catch (FileNotFoundException ex){
//			ex.printStackTrace();
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		}
//	}
//	