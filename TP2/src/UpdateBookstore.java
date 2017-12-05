import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.OWL;

import java.io.*;
import java.util.HashMap;

public class UpdateBookstore {
    HashMap<String, String> americanWriters;


    public UpdateBookstore(){
        americanWriters = new HashMap<>();
    }

    public void updateBookstore(String inputDbPedia, String inputModel, String output) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(inputDbPedia));

        String line, americanWriter = "", uri = "";

        Boolean firstline = true;

        //Stockage des écrivains américain de dbPedia
        while ((line = br.readLine()) != null) {
            if(firstline){
                String[] newLine = line.split(",");

                firstline = false;
            }else{

                String[] newLine = line.split(",");

                americanWriter = newLine[0];
                uri = newLine[1];


                String[] uriTab = uri.split("/");

                if(uriTab.length>=5){
                    uri = uriTab[4];
                }

                if(!this.americanWriters.containsKey(americanWriter))
                    this.americanWriters.put(americanWriter,uri);
            }
        }
        br.close();

        // Lecture du model au format turtle
        Model model = ModelFactory.createDefaultModel();

        InputStream in = FileManager.get().open( inputModel );
        FileOutputStream out = new FileOutputStream(output);

        model.read(in, "", "Turtle");

        model.setNsPrefix("dbr","http://dbpedia.org/resource/");
        model.setNsPrefix("owl","http://www.w3.org/2002/07/owl#");

        // Parcours de tous les auteurs présents dans notre Bookstore
        StmtIterator iter = model.listStatements(new SimpleSelector(null, null, (RDFNode)FOAF.Person));

        int nbLink = 0;

        if (iter.hasNext()) {
            while (iter.hasNext()) {

                Statement stmt = iter.nextStatement();
                Resource  subject   = stmt.getSubject();

                String firstName = subject.getProperty(FOAF.firstName).getLiteral().toString();
                String lastName = subject.getProperty(FOAF.lastName).getLiteral().toString();

                String testAmericanWriter = firstName.trim() + " " + lastName.trim();

                if(this.americanWriters.containsKey(testAmericanWriter)){
                    Resource writer = model.createResource("dbr:" + this.americanWriters.get(testAmericanWriter));
                    model.add(subject, OWL.sameAs, writer);
                    nbLink++;
                }
            }
        }

        System.out.println(nbLink +  " liens vers dbPedia ont été rajoutés dans le model d'origine.");

        model.write(out,"Turtle");

        in.close();
        out.close();
    }


    // Permet de lire le model rdf du fichier d'entrée pour vérifier qu'il ne comporte pas d'erreur
    public void readBookstore(String inputFileName){

        Model model = ModelFactory.createDefaultModel();

        InputStream in = FileManager.get().open( inputFileName );

        if (in == null) {
            throw new IllegalArgumentException( "File: " + inputFileName + " not found");
        }

        model.read(in, "", "Turtle");
    }

    // Lit un model au format Turtle puis génère un nouveau fichier contenant le même modèle au format RDF/XML
    public void generateRtfXML(String input, String output) throws IOException{
        Model model = ModelFactory.createDefaultModel();
        InputStream in = FileManager.get().open( input );

        model.read(in, "", "Turtle");
        OutputStream out = new FileOutputStream(output);
        model.write(out, "RDF/XML");
    }

    public static void main (String[] args) throws IOException{
        UpdateBookstore generator = new UpdateBookstore();

        // Génération d'un fichier rdf au format turtle
        generator.updateBookstore("dbpediaAmericanWriter.csv",
                                "artemisBookstoreData-v1.ttl",
                                "artemisBookstoreData-v2.ttl");

        // Permet la validation du fichier généré
        generator.readBookstore("artemisBookstoreData-v2.ttl");

        // Génère le bookstore avec les liens au format rtf/xml
        generator.generateRtfXML("artemisBookstoreData-v2.ttl", "artemisBookstoreData-v2.rtf");
    }
}
