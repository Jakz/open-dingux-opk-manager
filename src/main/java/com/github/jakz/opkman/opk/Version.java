package com.github.jakz.opkman.opk;

public class Version
{
  public final int major, minor;
  String detail;
  
  public Version(int major, int minor, String detail)
  {
    this.major = major;
    this.minor = minor;
    this.detail = detail;
  }
  
  public static Version of(int major, int minor)
  {
    return new Version(major, minor, "");
  }
}
