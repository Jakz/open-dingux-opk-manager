package com.github.jakz.opkman.opk;

public enum System
{
  GCW0("GCW0", "gcw0"),
  RG350("RG350", "rg350")
  
  ;
  
  private System(String name, String identifier)
  {
    this.name = name;
    this.identifier = identifier;
  }
  
  public final String name;
  public final String identifier;
}
