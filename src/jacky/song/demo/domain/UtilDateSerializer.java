package jacky.song.demo.domain;

import java.util.Date;

import com.activeandroid.serializer.TypeSerializer;

final public class UtilDateSerializer extends TypeSerializer {
	@Override
	public Class<?> getDeserializedType() {
		return Date.class;
	}

	@Override
	public SerializedType getSerializedType() {
	  return SerializedType.LONG;
	}

	@Override
	public Long serialize(Object data) {
		if (data == null) {
			return null;
		}

		return ((Date) data).getTime();
	}

	@Override
	public Date deserialize(Object data) {
		if (data == null) {
			return null;
		}

		return new Date((Long) data);
	}
}