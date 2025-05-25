package com.github.errebenito.metallumbot;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TestListAppender extends AbstractAppender {

  private final List<LogEvent> events = new ArrayList<>();

  protected TestListAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
    super(name, filter, layout, false, Property.EMPTY_ARRAY);
  }

  public static TestListAppender create(String name) {
    return new TestListAppender(name, null, null);
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
