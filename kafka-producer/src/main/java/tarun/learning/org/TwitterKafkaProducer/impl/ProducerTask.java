package tarun.learning.org.TwitterKafkaProducer.impl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import tarun.learning.org.TwitterKafkaProducer.interfaces.Producer;
import tarun.learning.org.TwitterKafkaProducer.interfaces.Transformer;
import tarun.learning.org.tweet.core.Encoder;

public class ProducerTask<S,T> implements Runnable{
	
	private final BlockingQueue<S> source;
	private final Producer target;
	private final Encoder<T> encoder;
	private final Transformer<S,T> transformer;
	
	public ProducerTask(BlockingQueue<S> source, Producer target, Encoder<T> encoder, Transformer<S,T> transformer) {
		this.source = source;
		this.target = target;
		this.encoder = encoder;
		this.transformer = transformer;
	}
	public void run() {
		while (true) {
			S msg;
		    try {
		    	msg = source.poll(1, TimeUnit.SECONDS);
		    } catch (InterruptedException e) {
		    	System.out.println("ProducerTask: Interrupted. Quitting the task");
		    	e.printStackTrace();
		    	break;
		    }
		    if (msg == null) {
		    	System.out.println("ProducerTask: Did not receive a message in 1 seconds");
		    } else {
		    	target.send(encoder.encode(
		    				transformer.transform(msg)));
		    }
		}
	}
}
