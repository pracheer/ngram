import java.util.Collection;
import java.util.LinkedList;

public class Node {
	String content_;
	boolean marker_; 
	int count_;
	Collection<Node> children_;

	public Node(String str){
		children_ = new LinkedList<Node>();
		marker_ = false;
		content_ = str;
		count_ = 0;
	}

	public Node subNode(String str){
		if(children_!=null){
			for(Node eachChild:children_){
				if(eachChild.content_.equals(str)){
					return eachChild;
				}
			}
		}
		return null;
	}

	public void print(String gen){
		if(children_!=null){
			for(Node eachChild:children_){
				eachChild.print( gen + content_);
			}
		} 
		if(marker_ == true) {
			System.out.println(gen + content_ + " " + Integer.toString(count_));
		}
	}
}