PREFIX foaf:  <http://xmlns.com/foaf/0.1/>
PREFIX owl:   <http://www.w3.org/2002/07/owl#>
PREFIX dbo: <http://dbpedia.org/ontology/>

ASK WHERE{
    {
    SELECT (COUNT(?birthPlaceName) AS ?num)
        WHERE
        {
            ?auth   foaf:firstName "Norman" ;
                    foaf:lastName "Mailer" ;
                    owl:sameAs ?dbpr .

            SERVICE <http://dbpedia.org/sparql>{
                ?dbpr dbo:birthPlace ?birthPlace .
                ?birthPlace foaf:name ?birthPlaceName
            }
        }
    }
    FILTER (?num > 1)
}
