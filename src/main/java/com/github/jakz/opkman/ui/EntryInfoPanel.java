package com.github.jakz.opkman.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import com.github.jakz.opkman.opk.Entry;

public class EntryInfoPanel extends JPanel
{
  private final Mediator mediator;
  private Entry entry;
  
  JLabel icon;
  JLabel title;
  
  public EntryInfoPanel(Mediator mediator)
  {
    this.mediator = mediator;
    
    icon = new JLabel();
    icon.setPreferredSize(new Dimension(40, 40));
    //icon.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    
    title = new JLabel();
    title.setFont(title.getFont().deriveFont(Font.BOLD));
    
    setPreferredSize(new Dimension(200, 500));
    setLayout(new GridBagLayout());
    
    GridBagHolder c = new GridBagHolder();
    add(icon, c.g(0, 0).s(3, 3).c());
    add(title, c.g(3, 1).w(4).leftInsets(10).c());
  }
  
  public void setEntry(Entry entry)
  {
    this.entry = entry;
  }
  
  public void refresh()
  {
    if (entry == null)
    {
      title.setText("");
      icon.setIcon(null);
    }
    else
    {
      title.setText(entry.title);
      icon.setIcon(mediator.gui().iconCache.get(entry));
    }
  }
  
}
