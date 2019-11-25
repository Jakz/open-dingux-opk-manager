package com.github.jakz.opkman.opk;

import java.util.UUID;

public class OpkEntry
{
  public final UUID uuid;
  
  public final String title;
  public final String description;
  public final String comment;
  
  public final OpkCategory category;
  public final String subcategory;
  
  public final String[] authors;
  
  private OpkEntry(UUID uuid, String title, String description, String comment, OpkCategory category, String subcategory, String[] authors)
  {
    this.uuid = uuid;
    this.title = title;
    this.description = description;
    this.comment = comment;
    this.category = category;
    this.subcategory = subcategory;
    this.authors = authors;
  }
  
  public static OpkEntry of(UUID uuid, String title, OpkCategory category)
  {
    return new OpkEntry(uuid, title, "", "", category, "", new String[0]);
  }
}
