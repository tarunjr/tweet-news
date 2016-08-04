package tarun.learning.org.tweet.core;

import org.apache.avro.specific.SpecificRecordBase;

import com.twitter.bijection.Injection;
import com.twitter.bijection.avro.SpecificAvroCodecs;

import scala.reflect.ClassTag;

public class AvroBinaryDecoder<T extends SpecificRecordBase> implements Decoder<T> {
	
	private Injection<T, byte[]> recordInjection;
	
	public AvroBinaryDecoder(Class<T> cls) {
		ClassTag<T> classTag = scala.reflect.ClassTag$.MODULE$.apply(cls);
		recordInjection = SpecificAvroCodecs.toBinary(classTag);
	}
	@Override
	public T decode(byte[] bytes) {
		return  (T)recordInjection.invert(bytes).get();
	}
}
