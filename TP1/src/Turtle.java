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
		    			    
		    	author="LastName FirstName";
				publisher="PublisherName";
				bookTitle="BookTitle";
				pagesNb="PagesNb";
				isbn="Isbn";
				idAuthor=UUID.randomUUID();
				idBook=UUID.randomUUID();
				idPublisher=UUID.randomUUID();


				String firstName = author.split(" ")[1];
				String lastName = author.split(" ")[0];

				System.out.println("@prefix foaf: <http://xmlns.com/foaf/0.1/> .");
				System.out.println("@prefix dcterms: <http://purl.org/dc/terms/> .");
				System.out.println("@prefix abo: <http://artemisBookstore.com/ontology#> .");
				System.out.println("@prefix authorPrefix: <http://artemisBookstore.com/id/author/> .");
				System.out.println("@prefix bookPrefix: <http://artemisBookstore.com/id/book/> .");
				System.out.println("@prefix publisherPrefix: <http://artemisBookstore.com/id/publisher/> .");

				System.out.println("");

				System.out.println("authorPrefix:" + idAuthor + " a foaf:Person ;");
				System.out.println("\t foaf:firstName \"" + firstName + "\" ;");
				System.out.println("\t foaf:lastName \"" + lastName + "\" ;");

				System.out.println("bookPrefix:" + idAuthor + " a dcterms:BibliographicResource ;");
				System.out.println("\t dcterms:title \"" + bookTitle + "\" ;");
				System.out.println("\t abo:author " + "authorPrefix:" + idAuthor);
		    }else{
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

		  Turtle turtle = new Turtle();
		  turtle.turtle("artemisBookstoreData-v1.csv", "");

	  }
	
}
