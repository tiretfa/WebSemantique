import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class Turtle {
	private HashMap<String,UUID> authors;
	private HashMap<String,UUID> publishers;
	
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
	    int i = 0;
	    while ((line = br.readLine()) != null) {
		    if(!firstline){	
		    	
		    	String[] newLine = line.split(";");
	    		    
//		    	i++;
//		    	System.out.println(i + " " + newLine.length );
		    	
    			author = newLine[0];    
    			author += " " +newLine[1];
    			if(authors.containsKey(author)){
    				idAuthor = authors.get(author);
    			}else{
    				idAuthor = UUID.randomUUID();
    				authors.put(author, idAuthor);
    			}
    			
    			bookTitle = newLine[2];
    			idBook = UUID.randomUUID();
 
    			pagesNb = newLine[3];
    	
    			isbn = newLine[4];
    			
    			if(newLine.length >= 6){
	    			publisher = newLine[5];
	    			if(publishers.containsKey(publisher)){
	    				idPublisher = publishers.get(publisher);
	    			}else{
	    				idPublisher = UUID.randomUUID();
	    				publishers.put(publisher, idPublisher);
	    			}
    			}
		    		
		    	
//		    	for(String s : newLine){
//		    		id = UUID.randomUUID();
//		    		System.out.println(s + " " + id);
//		    	}
		    			    
		    }
		    if(firstline){
		    	firstline = false;
		    	
		    }
//	    	System.out.println(line);
//			bw.write(line);
//			bw.newLine();
	    }
	    br.close();
	    //System.out.println(publishers.size()+ " " + authors.size());
//	    bw.close();
	  }
	  
	  public static void main (String[] args) throws IOException{
		  Turtle t1 = new Turtle();
		  t1.turtle("artemisBookstoreData-v1.csv", "");
	  }
	
}
