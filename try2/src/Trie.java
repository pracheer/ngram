public class Trie{
	private Node root_;

	public Trie(){
		root_ = new Node("$Root$"); 
	}
	
	public void insert(String str){
		Node current = root_; 
		String[] list = str.split(" "); 
		if(str.length()==0) //For an empty character
			current.marker_=true;
		for(int i=0;i<list.length;i++){
			++current.count_;
			Node child = current.subNode(list[i]);
			if(child!=null){ 
				current = child;
			}
			else{
				current.children_.put(list[i], new Node(list[i]));
				current = current.subNode(list[i]);
			}
			// Set marker to indicate end of the word
			if(i==list.length-1) {
				current.marker_ = true;
				++current.count_;
			}
		} 
	}

	public boolean search(String str){
		Node current = root_;
		String[] list = str.split(" ");
		while(current != null){
			for(int i=0;i<list.length;i++){    
				if(current.subNode(list[i]) == null)
					return false;
				else
					current = current.subNode(list[i]);
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
		String[] list = str.split(" ");
		while(current != null){
			for(int i=0;i<list.length;i++){    
				if(current.subNode(list[i]) == null)
					return -1;
				else
					current = current.subNode(list[i]);
			}
			return current.count_;
		}
		return -1; 
	}

	public void print(){
		root_.print("");
	}
}