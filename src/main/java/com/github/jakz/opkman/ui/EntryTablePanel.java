package com.github.jakz.opkman.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import com.github.jakz.opkman.opk.Entry;
import com.pixbits.lib.ui.table.ColumnSpec;
import com.pixbits.lib.ui.table.DataSource;
import com.pixbits.lib.ui.table.FilterableDataSource;
import com.pixbits.lib.ui.table.SimpleListSelectionListener;
import com.pixbits.lib.ui.table.TableModel;

public class EntryTablePanel extends JPanel
{
  private final Mediator mediator;
  
  private FilterableDataSource<Entry> data;
  
  private TableModel<Entry> model;
  private JTable table;
  
  public EntryTablePanel(Mediator mediator)
  {
    this.mediator = mediator;
    
    table = new JTable();
    table.setAutoCreateRowSorter(true);
    model = new Model(table);
    
    setLayout(new BorderLayout());
    JScrollPane pane = new JScrollPane(table);
    pane.setPreferredSize(new Dimension(600, 300));
    add(pane, BorderLayout.CENTER);

    model.addColumn(new ColumnSpec<Entry, ImageIcon>("", ImageIcon.class, 
      entry -> mediator.gui().iconCache.asyncGet(entry, 
        () -> SwingUtilities.invokeLater(() -> model.fireTableDataChanged())
      )
    ).setWidth(40));
    
    model.addColumn(new ColumnSpec<>("Name", String.class, entry -> entry.title));
    model.addColumn(new ColumnSpec<>("Category", String.class, entry -> entry.category.name));
    model.addColumn(new ColumnSpec<>("Subcategory", String.class, entry -> entry.subcategory));
    model.addColumn(new ColumnSpec<Entry, Integer>("#", Integer.class, entry -> entry.releases.size()).setWidth(40));
    
    
    model.setRowHeight(36);
    
    
    table.getSelectionModel().addListSelectionListener(SimpleListSelectionListener.ofJustSingle(i -> {
      mediator.onEntrySelected(i != -1 ? model.data().get(i) : null);      
    }));
  }
  
  public void refresh()
  {
    data = FilterableDataSource.of(mediator.repository().entries());
    model.setData(data);
    model.fireTableDataChanged();
  }
  
  private class Model extends TableModel<Entry>
  {
    Model(JTable table)
    {
      super(table, DataSource.empty());
    }
  }  
}
