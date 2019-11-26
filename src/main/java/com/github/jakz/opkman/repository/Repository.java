package com.github.jakz.opkman.repository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import com.github.jakz.opkman.opk.Entry;
import com.github.jakz.opkman.opk.Release;

public class Repository
{
  private final Set<Entry> entries;
  
  public Repository(Collection<Entry> entries)
  {
    this.entries = new TreeSet<>(entries);
  }
  
  public int size()
  {
    return entries.size();
  }
  
  public Stream<Entry> stream()
  {
    return entries.stream();
  }
}
