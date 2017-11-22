import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class Turtle {
	HashMap<String,String> authors;
	HashMap<String,String> publishers;
	
	public Turtle(){
		authors = new HashMap<>();
		publishers = new HashMap<>();	
	}
	
	  public void turtle(String input, String output) throws IOException {
	    BufferedReader br = new BufferedReader(new FileReader(input));
//	    BufferedWriter bw = new BufferedWriter(new FileWriter(output));
	    UUID id ;
	    
	    String line, author = "", publisher = "", bookTitle = "", pagesNb = "", isbn = "";
	    UUID idAuthor, idPublisher, idBook;
	    Boolean firstline = true;
	    while ((line = br.readLine()) != null) {
		    if(firstline){	
		    	String[] newLine = line.split(";");
		    	
		    	for(int i = 0; i < newLine.length; i++){
		    		if(i == 0){
		    			author = newLine[i];
		    		}else if(i == 1 ){
		    			author += " " +newLine[i];
		    			
		    		}else if(i == 2){
		    			
		    		}
		    	}
//		    	for(String s : newLine){
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
		  
	  }
	
}
