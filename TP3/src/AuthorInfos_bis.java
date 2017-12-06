import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.DCTerms;

import java.io.InputStream;
import java.util.Scanner;

public class AuthorInfos_bis {

    private static final String inputFileName  = "artemisBookstoreData-v2.ttl";

    private static void findAuthorInfos(String firstName, String lastName){
        Model model = ModelFactory.createDefaultModel();

        // use the FileManager to find the input file
        InputStream in = FileManager.get().open(inputFileName);
        if (in == null) {
            throw new IllegalArgumentException("File: " + inputFileName + " not found");
        }

        // read the RDF/XML file
        model.read( in, "", "Turtle");

//        model.write(System.out, "Turtle");

        // Create a new query
        String queryString =
                "PREFIX foaf:  <http://xmlns.com/foaf/0.1/> " +
                "PREFIX owl:   <http://www.w3.org/2002/07/owl#> " +
                "PREFIX dbo: <http://dbpedia.org/ontology/> " +
                "CONSTRUCT { " +
                            "?auth dbo:birthPlace ?birthPlace. " +
                            "?auth dbo:birthDate ?birthDate. " +
                            "?auth dbo:abstract ?abstract. " +
                        "} " +
                        "WHERE " +
                        "{" +
                        "    ?auth   foaf:firstName \"" + firstName + "\" ; " +
                        "            foaf:lastName \"" + lastName + "\" ; " +
                        "            owl:sameAs ?dbpr . " +
                        "    SERVICE <http://dbpedia.org/sparql>{ " +
                        "        ?dbpr dbo:birthPlace ?birthPlace . " +
                        "        ?dbpr dbo:birthDate ?birthDate . " +
                        "        ?dbpr dbo:abstract ?abstract . " +
                        "        ?birthPlace foaf:name ?birthPlaceName " +
                        "    }" +
                        "}";

        Query query = QueryFactory.create(queryString);

        // Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        Model results = qe.execConstruct();

        // Affichage du lieu de naissance
        Property prop = results.getProperty("http://dbpedia.org/ontology/birthPlace");

        StmtIterator iter = results.listStatements(new SimpleSelector(null, prop, (RDFNode)null));

        if (iter.hasNext()) {
            System.out.println("Le lieu de naissance de " + firstName + " " + lastName + " est:");
            while (iter.hasNext()) {
                Statement stmt = iter.nextStatement();
                RDFNode object = stmt.getObject();
                System.out.println("\t-" + object.toString());
            }
        }

        // Affichage de la date de naissance
        prop = results.getProperty("http://dbpedia.org/ontology/birthDate");

        iter = results.listStatements(new SimpleSelector(null, prop, (RDFNode)null));

        if (iter.hasNext()) {
            System.out.println("La date de naissance de " + firstName + " " + lastName + " est:");
            while (iter.hasNext()) {
                Statement stmt = iter.nextStatement();
                RDFNode object = stmt.getObject();
                System.out.println("\t-" + object.toString());
            }
        }

        // Affichage du résumé
        prop = results.getProperty("http://dbpedia.org/ontology/abstract");

        iter = results.listStatements(new SimpleSelector(null, prop, (RDFNode)null));

        if (iter.hasNext()) {
            System.out.println("Le résumé concernant " + firstName + " " + lastName + " est:");
            while (iter.hasNext()) {
                Statement stmt = iter.nextStatement();
                RDFNode object = stmt.getObject();
                System.out.println("\t-" + object.toString());
            }
        }

        // results.write(System.out, "Turtle");

        // Important - free up resources used running the query
        qe.close();

    }

    public static void main (String args[]) {

        Scanner sc = new Scanner(System.in);
        System.out.println("Veuillez saisir le prenom de l'auteur :");
        String firstName = sc.nextLine();
        System.out.println("Veuillez saisir le nom de l'auteur :");
        String lastName = sc.nextLine();
        findAuthorInfos(firstName, lastName);
    }
}
