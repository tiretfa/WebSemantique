import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;

import java.io.InputStream;
import java.util.Scanner;

public class AuthorsByBirthPlace {

    private static final String inputFileName  = "artemisBookstoreData-v2.ttl";

    private static void findAuthorsByBirthPlace(String state){
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
            "SELECT DISTINCT ?firstName ?lastName " +
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
        ResultSet results = qe.execSelect();

        // Output query results
        ResultSetFormatter.out(System.out, results, query);

        // Important - free up resources used running the query
        qe.close();

    }

    public static void main (String args[]) {

        Scanner sc = new Scanner(System.in);
        System.out.println("Veuillez saisir l'Etat choisi :");
        String state = sc.nextLine();
        findAuthorsByBirthPlace(state);
    }
}
