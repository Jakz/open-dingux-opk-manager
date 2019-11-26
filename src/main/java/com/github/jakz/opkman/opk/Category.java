package com.github.jakz.opkman.opk;

public enum Category
{
  APPLICATION("Application", "app"),
  GAME("Game", "game"),
  EMULATOR("Emulator", "emu")
  
  ;
  
  private Category(String name, String identifier)
  {
    this.name = name;
    this.identifier = identifier;
  }
  
  public final String name;
  public final String identifier;
}
