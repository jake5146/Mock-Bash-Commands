import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.net.*;

public class urlFcn {

	public static void main(String[] args) {
		/*try {
			URL url = new URL("http://scholar.google.ca/citations?user=rr8pZoUAAAAJ&hl=en");
			 InputStream is = (InputStream) url.getContent();
			 BufferedReader br = new BufferedReader(new InputStreamReader(is));
			 String line = null;
			 StringBuffer sb = new StringBuffer();
			 while((line = br.readLine()) != null){
			   sb.append(line);
			 }
			 String htmlContent = sb.toString();
			 br.close();
			 System.out.println(htmlContent);
		} catch (MalformedURLException e) { 
			System.out.println("The URL is a malformed URL. Please try again.");
		} catch (IOException e) {   
		    System.out.println("The URL is invalid. Please try again.");
		}*/
        String reForAuthorExtraction = "(.?)";	
        Pattern patternObject = Pattern.compile(reForAuthorExtraction);
        Matcher matcherObject = patternObject.matcher("! @ # $ % ^ & * ( ) - _ + = { } [ ] : ; \' \" < > , . ? / ~ `");
        while (matcherObject.find()) {
        	System.out.println(matcherObject.group(1));
        }


	}

}









