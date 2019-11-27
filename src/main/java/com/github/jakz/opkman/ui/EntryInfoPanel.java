package com.github.jakz.opkman.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.Painter;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.nimbus.AbstractRegionPainter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.github.jakz.opkman.opk.Entry;

public class EntryInfoPanel extends JPanel
{
  private final Mediator mediator;
  private Entry entry;
  
  JLabel icon, title, category;
  JTextPane description, comment, notes;
  JScrollPane[] panes;
  
  public EntryInfoPanel(Mediator mediator)
  {
    this.mediator = mediator;
    
    icon = new JLabel();
    icon.setPreferredSize(new Dimension(40, 40));
    //icon.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    
    title = new JLabel();
    title.setFont(title.getFont().deriveFont(Font.BOLD));
    
    category = new JLabel();
    category.setFont(category.getFont().deriveFont(category.getFont().getSize()*0.8f));
    
    description = new JTextPane();
    comment = new JTextPane();
    notes = new JTextPane();
    
    JTextPane[] areas = new JTextPane[] { description, comment, notes };
    panes = new JScrollPane[areas.length];
    
    UIDefaults defaults = new UIDefaults();
    //defaults.put("TextPane.background", new ColorUIResource(Color.YELLOW));

    
    for (int i = 0; i < areas.length; ++i)
    {
      //areas[i].setEditable(false);
      areas[i].setBorder(null);
      areas[i].setBackground(Color.RED);
      areas[i].setContentType("text/html");
      areas[i].putClientProperty("Nimbus.Overrides", defaults);
      areas[i].putClientProperty("Nimbus.Overrides.InheritDefaults", false);

      panes[i] = new JScrollPane(areas[i]);
      panes[i].setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      panes[i].setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
      panes[i].setMinimumSize(new Dimension(280, 80));
      panes[i].setPreferredSize(new Dimension(280, 80));

    }
    
    setPreferredSize(new Dimension(300, 500));
    setLayout(new GridBagLayout());
    
    GridBagHolder c = new GridBagHolder();
    
    add(icon, c.g(0, 0).s(3, 3).vfill().c());
    add(title, c.g(3, 1).s(4,1).leftInsets(10).topLeft().c());
    add(category, c.g(3, 2).s(4,1).leftInsets(10).c());
    
    c.hfill().noInsets().topInsets(30);
    
    add(panes[0], c.g(0, 4).s(7,10).h(10).c());
    add(panes[1], c.g(0, 14).s(7,10).c());
    add(panes[2], c.g(0, 24).s(7,10).c());
        
    refresh();
  }
  
  public void setEntry(Entry entry)
  {
    this.entry = entry;
  }
  
  public void refresh()
  {
    title.setText("");
    category.setText("");
    icon.setIcon(null);
    for (JScrollPane pane : panes) 
      pane.setVisible(false);

    if (entry != null)
    {
      title.setText(entry.title);
      category.setText(entry.category.name + (entry.subcategory.isEmpty() ? "" : " (" + entry.subcategory + ")"));
      icon.setIcon(mediator.gui().iconCache.asyncGet(entry, () -> refresh()));
      
      if (!entry.description.isEmpty())
      {
        description.setText("<b>Description</b><br>" + entry.description);
        panes[0].setVisible(true);
      }
      
      if (!entry.comment.isEmpty())
      {
        comment.setText("<b>Comment</b><br>" +entry.comment);
        panes[1].setVisible(true);
      }
      
      if (!entry.notes.isEmpty())
      {
        notes.setText("<b>Notes</b><br>" +entry.notes);
        panes[2].setVisible(true);
      }
    }
  }
  
  private static class Painter extends javax.swing.plaf.nimbus.AbstractRegionPainter
  {
    private final Color color;

    private Painter(Color color)
    {
      this.color = color;
    }
    
    @Override
    protected AbstractRegionPainter.PaintContext getPaintContext()
    {
      return new AbstractRegionPainter.PaintContext(null, null, false);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys)
    {
      g.setColor(c.isEnabled() ? c.getBackground() : color);
      g.fillRect(0, 0, width, height);
    }
  }
  
}
