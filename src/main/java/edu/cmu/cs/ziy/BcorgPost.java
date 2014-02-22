package edu.cmu.cs.ziy;

import java.util.Date;

public class BcorgPost {

  private int id;

  private Date postedTime;

  private int forumId;
  
  private int threadId;
  
  private int authorId;
  
  private String authorName;
  
  private boolean threadStarter;
  
  private String title;
  
  private String content;

  public BcorgPost(int id, Date postedTime, int forumId, int threadId, int authorId,
          String authorName, boolean threadStarter, String title, String content) {
    super();
    this.id = id;
    this.postedTime = postedTime;
    this.forumId = forumId;
    this.threadId = threadId;
    this.authorId = authorId;
    this.authorName = authorName;
    this.threadStarter = threadStarter;
    this.title = title;
    this.content = content;
  }

  @Override
  public String toString() {
    return String.valueOf(id);
  }


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    BcorgPost other = (BcorgPost) obj;
    if (id != other.id)
      return false;
    return true;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Date getPostedTime() {
    return postedTime;
  }

  public void setPostedTime(Date postedTime) {
    this.postedTime = postedTime;
  }

  public int getForumId() {
    return forumId;
  }

  public void setForumId(int forumId) {
    this.forumId = forumId;
  }

  public int getThreadId() {
    return threadId;
  }

  public void setThreadId(int threadId) {
    this.threadId = threadId;
  }

  public int getAuthorId() {
    return authorId;
  }

  public void setAuthorId(int authorId) {
    this.authorId = authorId;
  }

  public String getAuthorName() {
    return authorName;
  }

  public void setAuthorName(String authorName) {
    this.authorName = authorName;
  }

  public boolean isThreadStarter() {
    return threadStarter;
  }

  public void setThreadStarter(boolean threadStarter) {
    this.threadStarter = threadStarter;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

}
