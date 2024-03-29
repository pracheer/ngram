import java.util.*;
import java.io.*;

public class Node {
	String content_;
	boolean marker_; 
	long count_;
	HashMap<String,Node> children_;

	public Node(String str){
		children_ = new HashMap<String,Node>();
		marker_ = false;
		content_ = str;
		count_ = 0;
	}

	public Node subNode(String str){
		if(children_!=null){
			return children_.get(str);
		}
		return null;
	}

	public void print(Writer out, String gen) throws IOException{
		if(children_!=null){
			Iterator<String> iterator = children_.keySet().iterator();
			while(iterator.hasNext()) {
				Node child = children_.get(iterator.next());
				child.print( out, gen + content_ + " ");
			}
		} 
		if(marker_ == true) {
			out.write(gen + content_ + " " + Long.toString(count_)+"\n");
		}
	}
	
	public long giveVocabCount(){
		if(children_ !=null)
			return children_.keySet().size();
		else
			return 0;
	}
	
	public String giveNthChildWord(long N) {
		long childCount = 0;
		Iterator<String> iterator = children_.keySet().iterator();
		if(children_ == null)
			return "";
		while(iterator.hasNext()) {
			Node child = children_.get(iterator.next());
			childCount += child.count_;
			if (childCount >= N)
				return child.content_;
		}
		return "";	
	}
}