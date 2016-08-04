package tarun.learning.org.tweet.core;

import org.apache.avro.specific.SpecificRecordBase;

import com.twitter.bijection.Injection;
import com.twitter.bijection.avro.SpecificAvroCodecs;

import scala.reflect.ClassTag;


public class AvroBinaryEncoder<T extends SpecificRecordBase> implements Encoder<T> {
	private Injection<T, byte[]> recordInjection;
	
	public AvroBinaryEncoder(Class<T> cls) {
		ClassTag<T> classTag = scala.reflect.ClassTag$.MODULE$.apply(cls);
		recordInjection = SpecificAvroCodecs.toBinary(classTag);
	}
	@Override
	public byte[] encode(T source) {
		byte[] bytes = recordInjection.apply(source);
		return bytes;
	}
}
