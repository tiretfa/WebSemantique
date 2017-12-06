PREFIX foaf:  <http://xmlns.com/foaf/0.1/>
PREFIX owl:   <http://www.w3.org/2002/07/owl#>
PREFIX dbo: <http://dbpedia.org/ontology/>
PREFIX dbp: <http://dbpedia.org/property/>
PREFIX dbr: <http://dbpedia.org/resource/>

ASK WHERE{
    {
    SELECT (COUNT(?authSpouse) AS ?nb)
        WHERE
        {
            ?auth   foaf:firstName "Paul" ;
                    foaf:lastName "Auster" ;
                    owl:sameAs ?dbpr .

            SERVICE <http://dbpedia.org/sparql>{
                ?dbpr dbo:spouse ?spouse .
            }

            ?authSpouse owl:sameAs ?spouse
        }
    }
    FILTER (?nb > 0)
}