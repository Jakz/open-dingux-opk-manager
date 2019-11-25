package com.github.jakz.opkman;

import java.util.Arrays;

import com.pixbits.lib.lang.StringUtils;

public class Sha1
{
  public static final int BYTES = 20;
  
  private byte[] data;
  
  public Sha1(byte[] data)
  {
    if (data.length != BYTES)
      throw new IllegalArgumentException("length of SHA-1 must be 20 bytes");
    
    this.data = data;
  }
  
  public Sha1(String data)
  {
    this.data = StringUtils.fromHexString(data);
    
    if (this.data.length != BYTES)
      throw new IllegalArgumentException("length of SHA-1 must be 20 bytes");    
  }
  
  @Override
  public boolean equals(Object object)
  {
    return object instanceof Sha1 && Arrays.equals(((Sha1)object).data, data);
  }
  
  @Override
  public int hashCode()
  {
    return Arrays.hashCode(data);
  }
}
