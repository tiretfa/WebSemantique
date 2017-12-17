PREFIX foaf:  <http://xmlns.com/foaf/0.1/>
PREFIX owl:   <http://www.w3.org/2002/07/owl#>
PREFIX dbo: <http://dbpedia.org/ontology/>
PREFIX dbr: <http://dbpedia.org/resource/>

SELECT DISTINCT ?firstName ?lastName

WHERE
{
    ?auth   foaf:firstName ?firstName ;
            foaf:lastName ?lastName ;
            owl:sameAs ?dbpr .

    SERVICE <http://dbpedia.org/sparql>{
        {
            ?dbpr dbo:birthPlace dbr:New_Jersey .
        }
        UNION
        {
            ?dbpr dbo:birthPlace ?birthPlace .
            ?birthPlace dbo:isPartOf dbr:New_Jersey .
        } .
    }
}