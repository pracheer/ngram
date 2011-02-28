import java.io.*;
import java.util.*;

public class Trie{
	private Node root_;
	
	private TreeMap <Long,Long> unigramTuringMap = null;
	private TreeMap <Long, Long> bigramTuringMap = null;
//	private Stopwatch totalTime = new Stopwatch();
//	private Stopwatch splitTime = new Stopwatch();
//	private Stopwatch forLoopTime = new Stopwatch();
//	private Stopwatch lookUpTime = new Stopwatch();

	public Trie(){
		root_ = new Node("");
	}
	
	public void insert(String str){
//		totalTime.start();
		Node current = root_; 
//		splitTime.start();
		String[] list = str.split(" ");
//		splitTime.stop();
		if(list.length == 0) //For an empty character
			current.marker_=true;
//		forLoopTime.start();
		for(int i=0;i<list.length;i++){
			++current.count_;
//			lookUpTime.start();
			Node child = current.subNode(list[i]);
//			lookUpTime.stop();
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
//		forLoopTime.stop();
//		totalTime.stop();
	}
	
	private void updateTuringMapCount (Node node, TreeMap<Long,Long> map) {
		if (map.containsKey(node.count_)){
			Long count = map.get(node.count_);
			map.put(node.count_, count+1);
		} else {
			map.put(node.count_, (long)1);
		}
	}
	private void createUnigramTuringMap () {
		if (unigramTuringMap != null)
			return;
		if (root_.children_ == null)
			return;
		unigramTuringMap = new TreeMap<Long, Long>();
		Collection<Node> uniGrams = root_.children_.values();
		for (Node unigram:uniGrams) {
			updateTuringMapCount(unigram, unigramTuringMap);
		}
	}
	private void createBigramTuringMap () {
		if (bigramTuringMap != null)
			return;
		if(root_.children_ ==null)
			return;
		bigramTuringMap = new TreeMap<Long, Long>();
		Collection<Node> uniGrams = root_.children_.values();
		for (Node unigram:uniGrams) {
			Collection<Node> biGrams = unigram.children_.values();
			for (Node bigram:biGrams) {				
				updateTuringMapCount(bigram, bigramTuringMap);
			}
		}
	}
	public double perplexityGram (String pred, String succ, int smoothingAlgo, double lambda) {
		double val = 0;
		if (this.search(pred)== null)
			pred = "</UNK>";
		if (this.search(succ)== null)
			succ = "</UNK>";
		double predCount = this.giveCount(pred);
		double jointCount = this.giveCount(pred + " " + succ);
		double vocabCount = root_.giveVocabCount();
		if (predCount <= 0) {
			System.err.println("Problem with perplexity");
			System.exit(0);
		}
		switch (smoothingAlgo){
		case 1:
			// Add one smoothing
			jointCount = jointCount + 1;
			predCount = predCount + vocabCount;
			break;
		case 2:
			// Add lambda smoothing
			jointCount = jointCount + lambda;
			predCount = predCount + lambda*vocabCount;
			break;
		case 3:
			// Good turing smoothing
			//  create unigram and bigrams good turing table
			createUnigramTuringMap();
			createBigramTuringMap();
			break;
			
		default:
			System.err.println("Problem with switchcase");
			System.exit(0);
		}
		val = jointCount/predCount;
		val = Math.log10(val);
		return val;
	}
	public void printTimeAnalysis () {
//		System.out.println("Total time spent in insert function is: " + totalTime.getElapsedTime());
//		System.out.println("Total time spent in splitting is: " + splitTime.getElapsedTime());
//		System.out.println("Total time spent in for loop is: " + forLoopTime.getElapsedTime());
//		System.out.println("Total time spent in lookup is: " + lookUpTime.getElapsedTime());
	}
	public void checkChildCount() {
		root_.checkChildCount();
	}

	public Node search(String str){
		Node current = root_;
		if(str.equals(""))
			return root_;
		
		String[] list = str.trim().split(" ");
		
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
		return 0; 
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

	public String genSuccWord(String predecessor) {
		String ret = "";
		Node currNode = this.search(predecessor);
		if(currNode == null) {
			currNode = root_;
		}
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt((int)currNode.count_);
		ret = currNode.giveNthChildWord(randomInt + 1);
		return ret;
	}
}