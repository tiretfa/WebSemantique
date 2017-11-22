import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;

import java.io.InputStream;
import java.util.Scanner;

public class ReadBookstore {

    static final String inputFileName  = "artemisBookstoreData-v1.ttl";

    public static void printBooksByAuthor(String firstNameInput, String lastNameInput){
        // create an empty model
        Model model = ModelFactory.createDefaultModel();

        // use the FileManager to find the input file
        InputStream in = FileManager.get().open(inputFileName);
        if (in == null) {
            throw new IllegalArgumentException( "File: " + inputFileName + " not found");
        }

        // read the RDF/XML file
        model.read( in, "", "Turtle" );


        String lastName = "";
        String firstName = "";

        for (StmtIterator iter = model.listStatements(new SimpleSelector(null, null, (RDFNode) null )); iter.hasNext(); ) {
            Statement s = iter.next();

            Resource  subject   = s.getSubject();
            Property  predicate = s.getPredicate();
            RDFNode   object    = s.getObject();

            if(predicate.getLocalName().compareTo("author") == 0){

                object.asResource().listProperties();

                for (StmtIterator j = object.asResource().listProperties(); j.hasNext(); ) {
                    Statement st = j.next();

                    if(st.getPredicate().getLocalName().compareTo("lastName") == 0){
                        lastName = st.getLiteral().toString();
                    }

                    if(st.getPredicate().getLocalName().compareTo("firstName") == 0){
                        firstName = st.getLiteral().toString();
                    }
                }
                if(firstNameInput.compareTo(firstName)== 0 && lastNameInput.compareTo(lastName) == 0)
                    System.out.print("Author: " + firstName + " " + lastName + " -> ");
            }

            if(firstNameInput.compareTo(firstName)== 0 && lastNameInput.compareTo(lastName) == 0){
                if(predicate.getLocalName().compareTo("title") == 0){
                    System.out.println("Title: " + s.getLiteral().toString());
                }
            }
        }
    }

    public static void main (String args[]) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Veuillez saisir le prenom de l'auteur :");
        String firstName = sc.nextLine();
        System.out.println("Veuillez saisir le nom de l'auteur :");
        String lastName = sc.nextLine();

        printBooksByAuthor(firstName, lastName);

    }
}
