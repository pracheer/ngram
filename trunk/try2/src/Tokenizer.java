import java.util.*;  
import java.util.regex.*;  
 
public class Tokenizer {  
    private String str;  
    private String tokenList;  
  
    private static final String regexLetterNumber = "[a-zA-Z0-9]";  
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
        str = str.replaceAll("<.*>", "");
        str = str.replaceAll(regexSeparator , " $1 "); // Puts blanks around unambiguous separators.  
        // Segments off unambiguous word-final clitics and punctuations.  
        str = str.replaceAll(regexClitics , " $1 ");  
        // Deals with periods.  
        String[] words = str.trim().split("\\s+");  
        for (String word : words) { 
        	
            Matcher m1 = p1.matcher(word);  
            Matcher m2 = p2.matcher(word);  
            if (m1.matches() && !abbrList.contains(word) && !m2.matches()) {  
                // Segments off the period.  
                tokenList += (word.substring(0, word.length() - 1) + " </S> <S> ");  
            } else {  
                tokenList += (word + " ");  
            }  
        }
        return tokenList;
    }  
}  