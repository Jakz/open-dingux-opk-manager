package com.github.jakz.opkman.opk;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class Entry implements Comparable<Entry>
{
  public final UUID uuid;
  
  public final String title;
  public final String description;
  public final String comment;
  
  public final Category category;
  public final String subcategory;
  
  public final String author;
  
  public final List<Release> releases;
  
  public Entry(UUID uuid, String title, String description, String comment, Category category, String subcategory, String author, List<Release> releases)
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
  
  public static Entry of(UUID uuid, String title, Category category)
  {
    return new Entry(uuid, title, "", "", category, "", "", Collections.emptyList());
  }
  
  public Stream<Release> stream()
  {
    return releases.stream();
  }
  
  @Override
  public boolean equals(Object object)
  {
    return object instanceof Entry && ((Entry)object).uuid.equals(uuid);
  }
  
  @Override
  public int compareTo(Entry other)
  {
    return title.compareTo(other.title);
  }
}
