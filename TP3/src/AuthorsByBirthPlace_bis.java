import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.DCTerms;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class AuthorsByBirthPlace_bis {

    private static final String inputFileName  = "artemisBookstoreData-v2.ttl";

    private static void findAuthorsByBirthPlace(String state) throws IOException{
        Model model = ModelFactory.createDefaultModel();

        // use the FileManager to find the input file
        InputStream in = FileManager.get().open(inputFileName);
        if (in == null) {
            throw new IllegalArgumentException("File: " + inputFileName + " not found");
        }

        // read the ttl file
        model.read( in, "", "Turtle");

        state = state.replace(" ", "_");

        String queryString =
                "PREFIX foaf:  <http://xmlns.com/foaf/0.1/> " +
                "PREFIX owl:   <http://www.w3.org/2002/07/owl#> " +
                "PREFIX dbo: <http://dbpedia.org/ontology/> " +
                "PREFIX dbr: <http://dbpedia.org/resource/> " +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "CONSTRUCT { " +
                        "?auth rdf:type foaf:Person. " +
                        "?auth foaf:firstName ?firstName. " +
                        "?auth foaf:lastName ?lastName. " +
                        "} " +
                        "WHERE " +
                        "{" +
                        "    ?auth   foaf:firstName ?firstName ; " +
                        "            foaf:lastName ?lastName ; " +
                        "            owl:sameAs ?dbpr . " +
                        "    SERVICE <http://dbpedia.org/sparql>{ " +
                        "        { " +
                        "            ?dbpr dbo:birthPlace dbr:" + state + " . " +
                        "        } " +
                        "        UNION " +
                        "        { " +
                        "            ?dbpr dbo:birthPlace ?birthPlace . " +
                        "            ?birthPlace dbo:isPartOf dbr:" + state + " . " +
                        "        } . " +
                        "    } " +
                        "}";

        Query query = QueryFactory.create(queryString);

        // Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        Model results = qe.execConstruct();

        //Décommenter cette ligne pour obtenir le modèle construit sur la sortie standard
        //results.write(System.out, "Turtle");

        Property prop = results.getProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");

        StmtIterator iter = results.listStatements(new SimpleSelector(null, prop, (RDFNode)null));

        if (iter.hasNext()) {
            System.out.println("Les auteurs nés dans l'Etat de " + state + " sont:");
            while (iter.hasNext()) {
                Statement stmt = iter.nextStatement();
                Resource  subject   = stmt.getSubject();
                System.out.println("\t- " + subject.getProperty(FOAF.firstName).getLiteral() + " " + subject.getProperty(FOAF.lastName).getLiteral());
            }
        } else {
            System.out.println("Il n'y a pas d'auteurs nés dans l'Etat de " + state + ".");
        }

        // Important - free up resources used running the query
        qe.close();

    }

    public static void main (String args[]) throws IOException {

        Scanner sc = new Scanner(System.in);
        System.out.println("Veuillez saisir l'Etat choisi :");
        String state = sc.nextLine();
        findAuthorsByBirthPlace(state);
    }
}
