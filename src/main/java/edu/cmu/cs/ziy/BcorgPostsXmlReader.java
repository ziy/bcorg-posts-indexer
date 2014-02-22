package edu.cmu.cs.ziy;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class BcorgPostsXmlReader implements Iterator<BcorgPost> {

  public static final String BCORG_POST_ELEMENT = "ROW";
  
  private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  private static SAXBuilder builder = new SAXBuilder();

  private List<Element> citations;

  public BcorgPostsXmlReader(InputStream inputStream) throws JDOMException, IOException {
    Document document = (Document) builder.build(inputStream);
    Element rootNode = document.getRootElement();
    citations = rootNode.getChildren(BCORG_POST_ELEMENT);
  }

  private int idx = 0;

  @Override
  public boolean hasNext() {
    return idx < citations.size();
  }

  @Override
  public BcorgPost next() {
    Element postElement = (Element) citations.get(idx++);
    int id = Integer.parseInt(postElement.getChildText(BcorgPostFields.ID));
    Date postedTime = null;
    try {
      postedTime = df.parse(postElement.getChildText(BcorgPostFields.POSTED_TIME));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    int forumId = Integer.parseInt(postElement.getChildText(BcorgPostFields.FORUM_ID));
    int threadId = Integer.parseInt(postElement.getChildText(BcorgPostFields.THREAD_ID));
    int authorId = Integer.parseInt(postElement.getChildText(BcorgPostFields.AUTHOR_ID));
    String authorName = postElement.getChildText(BcorgPostFields.AUTHOR_NAME);
    boolean threadStarter = postElement.getChildText(BcorgPostFields.THREAD_STARTER).equals("1") ? true
            : false;
    String title = postElement.getChildText(BcorgPostFields.TITLE);
    String content = postElement.getChildText(BcorgPostFields.CONTENT);
    return new BcorgPost(id, postedTime, forumId, threadId, authorId, authorName, threadStarter,
            title, content);
  }

  @Override
  public void remove() {
    // TODO Auto-generated method stub

  }

}
