public class Trie{
	private Node root_;

	public Trie(){
		root_ = new Node("$Root$ "); 
	}

	public void insert(String str){
		Node current = root_; 
		if(str.length()==0) //For an empty character
			current.marker_=true;
		for(int i=0;i<str.length();i++){
			++current.count_;
			Node child = current.subNode(str.substring(i,i+1));
			if(child!=null){ 
				current = child;
			}
			else{
				current.children_.put(str.substring(i, i+1), new Node(str.substring(i,i+1)));
				current = current.subNode(str.substring(i,i+1));
			}
			// Set marker to indicate end of the word
			if(i==str.length()-1) {
				current.marker_ = true;
				++current.count_;
			}
		} 
	}

	public boolean search(String str){
		Node current = root_;
		while(current != null){
			for(int i=0;i<str.length();i++){    
				if(current.subNode(str.substring(i,i+1)) == null)
					return false;
				else
					current = current.subNode(str.substring(i,i+1));
			}
			
			if (current.marker_ == true)
				return true;
			else
				return false;
		}
		return false; 
	}
	
	public long giveCount(String str){
		Node current = root_;
		while(current != null){
			for(int i=0;i<str.length();i++){    
				if(current.subNode(str.substring(i,i+1)) == null)
					return -1;
				else
					current = current.subNode(str.substring(i,i+1));
			}
			return current.count_;
		}
		return -1; 
	}

	public void print(){
		root_.print("");
	}
}