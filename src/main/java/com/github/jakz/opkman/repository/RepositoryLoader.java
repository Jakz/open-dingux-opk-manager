package com.github.jakz.opkman.repository;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.github.jakz.opkman.Sha1;
import com.github.jakz.opkman.opk.Category;
import com.github.jakz.opkman.opk.Entry;
import com.github.jakz.opkman.opk.Release;
import com.github.jakz.opkman.opk.System;
import com.github.jakz.opkman.opk.Version;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.pixbits.lib.lang.StringUtils;

public class RepositoryLoader
{ 
  private static Logger logger = Logger.getLogger(RepositoryLoader.class);

  public RepositoryLoader()
  {
    
  }
  
  public Repository load(Path path) throws JsonSyntaxException, JsonIOException, IOException
  {
    GsonBuilder builder = new GsonBuilder();
    
    builder.registerTypeAdapter(Sha1.class, new Sha1Deserializer());
    builder.registerTypeAdapter(Category.class, new CategoryDeserializer());
    builder.registerTypeAdapter(System.class, new SystemDeserializer());
    builder.registerTypeAdapter(Version.class, new VersionDeserializer());
    builder.registerTypeAdapter(LocalDate.class, new DateDeserializer());
    
    builder.registerTypeAdapter(Release.class, new ReleaseDeserializer());
    builder.registerTypeAdapter(Entry.class, new EntryDeserializer());
    builder.registerTypeAdapter(Repository.class, new RepositoryDeserializer());
    
    Gson gson = builder.create();
    
    Repository repository = gson.fromJson(Files.newBufferedReader(path), Repository.class);
    
    logger.info(String.format(
        "loaded repository from %s: %d entries, %d total releases", 
        path.toString(), repository.size(), repository.stream().flatMap(Entry::stream).count())
    );
    
    return repository;
  }
  
  private <T> T optionalParse(JsonObject object, String key, Class<T> type, T def, JsonDeserializationContext context)
  {
    return object.has(key) ?
      context.deserialize(object.get(key), type) :
      def;
  }
  
  private class DateDeserializer implements JsonDeserializer<LocalDate>
  {

    @Override
    public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
    {
      return LocalDate.parse(json.getAsString());
    }
    
  }
  
  private class VersionDeserializer implements JsonDeserializer<Version>
  {

    @Override
    public Version deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
    {
      //TODO: complete
      return new Version(1, 0, json.getAsString());
    }  
  }
  
  private class Sha1Deserializer implements JsonDeserializer<Sha1>
  {

    @Override
    public Sha1 deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
    {
      byte[] data = StringUtils.fromHexString(json.getAsString());    
      return new Sha1(data);
    }
  }
  
  private class CategoryDeserializer implements JsonDeserializer<Category>
  {
    @Override
    public Category deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
    {
      String identifier = json.getAsString();
      return Arrays.stream(Category.values())
          .filter(category -> category.identifier.equals(identifier))
          .findFirst()
          .orElseThrow(() -> new JsonParseException("Unknown category: "+identifier));
    }
  }
  
  private class SystemDeserializer implements JsonDeserializer<System>
  {
    @Override
    public System deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
    {
      String identifier = json.getAsString();
      return Arrays.stream(System.values())
          .filter(system -> system.identifier.equals(identifier))
          .findFirst()
          .orElseThrow(() -> new JsonParseException("Unknown category: "+identifier));
    }
  }
  
  private class ReleaseDeserializer implements JsonDeserializer<Release>
  {

    @Override
    public Release deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
    {
      JsonObject obj = json.getAsJsonObject();
      
      Sha1 sha1 = context.deserialize(obj.get("sha1"), Sha1.class);
      long size = obj.get("size").getAsLong();
      Version version = context.deserialize(obj.get("version"), Version.class);
      System[] systems = context.deserialize(obj.get("systems"), System[].class);
      String comment = optionalParse(obj, "comment", String.class, "", context);
      LocalDate date = context.deserialize(obj.get("date"), LocalDate.class);
      
      return new Release(sha1, size, version, date, comment, systems);
    }
    
  }
  
  private class EntryDeserializer implements JsonDeserializer<Entry>
  {
    @Override
    public Entry deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
    {
      JsonObject obj = json.getAsJsonObject();
      
      UUID uuid = context.deserialize(obj.get("uuid"), UUID.class);
      String title = context.deserialize(obj.get("title"), String.class);
      String description = optionalParse(obj, "description", String.class, "", context);
      String comment = optionalParse(obj, "comment", String.class, "", context);
      Category category = context.deserialize(obj.get("category"), Category.class);
      String subcategory = optionalParse(obj, "subcategory", String.class, "", context);
      String author = optionalParse(obj, "author", String.class, "", context);
      URL icon = optionalParse(obj, "icon", URL.class, null, context);
      List<Release> releases = context.deserialize(obj.get("releases"), new TypeToken<List<Release>>(){}.getType());
      
      return new Entry(uuid, title, description, comment, category, subcategory, author, icon, releases);
    }
  }
  
  private class RepositoryDeserializer implements JsonDeserializer<Repository>
  {

    @Override
    public Repository deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
    {
      Collection<Entry> entries = context.deserialize(json.getAsJsonObject().get("entries"), new TypeToken<List<Entry>>(){}.getType());
      return new Repository(entries);
    }
    
  }
}
