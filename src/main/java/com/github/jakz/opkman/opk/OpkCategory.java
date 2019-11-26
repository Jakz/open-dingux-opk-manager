package com.github.jakz.opkman.opk;

public enum OpkCategory
{
  APPLICATION("Application", "app"),
  GAME("Game", "game"),
  EMULATOR("Emulator", "emu")
  
  ;
  
  private OpkCategory(String name, String identifier)
  {
    this.name = name;
    this.identifier = identifier;
  }
  
  public final String name;
  public final String identifier;
}
