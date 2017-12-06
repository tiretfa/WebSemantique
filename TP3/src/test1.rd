PREFIX abo: <http://artemisBookstore.com/ontology#>
PREFIX foaf:  <http://xmlns.com/foaf/0.1/>
PREFIX dcterms: <http://purl.org/dc/terms/>

SELECT ?title
WHERE  {
    ?subject abo:author ?auth .
    ?auth foaf:firstName "Paul" .
    ?auth foaf:lastName "Auster" .
    ?subject dcterms:title ?title .
}
LIMIT 100