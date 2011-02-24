public class Trie{
	private Node root_;

	public Trie(){
		root_ = new Node('$'); 
	}

	public void insert(String str){
		Node current = root_; 
		if(str.length()==0) //For an empty character
			current.marker_=true;
		for(int i=0;i<str.length();i++){
			++current.count_;
			Node child = current.subNode(str.charAt(i));
			if(child!=null){ 
				current = child;
			}
			else{
				current.children_.add(new Node(str.charAt(i)));
				current = current.subNode(str.charAt(i));
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
				if(current.subNode(str.charAt(i)) == null)
					return false;
				else
					current = current.subNode(str.charAt(i));
			}
			/* 
			 * This means that a string exists, but make sure its
			 * a word by checking its 'marker' flag
			 */
			if (current.marker_ == true)
				return true;
			else
				return false;
		}
		return false; 
	}

	public void print(){
		root_.print("");
	}
}