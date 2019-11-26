package com.github.jakz.opkman.opk;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class OpkEntry implements Comparable<OpkEntry>
{
  public final UUID uuid;
  
  public final String title;
  public final String description;
  public final String comment;
  
  public final OpkCategory category;
  public final String subcategory;
  
  public final String author;
  
  public final List<OpkRelease> releases;
  
  public OpkEntry(UUID uuid, String title, String description, String comment, OpkCategory category, String subcategory, String author, List<OpkRelease> releases)
  {
    this.uuid = uuid;
    this.title = title;
    this.description = description;
    this.comment = comment;
    this.category = category;
    this.subcategory = subcategory;
    this.author = author;
    this.releases = releases;
  }
  
  public static OpkEntry of(UUID uuid, String title, OpkCategory category)
  {
    return new OpkEntry(uuid, title, "", "", category, "", "", Collections.emptyList());
  }
  
  public Stream<OpkRelease> stream()
  {
    return releases.stream();
  }
  
  @Override
  public boolean equals(Object object)
  {
    return object instanceof OpkEntry && ((OpkEntry)object).uuid.equals(uuid);
  }
  
  @Override
  public int compareTo(OpkEntry other)
  {
    return title.compareTo(other.title);
  }
}
