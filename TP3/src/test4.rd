PREFIX foaf:    <http://xmlns.com/foaf/0.1/>
PREFIX owl:     <http://www.w3.org/2002/07/owl#>
PREFIX dbo:     <http://dbpedia.org/ontology/>
PREFIX dbp:     <http://dbpedia.org/property/>
PREFIX dbr:     <http://dbpedia.org/resource/>
PREFIX abo:     <http://artemisBookstore.com/ontology#>
PREFIX dcterms: <http://purl.org/dc/terms/>

SELECT ?authTitle ?authSpouseTitle
    WHERE
    {
        ?auth   foaf:firstName ?authFirstName ;
                foaf:lastName ?authLastName ;
                owl:sameAs ?dbpr .

        ?authBook   abo:author ?auth;
                dcterms:title ?authTitle

        SERVICE <http://dbpedia.org/sparql>{
            ?dbpr dbo:spouse ?spouse .
        }

        ?authSpouse foaf:firstName ?authSpouseFirstName ;
                    foaf:lastName ?authSpouseLastName ;
                    owl:sameAs ?spouse .

        ?authSpouseBook   abo:author ?authSpouse;
                          dcterms:title ?authSpouseTitle

        FILTER NOT EXISTS{
            SERVICE <http://dbpedia.org/sparql>{
                ?dbpr dbo:deathDate ?deathDate .
            }
        }

        FILTER NOT EXISTS{
            SERVICE <http://dbpedia.org/sparql>{
                ?spouse dbo:deathDate ?deathDate .
            }
        }
    }
