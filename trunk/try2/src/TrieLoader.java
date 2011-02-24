import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
// TODO: 1.) print  2.) add count  3.) hashmap  4.) char to string
public class TrieLoader {

	public static Trie trieDSA;

	public static void main(String[] args) {
		TrieLoader trieLoader = new TrieLoader();
		trieLoader.load();
		//new TrieTestFrame();
	}

	public void load(){
		trieDSA = new Trie();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File("src/words2.txt")));
			String eachLine = null;
			while((eachLine=br.readLine())!=null){
				trieDSA.insert(eachLine);
			}
			trieDSA.insert("AA");
			trieDSA.insert("a");
			trieDSA.insert("");

		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			trieDSA.print();
			System.out.println("DONE");
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