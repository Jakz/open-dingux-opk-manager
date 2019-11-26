package com.github.jakz.opkman;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.file.Paths;
import java.util.Vector;

import com.github.jakz.opkman.repository.Repository;
import com.github.jakz.opkman.repository.RepositoryLoader;
import com.github.jakz.opkman.ui.EntryTablePanel;
import com.github.jakz.opkman.ui.MainPanel;
import com.github.jakz.opkman.ui.Mediator;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.pixbits.lib.ui.UIUtils;
import com.pixbits.lib.ui.WrapperFrame;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class App 
{
  public static MainPanel gui;
  
  public static void buildUI(Repository repository)
  {
    UIUtils.setNimbusLNF();
    
    Mediator mediator = new Mediator(repository);
    
    WrapperFrame<MainPanel> frame = UIUtils.buildFrame(new MainPanel(mediator), "OpkMan v0.1");
    frame.setVisible(true);
    frame.exitOnClose();
    frame.centerOnScreen();
    
    gui = frame.panel();
    
    frame.panel().entryTablePanel.refresh();
  }
  
  
  
  @SuppressWarnings("unchecked")
  public static void main(String[] args)
  {
    try
    {
      //SftpManager manager = new SftpManager(new Config());
      //manager.test();
      
      RepositoryLoader loader = new RepositoryLoader();
      Repository repository = loader.load(Paths.get("repository.json"));
      
      buildUI(repository);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    
    try
    {
      /*ChannelSftp channel = initializeSFTP();
      channel.connect();
      System.out.println(channel.pwd());
      
      
      Vector<LsEntry> vector = channel.ls("/media/data/apps");
      for (LsEntry object : vector)
        System.out.println(object.getFilename());
        
      
      channel.exit();*/
      
      /*ChannelShell channel = initializeSSH();
      
      channel.setOutputStream(System.out);
     
      PipedInputStream pis = new PipedInputStream();
      PipedOutputStream pos = new PipedOutputStream();
      channel.setInputStream(new PipedInputStream(pos));
      channel.setOutputStream(new PipedOutputStream(pis));
      
      channel.connect();
      
      pos.write("ls /media/data/apps\r".getBytes());
      pos.write("sha1sum uae4all.opk\r".getBytes());
      pos.flush();

      new Thread() {
        @Override
        public void run()
        {
          byte[] buffer = new byte[1024];
          try
          {
            while (true)
            {
              System.out.println("Av: "+pis.available());
              while (pis.available() > 0)
              {
                int c = pis.read(buffer, 0, 1024);
                System.out.println(new String(buffer, 0, c));
              }
              
              Thread.sleep(1000);
            }
          } catch (Exception e)
          {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
      }.start();*/
      

      
      /*pos.close();
      
      
      channel.disconnect();
      channel.getSession().disconnect();*/
      
    } 
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  static final String HOST_NAME = "10.1.1.2";
  static final String USERNAME = "root";
  
  public static ChannelSftp initializeSFTP() throws JSchException
  {
    JSch jsch = new JSch();
    Session session = jsch.getSession(USERNAME, HOST_NAME);

    session.setConfig("StrictHostKeyChecking", "no");
    session.connect();
    ChannelSftp channel = (ChannelSftp)session.openChannel("sftp");
    channel.connect();
    return channel;
  }
  
  public static ChannelShell initializeSSH() throws JSchException
  {
    JSch jsch = new JSch();
    Session session = jsch.getSession(USERNAME, HOST_NAME);

    session.setConfig("StrictHostKeyChecking", "no");
    session.connect();
    ChannelShell channel = (ChannelShell)session.openChannel("shell");
    return channel;
  }
}
