package com.github.jakz.opkman.opk;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.EnumSet;

import com.github.jakz.opkman.Sha1;

public class OpkRelease
{
  public final Sha1 checksum;
  public final long size;;
  public final EnumSet<OpkSystem> systems;
  public final Version version;
  public final LocalDate date;
  public final String comment;
  
  private OpkRelease(Sha1 checksum, long size, Version version, LocalDate date, String comment, OpkSystem... systems)
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
    return obj instanceof OpkRelease && ((OpkRelease)obj).checksum.equals(checksum) && ((OpkRelease)obj).size == size;
  }
}
