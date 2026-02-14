package com.github.errebenito.metallumbot.bot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;

public class ListAppender extends AbstractAppender {
  private final List<LogEvent> events = new ArrayList<>();

  protected ListAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
    super(name, filter, layout, false, Property.EMPTY_ARRAY);
  }

  public static ListAppender create(String name) {
    return new ListAppender(name, null, null);
  }

  @Override
  public void append(LogEvent event) {
    events.add(event.toImmutable());
  }

  public List<LogEvent> getEvents() {
    return events;
  }

  public List<String> getFormattedMessages() {
    return events.stream().map(e -> e.getMessage().getFormattedMessage()).toList();
  }
}