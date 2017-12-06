PREFIX foaf:  <http://xmlns.com/foaf/0.1/>
PREFIX owl:   <http://www.w3.org/2002/07/owl#>
PREFIX dbo: <http://dbpedia.org/ontology/>

SELECT ?birthPlaceName

WHERE
{
    ?auth   foaf:firstName "Paul" ;
            foaf:lastName "Auster" ;
            owl:sameAs ?dbpr .

    SERVICE <http://dbpedia.org/sparql>{
        ?dbpr dbo:birthPlace ?birthPlace .
        ?birthPlace foaf:name ?birthPlaceName
    }
}