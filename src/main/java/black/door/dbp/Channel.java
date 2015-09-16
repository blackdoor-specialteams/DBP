package black.door.dbp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Created by nfischer on 9/15/2015.
 */
public class Channel {
	private Map<String, Consumer<Entry>> handlers;
	private boolean enabled;
	private String name;
	private int priority = Integer.MAX_VALUE;

	public Channel(String name){
		this.name = name.toUpperCase();
		handlers = new ConcurrentHashMap<>();
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

	public Channel registerHandler(String name, Consumer<Entry> handler){
		String n = name.toLowerCase();
		assert !handlers.containsKey(n) : "This channel already has a handler" +
				" with that name";
		handlers.put(n, handler);
		return this;
	}

	public Consumer<Entry> getHandler(String name){
		return handlers.get(name.toLowerCase());
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
		if(enabled){
			Entry entry = new Entry(name, message, extras);
			handlers.entrySet().stream().forEach(
					e -> e.getValue().accept(entry)
			);
		}
	}
}
