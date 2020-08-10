package org.deri.tarql;

import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;


import java.io.*;
import java.util.Iterator;
import java.util.List;

public class Main {

    public void metaDataRDF( String RDFfile) {
        String prefix = "http://upb.de/";
        String predicatePrefix = "http://upb.de/ont/";
        String RDFtriple = "";

        try {
            PrintWriter writer = new PrintWriter(new FileWriter(RDFfile));
            for(int i=1; i<=11; i++) {
                int j= i-1;

                RDFtriple = "<" + prefix + "popularity/" + i + "> <" +
                        predicatePrefix + "morePopularThan> <" + prefix +  "popularity/" + j + ">";
                writer.println(RDFtriple);
                RDFtriple = "<" + prefix + "duration/" + i + "> <" +
                        predicatePrefix + "moreLongerThan> <" + prefix +   "duration/" + j + ">";
                writer.println(RDFtriple);
                RDFtriple = "<" + prefix + "acousticness/" + i + "> <" +
                        predicatePrefix + "moreAcousticThan> <" + prefix + "acousticness/" + j + ">";
                writer.println(RDFtriple);
                RDFtriple = "<" + prefix + "danceability/" + i + "> <" +
                        predicatePrefix + "moreDanceableThan> <" + prefix + "danceability/" +  j + ">";
                writer.println(RDFtriple);
                RDFtriple = "<" + prefix + "energy/" + i + "> <" +
                        predicatePrefix + "moreEnergeticThan> <" + prefix + "energy/"+ j + ">";
                writer.println(RDFtriple);
                RDFtriple = "<" + prefix + "instrumentalness/" + i + "> <" +
                        predicatePrefix + "moreIntstrumentalThan> <" + prefix +"instrumentalness/" + j + ">" ;

                writer.println(RDFtriple);
                RDFtriple = "<" + prefix + "key/" + i + "> <" +
                        predicatePrefix + "moreHigherThan> <" + prefix + "key/" + j + ">";
                writer.println(RDFtriple);
                RDFtriple = "<" + prefix + "liveness/" + i + "> <" +
                        predicatePrefix + "moreLiveThan> <" + prefix + "liveness/" + j + ">" ;
                writer.println(RDFtriple);
                RDFtriple = "<" + prefix + "timeSignal/" + i + "> <" +
                        predicatePrefix + "moreTimeThan> <" + prefix + "timeSignal/" + j + ">";

                writer.println(RDFtriple);

                RDFtriple = "<" + prefix + "tempo/" + i + "> <" +
                        predicatePrefix + "moreFastThan> <" + prefix + "tempo/" + j + ">";

                writer.println(RDFtriple);
                int k = i+11;
                int l = k-1;
                RDFtriple = "<" + prefix + "tempo/" + k + "> <" +
                        predicatePrefix + "moreFastThan> <" + prefix + "tempo/" + l + ">";

                writer.println(RDFtriple);



            }

            for(int i=-39; i<2; i++) {
                int j= i-1;
                RDFtriple = "<" + prefix + "loudness/" + i + "> <" +
                        predicatePrefix + "moreLoudThan> <" + prefix + "loudness/" + j + ">";

                writer.println(RDFtriple);
            }

            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(RDFtriple);

    }

    public static void main(String [] args) {
        Main obj = new Main();
        obj.metaDataRDF("songRDF2.nt");

        InputStreamSourceExtend fileStream = new InputStreamSourceExtend();
        InputStreamSource source = fileStream.fromFilenameOrIRI("preprocessedSongData2.csv");


        CSVOptions options = new CSVOptions();
        options.setDefaultsForCSV();
        options.setColumnNamesInFirstRow(true);
        options.setEncoding("UTF-8");
        String sparqlQuery = "PREFIX ex: <http://upb.de/songData#>\n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "\n" +
                "CONSTRUCT {\n" +
                "  ?URI a ex:Song;\n" +

                "    ex:artist ?artist_URI;\n" +
                "    ex:album ?album_URI;\n" +
                "    ex:popularity ?popularity_URI;\n" +
                "    ex:acoustics ?acousticenss_URI;\n" +
                "    ex:danceability ?danceability_URI;\n" +
                "    ex:tempo ?tempo_URI;\n" +
                "    \n" +
                "} \n" +
                "FROM <file:preprocessedSongData2.csv> \n" +
                "WHERE {\n" +
                " BIND (URI(CONCAT('http://upb.de/song/', ?song)) AS ?URI)\n" +

                " BIND (URI(CONCAT('http://upb.de/songArtist/', ?artist_name)) AS ?artist_URI)\n" +
                " BIND (URI(CONCAT('http://upb.de/songAlbum/', ?album_names)) AS ?album_URI)\n" +
                " BIND (URI(CONCAT('http://upb.de/popularity/', ?song_popularity)) AS ?popularity_URI)\n" +
                " BIND (URI(CONCAT('http://upb.de/acousticness/', ?acousticness)) AS ?acousticenss_URI)\n" +
                " BIND (URI(CONCAT('http://upb.de/danceability/', ?danceabitlity)) AS ?danceability_URI)\n" +
                " BIND (URI(CONCAT('http://upb.de/tempo/', ?tempo)) AS ?tempo_URI)\n" +
                "  \n" +
                "}";
        Query query = QueryFactory.create(sparqlQuery);
        TarqlQuery tarqlQuery = new TarqlQuery(query);
        TarqlQueryExecution queryExecution = new TarqlQueryExecution(source, options, tarqlQuery);
        System.out.println("Generating triples");
        try {
            Iterator<Triple> triples = queryExecution.execTriples();
            //System.out.println("Generated triples" + triples.hasNext());
            int i=0;
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("songRDF2.nt", true)));

            while (triples.hasNext()) {

                Triple currentTriple = triples.next();
                if(currentTriple.getObject().isLiteral())
                    writer.println("<" + currentTriple.getSubject() + "> <" + currentTriple.getPredicate() + "> " + currentTriple.getObject() + "");
                else
                    writer.println("<" + currentTriple.getSubject() + "> <" + currentTriple.getPredicate() + "> <" + currentTriple.getObject() + ">");

            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }








}
