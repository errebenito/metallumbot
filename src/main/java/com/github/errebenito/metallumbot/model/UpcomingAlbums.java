package com.github.errebenito.metallumbot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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

  private static final long serialVersionUID = 2788594305013188535L;

  private static final String PROPERTY_NAME = "aaData";

  @JsonProperty(PROPERTY_NAME)
  @Valid
  private List<List<String>> albumData = new ArrayList<>();
      
  @JsonProperty(PROPERTY_NAME)
  public List<List<String>> getAlbumData() {
    return albumData;
  }
  
  @JsonProperty(PROPERTY_NAME)
  public void setalbumData(final List<List<String>> albumData) {
    this.albumData = albumData;
  }  
  
  @Override
  public String toString() {
    final List<String> result = new ArrayList<>();
    for (final List<String> list : this.getAlbumData().subList(0, 10)) {
      for (final String string : list) {
        if (isAlbumLink(string)) {
          result.add(trimLink(string));
        }
      }
    }
    return formatData(Arrays.toString(result.toArray()));
  }
    
  private boolean isAlbumLink(final String string) {
    return string.contains("https://www.metal-archives.com/albums/");
  }

  private String trimLink(final String link) {
    final int index = link.lastIndexOf("\">");
    return link.substring(START_LENGTH, index).replace("\"", "");
  }
  
  private String formatData(final String data) {
    return data.replace("[", "").replace("]", "").replace(",", "\n");
  }
}
