package com.github.errebenito.metallumbot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;
import jakarta.validation.Valid;

/**
 * POJO for mapping the JSON.

 * @author rbenito
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"aaData"})
public class UpcomingAlbums implements Serializable {
  
  private static final int START_LENGTH = 8;

  private static final int ALBUM_LINK_OFFSET = 1;

  private static final long serialVersionUID = 2788594305013188535L;

  private static final String PROPERTY_NAME = "aaData";

  private final Random random = new Random();

  @JsonProperty(PROPERTY_NAME)
  @Valid
  private List<List<String>> albumData = new ArrayList<>();
      
  @JsonProperty(PROPERTY_NAME)
  public List<List<String>> getAlbumData() {
    return albumData;
  }
  
  @JsonProperty(PROPERTY_NAME)
  public void setAlbumData(final List<List<String>> albumData) {
    this.albumData = albumData;
  }  
  
  @Override
  public String toString() {
    return trimLink(this.getAlbumData().get(random.nextInt(this.albumData.size())).get(ALBUM_LINK_OFFSET)); 
  }
   
  private String trimLink(final String link) {
    final int index = link.lastIndexOf("\">");
    return link.substring(START_LENGTH, index).replace("\"", "");
  }

}
