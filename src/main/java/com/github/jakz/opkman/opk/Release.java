package com.github.jakz.opkman.opk;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.EnumSet;

import com.github.jakz.opkman.Sha1;

public class Release
{
  public final Sha1 checksum;
  public final long size;;
  public final EnumSet<System> systems;
  public final Version version;
  public final LocalDate date;
  public final String comment;
  
  private Release(Sha1 checksum, long size, Version version, LocalDate date, String comment, System... systems)
  {
    this.checksum = checksum;
    this.size = size;
    this.version = version;
    this.date = date;
    this.comment = comment;
    this.systems = EnumSet.copyOf(Arrays.asList(systems));
  }
  
  @Override
  public boolean equals(Object obj)
  {
    return obj instanceof Release && ((Release)obj).checksum.equals(checksum) && ((Release)obj).size == size;
  }
}
