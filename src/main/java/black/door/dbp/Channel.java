package black.door.dbp;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Consumer;

/**
 * Created by nfischer on 9/15/2015.
 */
public class Channel {
	private Set<String> handlers;
	private Consumer<Entry> handler;
	private boolean enabled;
	private String name;
	private int priority = Integer.MAX_VALUE;

	public Channel(String name){
		this.name = name.toUpperCase();
		handler = (e) -> {};
		handlers = new ConcurrentSkipListSet<>();
		enabled = true;
	}

	public int getPriority() {
		return priority;
	}

	public Channel setPriority(int priority) {
		this.priority = priority;
		return this;
	}

	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	/**
	 * Registers a handler to this channel. Be careful not to register the same
	 * handler multiple times with this method. If unsure,
	 * registerHandler(Consumer<Entry>, String) is safer.
	 * @param handler
	 * @return
	 */
	public Channel registerHandler(Consumer<Entry> handler){
		this.handler = this.handler.andThen(handler);
		return this;
	}

	/**
	 * Registers a handler with a given name. Has the same effect as
	 * registerHandler(Consumer<Entry>) but provides an assertion that no handler
	 * has yet been registered to this Channel with that name.
	 * @param handler
	 * @param name
	 * @return
	 */
	public Channel registerHandler(Consumer<Entry> handler, String name){
		String namee = name.toLowerCase();
		assert !handlers.contains(namee)
				: "A handler with this name has already been registered.";
		registerHandler(handler);
		handlers.add(namee);
		return this;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public Channel enable(){
		return setEnabled(true);
	}

	public Channel disable(){
		return setEnabled(false);
	}

	public Channel setEnabled(boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	public void log(Object message){
		log(message, null);
	}

	public void log(Object message, Object... extras){
		if(enabled)
			handler.accept( new Entry(name, message, extras));
	}
}
