package black.door.dbp;

import black.door.dbp.handlers.StandardErrorHandler;
import black.door.dbp.handlers.StandardOutHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static black.door.dbp.StandardChannelName.*;

/**
 * Created by nfischer on 9/14/2015.
 */
public enum DBP {
	INSTANCE;

	private Map<String, Channel> channels;

	DBP(){
		channels = new ConcurrentHashMap<>();

		//region DEV
		Channel dev = getStdOutChannel(DEV);
		//endregion

		//region DEBUG
		Channel debug = getStdOutChannel(DEBUG);
		//endregion

		//region INFO
		Channel info = getStdOutChannel(INFO);
		//endregion

		//region DEMO
		Channel demo = getStdOutChannel(DEMO);
		//endregion

		//region WARNING
		Channel warning = getStdErrChannel(WARNING);
		//endregion

		//region ERROR
		Channel error = getStdErrChannel(ERROR);
		//endregion

		channels.put(dev.getName(), dev);
		channels.put(debug.getName(), debug);
		channels.put(info.getName(), info);
		channels.put(demo.getName(), demo);
		channels.put(warning.getName(), warning);
		channels.put(error.getName(), error);

		channels.entrySet().stream()
				.filter(e -> e.getValue().getPriority() < INFO.ordinal())
				.forEach(e -> e.getValue().disable());
	}

	private static Channel getStdOutChannel(StandardChannelName name){
		Channel c = new Channel(name.name());
		c.registerHandler("stdout", new StandardOutHandler());
		c.setPriority(name.ordinal());
		return c;
	}

	private static Channel getStdErrChannel(StandardChannelName name){
		Channel c = new Channel(name.name());
		c.registerHandler("stderr", new StandardErrorHandler()
				.setPrintingLine(true));
		c.setPriority(name.ordinal());
		return c;
	}

	public static Channel addChannel(Channel channel){
		String name = channel.getName().toUpperCase();
		assert !INSTANCE.channels.containsKey(name) : "A channel with this " +
				"name already exists.";
		return INSTANCE.channels.put(name, channel);
	}

	public static Channel channel(StandardChannelName name){
		return channel(name.name());
	}

	public static Channel channel(String name){
		return INSTANCE.channels.get(name.toUpperCase());
	}

	public static Channel debug(){
		return channel(StandardChannelName.DEBUG);
	}

	public static Channel info(){
		return channel(StandardChannelName.INFO);
	}

	public static Channel error(){
		return channel(StandardChannelName.ERROR);
	}

	public static Map<String, Channel> getEnabledChannels(){
		return INSTANCE.channels.entrySet().stream()
				.filter(e -> e.getValue().isEnabled())
				.collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue()));
	}

	/**
	 * Disable all channels which have a priority below the given level's channel
	 * @param level
	 * @return
	 */
	public static DBP disableChannelsBelow(StandardChannelName level){
		INSTANCE.channels.entrySet().stream()
			.filter(e -> e.getValue().getPriority() < level.ordinal())
			.forEach(e -> e.getValue().disable());
		return INSTANCE;
	}

	/**
	 * Enable all channels which have a priority as high as level or higher
	 * @param level
	 * @return
	 */
	public static DBP enableChannelAndAbove(StandardChannelName level){
		INSTANCE.channels.entrySet().stream()
				.filter(e -> e.getValue().getPriority() >= level.ordinal())
				.forEach(e -> e.getValue().enable());
		return INSTANCE;
	}

}
