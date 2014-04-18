package edu.cmu.cs.ziy;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

public class BcorgPostsSearcherTest {

  @Test
  public void test() throws IOException, ParseException {
    IndexReader reader = DirectoryReader.open(FSDirectory.open(new File("index")));
    IndexSearcher searcher = new IndexSearcher(reader);
    Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);
    QueryParser parser = new QueryParser(Version.LUCENE_46, BcorgPostFields.CONTENT, analyzer);
    // Query query = parser.parse("Herceptin OR Herclon OR Trastuzumab");
    Query query = parser.parse("\"silicone gel\" AND pain");
    ScoreDoc[] hits = searcher.search(query, 100).scoreDocs;
    for (int i = 0; i < hits.length; i++) {
      Document doc = searcher.doc(hits[i].doc);
      System.out.println(doc.get(BcorgPostFields.CONTENT) + "\n");
    }
  }
}
