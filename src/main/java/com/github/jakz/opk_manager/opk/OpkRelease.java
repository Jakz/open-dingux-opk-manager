package com.github.jakz.opk_manager.opk;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.EnumSet;

import com.github.jakz.opk_manager.Sha1;

public class OpkRelease
{
  public final Sha1 checksum;
  public final EnumSet<OpkSystem> systems;
  public final Version version;
  public final LocalDate date;
  
  private OpkRelease(Sha1 checksum, Version version, LocalDate date, OpkSystem... systems)
  {
    this.checksum = checksum;
    this.version = version;
    this.date = date;
    this.systems = EnumSet.copyOf(Arrays.asList(systems));
  }
}
