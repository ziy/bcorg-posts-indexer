package edu.cmu.cs.ziy;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.jdom2.JDOMException;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.Files;

public class BcorgPostsIndexer {

  private static class SuppportedExtensionFilter implements FilenameFilter {

    @Override
    public boolean accept(File dir, String name) {
      for (String extension : supportedExtensions) {
        if (name.endsWith(extension)) {
          return true;
        }
      }
      return false;
    }
  }

  private IndexWriter writer;

  public BcorgPostsIndexer(String indexPath) throws IOException {
    File indexDir = new File(indexPath);
    if (indexDir.exists() && indexDir.listFiles().length > 0) {
      throw new RuntimeException(new FileAlreadyExistsException(indexDir.getAbsolutePath()));
    } else if (!indexDir.exists()) {
      indexDir.mkdir();
    }
    Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);
    IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_46, analyzer);
    iwc.setOpenMode(OpenMode.CREATE);
    iwc.setRAMBufferSizeMB(2000);
    writer = new IndexWriter(FSDirectory.open(indexDir), iwc);
  }

  public static Set<String> supportedExtensions = Sets.newHashSet(".xml.gz", ".xml");

  public void indexDocs(File file) throws JDOMException, IOException {
    String extName = Files.getFileExtension(file.getName());
    if (extName.equals("xml")) {
      indexDocs(new BufferedInputStream(new FileInputStream(file)));
    } else if (extName.equals("gz")) {
      indexDocs(new BufferedInputStream(new GZIPInputStream(new FileInputStream(file))));
    }
  }

  public void indexDocs(InputStream inputStream) throws JDOMException, IOException {
    BcorgPostsXmlReader reader = new BcorgPostsXmlReader(inputStream);
    while (reader.hasNext()) {
      BcorgPost post = reader.next();
      Document doc = new Document();
      doc.add(new IntField(BcorgPostFields.ID, post.getId(), Field.Store.YES));
      doc.add(new LongField(BcorgPostFields.POSTED_TIME, post.getPostedTime().getTime(),
              Field.Store.YES));
      doc.add(new IntField(BcorgPostFields.FORUM_ID, post.getForumId(), Field.Store.YES));
      doc.add(new IntField(BcorgPostFields.THREAD_ID, post.getThreadId(), Field.Store.YES));
      doc.add(new IntField(BcorgPostFields.AUTHOR_ID, post.getAuthorId(), Field.Store.YES));
      doc.add(new StringField(BcorgPostFields.AUTHOR_NAME, post.getAuthorName(), Field.Store.YES));
      doc.add(new StringField(BcorgPostFields.THREAD_STARTER,
              String.valueOf(post.isThreadStarter()), Field.Store.YES));
      doc.add(new TextField(BcorgPostFields.TITLE, post.getTitle(), Field.Store.YES));
      doc.add(new TextField(BcorgPostFields.CONTENT, post.getContent(), Field.Store.YES));
      writer.addDocument(doc);
    }
    writer.commit();
  }

  public void optimize() throws IOException {
    writer.forceMerge(1);
    writer.close();
  }

  public static void main(String[] args) throws JDOMException, IOException {
    BcorgPostsIndexer bpi = new BcorgPostsIndexer(args[0]);
    File dir = new File(args[1]);
    List<File> files = Lists.newArrayList(dir.listFiles(new SuppportedExtensionFilter()));
    Collections.sort(files);
    Stopwatch stopwatch = Stopwatch.createStarted();
    for (File file : files) {
      System.out.print("Indexing " + file.getName() + "... ");
      stopwatch.reset();
      bpi.indexDocs(file);
      System.out.println(stopwatch.elapsed(TimeUnit.SECONDS) + " secs");
    }
    System.out.print("Optimizing... ");
    stopwatch.reset();
    bpi.optimize();
    System.out.println(stopwatch.elapsed(TimeUnit.SECONDS) + " secs");
  }

}
