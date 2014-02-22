package edu.cmu.cs.ziy;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Set;

import org.jdom2.JDOMException;
import org.junit.Test;

import com.google.common.collect.Sets;

public class BcorgPostsXmlReaderTest {

  @Test
  public void test() throws JDOMException, IOException {
    BcorgPostsXmlReader reader = new BcorgPostsXmlReader(getClass().getResourceAsStream(
            "/bcorg/xml/bcorg-0000.xml"));
    Set<Integer> ids = Sets.newHashSet();
    while (reader.hasNext()) {
      BcorgPost post = reader.next();
      ids.add(post.getId());
    }
    assertEquals(ids.size(), 1000);
  }

}
