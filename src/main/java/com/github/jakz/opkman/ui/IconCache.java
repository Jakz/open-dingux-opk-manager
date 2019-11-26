package com.github.jakz.opkman.ui;

import java.awt.Image;
import java.io.IOException;
import java.util.function.Function;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.github.jakz.opkman.opk.Entry;

public class IconCache extends com.pixbits.lib.util.IconCache<Entry>
{
  public IconCache()
  {
    super(entry -> {
      if (entry.hasIcon())
      {
        Image image;
        try
        {
          image = ImageIO.read(entry.icon);
          return new ImageIcon(image);

        } 
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }

      return null;
    });
  }
}
