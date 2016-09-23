package tarun.learning.org.TwitterKafkaProducer.interfaces;

import twitter4j.Status;

public interface Filter {
  boolean filter(Status status);
}
