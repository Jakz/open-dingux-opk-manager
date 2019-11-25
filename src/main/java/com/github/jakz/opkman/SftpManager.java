package com.github.jakz.opkman;

import java.io.ByteArrayOutputStream;
import java.net.ConnectException;

import org.apache.log4j.Logger;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

class SftpManager implements AutoCloseable
{
  private static Logger logger = Logger.getLogger(SftpManager.class);
  
  private Config config;
  private ChannelSftp channel;
  
  public SftpManager(Config config)
  {
    this.config = config;
  }
  
  private void tryToFetchConsoleIndex() throws SftpException, JSchException
  {
    
    try
    {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      channel().get(config.indexPath, bos);
    }
    catch (SftpException e)
    {
      if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE)
      {
        logger.debug("Index file not found at "+config.indexPath);
      }
      else
        throw e;
    }
    
  }
  
  private ChannelSftp channel() throws JSchException
  {
    if (channel == null)
      channel = initializeSftp();
    
    return channel;
  }
  
  private ChannelSftp initializeSftp() throws JSchException
  {
    JSch jsch = new JSch();
    Session session = jsch.getSession(config.userName, config.hostName);
    
    if (config.password != null && !config.password.isEmpty())
      session.setPassword(config.password);

    session.setConfig("StrictHostKeyChecking", "no");
    session.connect();
    ChannelSftp channel = (ChannelSftp)session.openChannel("sftp");
    channel.connect();
    return channel;
  }
  
  @Override
  public void close()
  {
    if (channel != null)
    {
      try
      {
        channel.disconnect();
        channel.getSession().disconnect();
      } 
      catch (JSchException e)
      {
        e.printStackTrace();
      }
    }
  }
  
  public void test() throws SftpException, JSchException
  {
    try
    {
      tryToFetchConsoleIndex();
      close();
    }
    catch (JSchException e)
    {
      if (e.getCause() instanceof ConnectException)
        logger.debug(String.format("Connection to %s timed out", config.hostName), e.getCause());

      throw e;
    }
    

  }
}