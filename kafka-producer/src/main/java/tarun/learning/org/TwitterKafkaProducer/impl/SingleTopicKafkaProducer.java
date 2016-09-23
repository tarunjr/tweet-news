package tarun.learning.org.TwitterKafkaProducer.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.google.common.io.Resources;

import tarun.learning.org.TwitterKafkaProducer.interfaces.Producer;

public class SingleTopicKafkaProducer implements Producer{

	private KafkaProducer<String, byte[]> producer;
	private String topic;

	public SingleTopicKafkaProducer(String topic) {
		this.topic = topic;
			  // set up the producer
      	producer = null;
        try (InputStream props = Resources.getResource("producer.props").openStream()) {
            Properties properties = new Properties();
            properties.load(props);
            producer = new KafkaProducer<>(properties);
        } catch (IOException e) {
			producer = null;
			e.printStackTrace();
		}
	}
	@Override
	public void send(byte[] bytes) {
		if(producer == null) {
			return;
		}
		producer.send(new ProducerRecord<String, byte[]>(topic, bytes));
	}

	public void close() {
		if (producer != null) {
			producer.close();
			producer =  null;
		}
	}

}
