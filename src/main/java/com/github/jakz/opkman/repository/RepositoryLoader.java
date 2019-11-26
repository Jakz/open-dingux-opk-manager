package com.github.jakz.opkman.repository;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.github.jakz.opkman.Sha1;
import com.github.jakz.opkman.opk.OpkCategory;
import com.github.jakz.opkman.opk.OpkEntry;
import com.github.jakz.opkman.opk.OpkRelease;
import com.github.jakz.opkman.opk.OpkSystem;
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
  
  public OpkRepository load(Path path) throws JsonSyntaxException, JsonIOException, IOException
  {
    GsonBuilder builder = new GsonBuilder();
    
    builder.registerTypeAdapter(Sha1.class, new Sha1Deserializer());
    builder.registerTypeAdapter(OpkCategory.class, new CategoryDeserializer());
    builder.registerTypeAdapter(OpkSystem.class, new SystemDeserializer());
    builder.registerTypeAdapter(Version.class, new VersionDeserializer());
    builder.registerTypeAdapter(LocalDate.class, new DateDeserializer());
    
    builder.registerTypeAdapter(OpkRelease.class, new ReleaseDeserializer());
    builder.registerTypeAdapter(OpkEntry.class, new EntryDeserializer());
    builder.registerTypeAdapter(OpkRepository.class, new RepositoryDeserializer());
    
    Gson gson = builder.create();
    
    OpkRepository repository = gson.fromJson(Files.newBufferedReader(path), OpkRepository.class);
    
    logger.info(String.format(
        "loaded repository from %s: %d entries, %d total releases", 
        path.toString(), repository.size(), repository.stream().flatMap(OpkEntry::stream).count())
    );
    
    return repository;
  }
  
  private <T> T optionalParse(JsonObject object, String key, Class<T> type, T def, JsonDeserializationContext context)
  {
    return object.has("key") ?
      context.deserialize(object.get("key"), type) :
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
  
  private class CategoryDeserializer implements JsonDeserializer<OpkCategory>
  {
    @Override
    public OpkCategory deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
    {
      String identifier = json.getAsString();
      return Arrays.stream(OpkCategory.values())
          .filter(category -> category.identifier.equals(identifier))
          .findFirst()
          .orElseThrow(() -> new JsonParseException("Unknown category: "+identifier));
    }
  }
  
  private class SystemDeserializer implements JsonDeserializer<OpkSystem>
  {
    @Override
    public OpkSystem deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
    {
      String identifier = json.getAsString();
      return Arrays.stream(OpkSystem.values())
          .filter(system -> system.identifier.equals(identifier))
          .findFirst()
          .orElseThrow(() -> new JsonParseException("Unknown category: "+identifier));
    }
  }
  
  private class ReleaseDeserializer implements JsonDeserializer<OpkRelease>
  {

    @Override
    public OpkRelease deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
    {
      JsonObject obj = json.getAsJsonObject();
      
      UUID uuid = context.deserialize(obj.get("entry"), UUID.class);
      Sha1 sha1 = context.deserialize(obj.get("sha1"), Sha1.class);
      long size = obj.get("size").getAsLong();
      Version version = context.deserialize(obj.get("version"), Version.class);
      OpkSystem[] systems = context.deserialize(obj.get("systems"), OpkSystem[].class);
      
      return null;
    }
    
  }
  
  private class EntryDeserializer implements JsonDeserializer<OpkEntry>
  {
    @Override
    public OpkEntry deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
    {
      JsonObject obj = json.getAsJsonObject();
      
      UUID uuid = context.deserialize(obj.get("uuid"), UUID.class);
      String title = context.deserialize(obj.get("title"), String.class);
      String description = optionalParse(obj, "description", String.class, "", context);
      String comment = optionalParse(obj, "comment", String.class, "", context);
      OpkCategory category = context.deserialize(obj.get("category"), OpkCategory.class);
      String subcategory = optionalParse(obj, "subcategory", String.class, "", context);
      String author = optionalParse(obj, "author", String.class, "", context);
      List<OpkRelease> releases = context.deserialize(obj.get("releases"), new TypeToken<List<OpkRelease>>(){}.getType());
      
      return new OpkEntry(uuid, title, description, comment, category, subcategory, author, releases);
    }
  }
  
  private class RepositoryDeserializer implements JsonDeserializer<OpkRepository>
  {

    @Override
    public OpkRepository deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
    {
      Collection<OpkEntry> entries = context.deserialize(json.getAsJsonObject().get("entries"), new TypeToken<List<OpkEntry>>(){}.getType());
      return new OpkRepository(entries);
    }
    
  }
}
