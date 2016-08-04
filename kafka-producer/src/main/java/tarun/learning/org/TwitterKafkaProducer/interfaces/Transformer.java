package tarun.learning.org.TwitterKafkaProducer.interfaces;

public interface Transformer<S,T> {
	T transform(S source);
}
