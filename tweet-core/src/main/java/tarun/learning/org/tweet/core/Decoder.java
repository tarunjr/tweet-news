package tarun.learning.org.tweet.core;

public interface Decoder<T> {
	T decode(byte[] bytes);
}
