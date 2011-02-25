import java.io.*;

public class Trie{
	private Node root_;

	public Trie(){
		root_ = new Node("$Root$"); 
	}
	
	public void insert(String str){
		Node current = root_; 
		String[] list = str.split(" "); 
		if(list.length == 0) //For an empty character
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

	public Node search(String str){
		Node current = root_;
		String[] list = str.split(" ");
		while(current != null){
			for(int i=0;i<list.length;i++){    
				if(current.subNode(list[i]) == null)
					return null;
				else
					current = current.subNode(list[i]);
			}
			return current;
		}
		return null; 
	}

	public long giveCount(String str){
		Node node = search(str);
		if(node != null){
			return node.count_;
		}
		return -1; 
	}

	public void print(String filename){
		File file = new File(filename);
	    Writer output;
		try {
			output = new BufferedWriter(new FileWriter(file));
			root_.print(output, "");
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}    
	}
}