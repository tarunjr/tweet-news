package tarun.learning.org.tweet.core;

public interface Encoder<T> {
	byte[] encode(T source);
}
