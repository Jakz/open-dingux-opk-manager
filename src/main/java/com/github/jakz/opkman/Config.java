package com.github.jakz.opkman;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Config
{
  public int timeout = 5000;
  public String hostName = "10.1.1.2";
  public String userName = "root";
  public String password = "";
  
  public String indexPath = "/media/data/local/home/.opkmanager/index.dat";
  
  public Path localCachePath = Paths.get(".");
  
  public Path repositoryPath()
  {
    return localCachePath.resolve("repository.json");
  }
}
