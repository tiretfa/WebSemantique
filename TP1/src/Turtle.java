import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class Turtle {
	HashMap<String,String> author;
	HashMap<String,String> publisher;
	
	  public static void turtle(String input, String output) throws IOException {
	    BufferedReader br = new BufferedReader(new FileReader(input));
//	    BufferedWriter bw = new BufferedWriter(new FileWriter(output));
	    UUID id ;
	    
	    String line, author = "", publisher = "";
	    Boolean firstline = true;
	    while ((line = br.readLine()) != null) {
		    if(firstline){	
		    	String[] newLine = line.split(";");
		    	
		    	for(int i = 0; i < newLine.length; i++){
		    		if(i == 0){
		    			author = newLine[i];
		    		}else if(i == 1 ){
		    			author += " " +newLine[i];
		    		}
		    	}
//		    	for(String s : newline){
//		    		id = UUID.randomUUID();
//		    		System.out.println(s + " " + id);
//		    	}
		    	firstline = false;		    
		    }
//	    	System.out.println(line);
//			bw.write(line);
//			bw.newLine();
	    }
	    br.close();
//	    bw.close();
	  }
	  
	  public static void main (String[] args) throws IOException{
		  Turtle.turtle("artemisBookstoreData-v1.csv", "");
	  }
	
}
