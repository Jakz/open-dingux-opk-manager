package com.github.jakz.opkman.repository;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import com.github.jakz.opkman.opk.OpkEntry;

public class OpkRepository
{
  private final Set<OpkEntry> entries;
  
  public OpkRepository()
  {
    entries = new TreeSet<>();
  }
}
