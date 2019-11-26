package com.github.jakz.opkman.repository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import com.github.jakz.opkman.opk.OpkEntry;
import com.github.jakz.opkman.opk.OpkRelease;

public class OpkRepository
{
  private final Set<OpkEntry> entries;
  
  public OpkRepository(Collection<OpkEntry> entries)
  {
    this.entries = new TreeSet<>(entries);
  }
  
  public int size()
  {
    return entries.size();
  }
  
  public Stream<OpkEntry> stream()
  {
    return entries.stream();
  }
}
