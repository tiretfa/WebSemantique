import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

public class Turtle {
	HashMap<String,UUID> authors;
	HashMap<String,UUID> publishers;
	
	public Turtle(){
		authors = new HashMap<>();
		publishers = new HashMap<>();	
	}

	public void generateShortRtfXML(String input, String output) throws IOException{

		generateTurtle(input, "temp.ttl", false);
		Model model = ModelFactory.createDefaultModel();
		InputStream in = FileManager.get().open( "temp.ttl" );

		if (in == null) {
			throw new IllegalArgumentException( "File: temp.ttl not found");
		}

		model.read(in, "", "Turtle");
		OutputStream out = new FileOutputStream(output);
		model.write(out, "RDF/XML");

		File file = new File("temp.ttl");
		file.delete();
	}

	public void generateTurtle(String input, String output, Boolean all) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(input));
		BufferedWriter bw = new BufferedWriter(new FileWriter(output));

		String line, author = "", publisherName = "", bookTitle = "", pagesNb = "", isbn = "", firstName = "", lastName = "";
		UUID UUIDAuthor = null, UUIDPublisher = null, UUIDBook = null;
		Boolean firstline = true;
		Boolean read = true;

		bw.write("@prefix foaf: <http://xmlns.com/foaf/0.1/> ."); bw.newLine();
		bw.write("@prefix dcterms: <http://purl.org/dc/terms/> ."); bw.newLine();
		bw.write("@prefix abo: <http://artemisBookstore.com/ontology#> ."); bw.newLine();
		bw.write("@prefix authorPrefix: <http://artemisBookstore.com/id/author/> ."); bw.newLine();
		bw.write("@prefix bookPrefix: <http://artemisBookstore.com/id/book/> ."); bw.newLine();
		bw.write("@prefix publisherPrefix: <http://artemisBookstore.com/id/publisher/> ."); bw.newLine();

		bw.newLine();

		while ((line = br.readLine()) != null) {
			if(firstline){
				String[] newLine = line.split(";");

				for(int i = 0; i < newLine.length; i++){
					System.out.println(newLine[i]);
				}

				author = "LastName FirstName";
				publisherName = "PublisherName";
				bookTitle = "BookTitle";
				pagesNb = "PagesNb";
				isbn = "Isbn";
				UUIDAuthor = UUID.randomUUID();
				UUIDBook = UUID.randomUUID();
				UUIDPublisher = UUID.randomUUID();

				firstName = author.split(" ")[1];
				lastName = author.split(" ")[0];

				System.out.println("");

				System.out.println("@prefix foaf: <http://xmlns.com/foaf/0.1/> .");
				System.out.println("@prefix dcterms: <http://purl.org/dc/terms/> .");
				System.out.println("@prefix abo: <http://artemisBookstore.com/ontology#> .");
				System.out.println("@prefix authorPrefix: <http://artemisBookstore.com/id/author/> .");
				System.out.println("@prefix bookPrefix: <http://artemisBookstore.com/id/book/> .");
				System.out.println("@prefix publisherPrefix: <http://artemisBookstore.com/id/publisher/> .");

				System.out.println("");

				System.out.println("authorPrefix:" + UUIDAuthor + " a foaf:Person ;");
				System.out.println("\t foaf:firstName \"" + firstName + "\" ;");
				System.out.println("\t foaf:lastName \"" + lastName + "\" .");

				System.out.println("");

				System.out.println("bookPrefix:" + UUIDBook + " a dcterms:BibliographicResource ;");
				System.out.println("\t dcterms:title \"" + bookTitle + "\" ;");
				System.out.println("\t abo:author " + "authorPrefix:" + UUIDAuthor + " ;");
				System.out.println("\t abo:isbn \"" + isbn + "\" ;");
				System.out.println("\t abo:pages \"" + pagesNb + "\"^^<http://www.w3.org/2001/XMLSchema#int> ;");
				System.out.println("\t dcterms:publisher " + "publisherPrefix:" + UUIDPublisher + " .");

				System.out.println("");

				System.out.println("publisherPrefix:" + UUIDPublisher + " a dcterms:publisher ;");
				System.out.println("\t foaf:name \"" + publisherName + "\" .");

				firstline = false;
			}else if(read){

				if(!all){
					read = false;
				}

				String[] newLine = line.split(";");

				author=newLine[0] + " " + newLine[1];
				firstName = author.split(" ")[1].replace("\"", "");
				lastName = author.split(" ")[0].replace("\"", "");

				if(this.authors.containsKey(author)){
					UUIDAuthor = this.authors.get(author);
				}else{
					UUIDAuthor = UUID.randomUUID();

					this.authors.put(author, UUIDAuthor);

					bw.write("authorPrefix:" + UUIDAuthor + " a foaf:Person ;"); bw.newLine();
					bw.write("\t foaf:firstName \"" + firstName + "\" ;"); bw.newLine();
					bw.write("\t foaf:lastName \"" + lastName + "\" ."); bw.newLine();
					bw.newLine();
				}

				bookTitle = newLine[2].replace("\"", "");
				UUIDBook = UUID.randomUUID();

				pagesNb=newLine[3].replace("\"","");
				isbn=newLine[4].replace("\"","");

				if(newLine.length >= 6){
					publisherName=newLine[5];

					if(this.publishers.containsKey(publisherName)){
						UUIDPublisher = this.authors.get(publisherName);
					}else{
						UUIDPublisher = UUID.randomUUID();
						this.publishers.put(publisherName, UUIDPublisher);

						bw.write("publisherPrefix:" + UUIDPublisher + " a dcterms:publisher ;"); bw.newLine();
						bw.write("\t foaf:name \"" + publisherName + "\" ."); bw.newLine();
						bw.newLine();
					}
				}

				bw.write("bookPrefix:" + UUIDBook + " a dcterms:BibliographicResource ;"); bw.newLine();
				bw.write("\t dcterms:title \"" + bookTitle + "\" ;"); bw.newLine();
				bw.write("\t abo:author " + "authorPrefix:" + UUIDAuthor + " ;"); bw.newLine();
				bw.write("\t abo:isbn \"" + isbn + "\" ;"); bw.newLine();
				bw.write("\t abo:pages \"" + pagesNb + "\"^^<http://www.w3.org/2001/XMLSchema#int> ;"); bw.newLine();
				bw.write("\t dcterms:publisher " + "publisherPrefix:" + UUIDPublisher + " ."); bw.newLine();
				bw.newLine();
			}
		}
		br.close();
		bw.close();
	}


	public void readTurtle(String inputFileName){

		Model model = ModelFactory.createDefaultModel();

		InputStream in = FileManager.get().open( inputFileName );

		if (in == null) {
			throw new IllegalArgumentException( "File: " + inputFileName + " not found");
		}

		model.read(in, "", "Turtle");
	}

	public static void main (String[] args) throws IOException{
		Turtle turtle = new Turtle();
		turtle.generateShortRtfXML("artemisBookstoreData-v1.csv", "artemisBookstoreData-v1.rtf");
		turtle.generateTurtle("artemisBookstoreData-v1.csv", "artemisBookstoreData-v1.ttl", true);
		turtle.readTurtle("artemisBookstoreData-v1.ttl");
	}
}
