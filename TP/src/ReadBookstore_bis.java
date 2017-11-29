import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.DCTerms;

import java.io.InputStream;
import java.util.Scanner;

public class ReadBookstore_bis {

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

        // Récupération des authors qui ont correspondent aux nom et aux prénoms demandés
        Resource author = null;

        StmtIterator iter = model.listStatements(new SimpleSelector(null, FOAF.lastName, (RDFNode)null){
            public boolean selects(Statement s) {
                return s.getString().compareTo(lastNameInput)==0;
            }
        });

        if (iter.hasNext()) {
            while (iter.hasNext()) {

                Statement stmt = iter.nextStatement();
                Resource  subject   = stmt.getSubject();

                if(subject.getProperty(FOAF.firstName).getLiteral().toString().compareTo(firstNameInput) == 0){
                    author = subject;
                }
            }
        }


        if(author != null){
        //Récupération des livres qui ont comme auteur celui trouvé précédemment
        Property prop = model.getProperty("http://artemisBookstore.com/ontology#author");

            iter = model.listStatements(new SimpleSelector(null, prop, (RDFNode)null){

            public boolean selects(Statement s) {
                return (s.getResource().getProperty(FOAF.firstName).getLiteral().toString().compareTo(firstNameInput)== 0) &&
                        (s.getResource().getProperty(FOAF.lastName).getLiteral().toString().compareTo(lastNameInput)== 0);
            }
        });

            if (iter.hasNext()) {
                System.out.println("Les livres écrits par " + author.getProperty(FOAF.firstName).getLiteral() + " " + author.getProperty(FOAF.lastName).getLiteral() + " sont:");
                while (iter.hasNext()) {
                    Statement stmt = iter.nextStatement();
                    Resource  subject   = stmt.getSubject();
                    System.out.println("\t- " + subject.getProperty(DCTerms.title).getLiteral());
                }
            } else {
                System.out.println("Il n'y a pas de livres écrits par " + author.getProperty(FOAF.firstName).getLiteral() + " " + author.getProperty(FOAF.lastName).getLiteral() + " sont:");
            }

        }
        else
            System.out.println("Il n'y a aucun auteur correspondant à votre sélection");
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
