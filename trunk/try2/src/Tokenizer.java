import java.util.ArrayList;  
import java.util.Arrays;  
import java.util.List;  
import java.util.regex.Pattern;  
import java.util.regex.Matcher;  
 
public class Tokenizer {  
    private String str;  
    private ArrayList<String> tokenList;  
  
    private static final String regexLetterNumber = "[a-zA-Z0-9]";  
    private static final String regexNotLetterNumber = "[^a-zA-Z0-9]";  
    private static final String regexSeparator = "[\\?!()\";/\\|`]";  
    private static final String regexClitics = "'|:|-|'S|'D|'M|'LL|'RE|'VE|N'T|'s|'d|'m|'ll|'re|'ve|n't";  
    private static final List<String> abbrList =  
        Arrays.asList("Co.", "Corp.", "vs.", "e.g.", "etc.", "ex.", "cf.",  
            "eg.", "Jan.", "Feb.", "Mar.", "Apr.", "Jun.", "Jul.", "Aug.",  
            "Sept.", "Oct.", "Nov.", "Dec.", "jan.", "feb.", "mar.",  
            "apr.", "jun.", "jul.", "aug.", "sept.", "oct.", "nov.",  
            "dec.", "est.", "b.", "m.", "bur.", "d.", "r.", "M.", "Dept.",  
            "MM.", "U.", "Mr.", "Jr.", "Ms.", "Mme.", "Mrs.", "Dr.",  
            "Ph.D.","U.S.", "U.K.","Ltd.", "A.M.", "i.e.");  
  
    public Tokenizer(String str) {  
        this.str = str;  
        tokenList = new ArrayList<String>();  
    }  
  
    public String[] tokenize() {  
        str = str.replaceAll("\\t", " "); // Changes tabs into spaces.  
        str = str.replaceAll("(" + regexSeparator + ")", " $1 "); // Puts blanks around unambiguous separators.  
        // Puts blanks around commas that are not inside numbers.  
        str = str.replaceAll("([^0-9]),", "$1 , ");  
        str = str.replaceAll(",([^0-9])", " , $1");  
        // Distinguishes single quotes from apstrophes by segmenting off  
        // single quotes not preceded by letters.  
        str = str.replaceAll("^(')", "$1 ");  
        str = str.replaceAll("(" + regexNotLetterNumber + ")'", "$1 '");  
        // Segments off unambiguous word-final clitics and punctuations.  
        str = str.replaceAll("(" + regexClitics + ")$", " $1");  
        str = str.replaceAll("(" + regexClitics + ")(" + regexNotLetterNumber + ")"," $1 $2");  
  
        // Deals with periods.  
        String[] words = str.trim().split("\\s+");  
        Pattern p1 = Pattern.compile(".*" + regexLetterNumber + "\\.");  
        Pattern p2 = Pattern.compile("^([A-Za-z]\\.([A-Za-z]\\.)+|[A-Z][bcdfghj-nptvxz]+\\.)$");  
        for (String word : words) {  
            Matcher m1 = p1.matcher(word);  
            Matcher m2 = p2.matcher(word);  
            
            if (m1.matches() && !abbrList.contains(word) && !m2.matches()) {  
                // Segments off the period.  
                tokenList.add(word.substring(0, word.length() - 1));  
//                tokenList.add(word.substring(word.length() - 1));
                tokenList.add("</S>");
            	tokenList.add("<S>");
            } else {  
                tokenList.add(word);  
            }  
        }
        return getTokens();
    }  
  
    public String[] getTokens() {  
        String[] tokens = new String[tokenList.size()];  
        tokenList.toArray(tokens);  
        return tokens;  
    }
    public void printTokens() {
    	String str = "";
    	for( int i = 0; i< tokenList.size(); i++) {
    		str += tokenList.get(i)+ "\t";
    	}
        System.out.println(str);  
    } 
}  