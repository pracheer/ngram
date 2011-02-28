import java.util.ArrayList;  
import java.util.Arrays;  
import java.util.List;  
import java.util.regex.Pattern;  
import java.util.regex.Matcher;  
 
public class Tokenizer {  
    private String str;  
    private String tokenList;  
  

//	private static Stopwatch a = new Stopwatch();
//	private static Stopwatch b = new Stopwatch();
//	private static Stopwatch c = new Stopwatch();
//	private static Stopwatch d = new Stopwatch();
//	
    private static final String regexLetterNumber = "[a-zA-Z0-9]";  
//    private static final String regexNotLetterNumber = "([^a-zA-Z0-9])";  
    private static final String regexSeparator = "([\\?!(),\";/\\|`])";  
    private static final String regexClitics = "(:|-|'S|'D|'M|'LL|'RE|'VE|N'T|'s|'d|'m|'ll|'re|'ve|n't)";  
    private static final List<String> abbrList =  
        Arrays.asList("Co.", "Corp.", "vs.", "e.g.", "etc.", "ex.", "cf.",  
            "eg.", "est.", "Dept.", "Mr.", "Jr.", "Ms.", "Mrs.", "Dr.",  
            "Ph.D.","U.S.", "U.K.","Ltd.", "A.M.", "i.e.", "...");  
    
    private static Pattern p1 = Pattern.compile(".*" + regexLetterNumber + "\\.");  
    private static Pattern p2 = Pattern.compile("^([A-Za-z]\\.([A-Za-z]\\.)+|[A-Z][bcdfghj-nptvxz]+\\.)$");  
    
    public Tokenizer(String str) {  
        this.str = str;  
        tokenList = "";  
    }  
  
    public String tokenize() {  
//    	a.start();
//        str = str.replaceAll("\\t", " "); // Changes tabs into spaces.  
        str = str.replaceAll("<.*>", "");
        str = str.replaceAll(regexSeparator , " $1 "); // Puts blanks around unambiguous separators.  
        // Puts blanks around commas that are not inside numbers.  
        //str = str.replaceAll("([^0-9]),", "$1 , ");  
        //str = str.replaceAll(",([^0-9])", " , $1");  
        // Distinguishes single quotes from apstrophes by segmenting off  
        // single quotes not preceded by letters.  
//        a.stop();
//        b.start();
        //str = str.replaceAll("^(')", "$1 ");  
        //str = str.replaceAll(regexNotLetterNumber + "'", "$1 '");  
        // Segments off unambiguous word-final clitics and punctuations.  
        str = str.replaceAll(regexClitics , " $1 ");  
        //str = str.replaceAll(regexClitics + regexNotLetterNumber ," $1 $2");  
//        b.stop();
        // Deals with periods.  
//        c.start();
        String[] words = str.trim().split("\\s+");  
//        c.stop();
        for (String word : words) { 
        	
//        	d.start();
            Matcher m1 = p1.matcher(word);  
            Matcher m2 = p2.matcher(word);  
            if (m1.matches() && !abbrList.contains(word) && !m2.matches()) {  
                // Segments off the period.  
                tokenList += (word.substring(0, word.length() - 1) + " </S> <S> ");  
//                tokenList += (word.substring(word.length() - 1)+ " ");
                //tokenList += ("</S> <S> ");
            } else {  
                tokenList += (word + " ");  
            }  
//            d.stop();
        }
        return tokenList;
    }  
    public static void printTimeAnalysis () {
//		System.out.println("\nTotal time spent in a is: " + a.getElapsedTime());
//		System.out.println("Total time spent in b is: " + b.getElapsedTime());
//		System.out.println("Total time spent in c is: " + c.getElapsedTime());
//		System.out.println("Total time spent in d is: " + c.getElapsedTime());
	}
//    public String[] getTokens() {  
//        String[] tokens = new String[tokenList.size()];  
//        tokenList.toArray(tokens);  
//        return tokens;  
//    }
    public void printTokens() {
//    	String str = "";
//    	for( int i = 0; i< tokenList.size(); i++) {
//    		str += tokenList.get(i)+ "\t";
//    	}
        System.out.println(tokenList);  
    } 
}  