package com.github.jakz.opkman.ui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainPanel extends JPanel
{
  private final Mediator mediator;
  
  public final IconCache iconCache;
  public final EntryTablePanel entryTablePanel;
  public final EntryInfoPanel entryInfoPanel;
  
  public MainPanel(Mediator mediator)
  {
    this.mediator = mediator;
    
    iconCache = new IconCache();
    entryTablePanel = new EntryTablePanel(mediator);
    entryInfoPanel = new EntryInfoPanel(mediator);
    
    this.setLayout(new BorderLayout());
    add(entryTablePanel, BorderLayout.CENTER);
    
    JPanel subPanel = new JPanel(new BorderLayout());
    subPanel.add(entryInfoPanel, BorderLayout.NORTH);
    subPanel.add(new JLabel(), BorderLayout.CENTER);
    add(subPanel, BorderLayout.EAST);
  }
}
