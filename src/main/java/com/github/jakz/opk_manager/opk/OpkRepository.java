package com.github.jakz.opk_manager.opk;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class OpkRepository
{
  private final Set<OpkEntry> entries;
  
  public OpkRepository()
  {
    entries = new TreeSet<>();
  }
}
