package com.github.jakz.opkman.ui;

import com.github.jakz.opkman.App;
import com.github.jakz.opkman.opk.Entry;
import com.github.jakz.opkman.repository.Repository;

public class Mediator
{
  private final Repository repository;
  
  public Mediator(Repository repository)
  {
    this.repository = repository;
  }
  
  public Repository repository()
  {
    return repository;
  }
  
  public MainPanel gui()
  {
    return App.gui;
  }
  
  public void onEntrySelected(Entry entry)
  {
    App.gui.entryInfoPanel.setEntry(entry);
    App.gui.entryInfoPanel.refresh();
  }
}
