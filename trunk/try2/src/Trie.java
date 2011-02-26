import java.io.*;
import java.util.*;

public class Trie{
	private Node root_;
	private Stopwatch totalTime = new Stopwatch();
	private Stopwatch splitTime = new Stopwatch();
	private Stopwatch forLoopTime = new Stopwatch();
	private Stopwatch lookUpTime = new Stopwatch();

	public Trie(){
		root_ = new Node("");
	}
	
	public void insert(String str){
		totalTime.start();
		Node current = root_; 
		splitTime.start();
		String[] list = str.split(" ");
		splitTime.stop();
		if(list.length == 0) //For an empty character
			current.marker_=true;
		forLoopTime.start();
		for(int i=0;i<list.length;i++){
			++current.count_;
			lookUpTime.start();
			Node child = current.subNode(list[i]);
			lookUpTime.stop();
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
		forLoopTime.stop();
		totalTime.stop();
	}
	
	public void printTimeAnalysis () {
		System.out.println("Total time spent in insert function is: " + totalTime.getElapsedTime());
		System.out.println("Total time spent in splitting is: " + splitTime.getElapsedTime());
		System.out.println("Total time spent in for loop is: " + forLoopTime.getElapsedTime());
		System.out.println("Total time spent in lookup is: " + lookUpTime.getElapsedTime());
	}
	public void checkChildCount() {
		root_.checkChildCount();
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

	public void genNgram(int n) {
		String str = "";
		Random randomGenerator = new Random();
		for (int i =0; i < 10; i++) {
			int randomInt = randomGenerator.nextInt((int)root_.count_);
			str += " " + root_.giveNthChildWord(randomInt + 1);
		}
		System.out.println(str);
	}
}