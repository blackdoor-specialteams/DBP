package black.door.dbp;

import java.util.Arrays;

/**
 * Created by nfischer on 9/14/2015.
 */
public class Entry {
	private long timestamp;
	private String channel;
	private Object message;
	private StackTraceElement[] stack;
	private Object[] extras;

	public Entry(String channel, Object message) {
		this(channel, message, null);
	}

	public Entry(String channel, Object message, Object[] extras) {
		this.channel = channel;
		this.message = message;
		this.extras = extras;
		this.timestamp = System.currentTimeMillis() / 1000;
		this.stack = Thread.currentThread().getStackTrace();
		this.stack = Arrays.copyOfRange(stack, 1, stack.length);
	}

	public long getTimestamp() {
		return timestamp;
	}

	protected void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getChannel() {
		return channel;
	}

	protected void setChannel(String channel) {
		this.channel = channel;
	}

	public Object getMessage() {
		return message;
	}

	protected void setMessage(Object message) {
		this.message = message;
	}

	public StackTraceElement[] getStack() {
		return stack;
	}

	protected void setStack(StackTraceElement[] stack) {
		this.stack = stack;
	}

	public Object[] getExtras() {
		return extras;
	}

	protected void setExtras(Object[] extras) {
		this.extras = extras;
	}

	@Override
	public String toString() {
		return "Entry{" +
				"timestamp=" + timestamp +
				", channel='" + channel + '\'' +
				", message=" + message +
				", stack=" + Arrays.toString(stack) +
				", extras=" + Arrays.toString(extras) +
				'}';
	}
}
