import java.util.*;
//import java.util.HashMap;
//import java.util.Iterator;

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
			Iterator<String> iterator = children_.keySet().iterator();
			while(iterator.hasNext()) {//Node eachChild:children_){
				Node child = children_.get(iterator.next());
				if(child.content_.equals(str)){
					return child;
				}
			}
		}
		return null;
	}

	public void print(String gen){
		if(children_!=null){
			Iterator<String> iterator = children_.keySet().iterator();
			while(iterator.hasNext()) {//Node eachChild:children_){
				Node child = children_.get(iterator.next());
				child.print( gen + content_);
			}
		} 
		if(marker_ == true) {
			System.out.println(gen + content_ + " " + Long.toString(count_));
		}
	}
}