package black.door.dbp.formatters;

import black.door.dbp.Entry;
import com.google.gson.Gson;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by nfischer on 9/19/2015.
 */
public class JsonFormatter implements Function<Entry, CharSequence> {

	@Override
	public CharSequence apply(Entry entry) {
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", OffsetDateTime.ofInstant(
				Instant.ofEpochSecond(entry.getTimestamp()), ZoneOffset.UTC)
				.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

		body.put("channel", entry.getChannel());
		String l = null;
		for(StackTraceElement e :entry.getStack()){
			if(!String.valueOf(e).startsWith("black.door.dbp")){
				l = String.valueOf(e);
				break;
			}
		}
		if(l != null)
			body.put("line", l);

		if(entry.getMessage() instanceof Map){
			body.put("message", entry.getMessage());
		}else{
			body.put("messageString", String.valueOf(entry.getMessage()));
		}

		if(entry.getExtras() != null) {
			List<String> extras = Arrays.asList(entry.getExtras()).stream()
					.map(String::valueOf)
					.collect(Collectors.toCollection(LinkedList::new));
			body.put("extras", extras);
		}

		return new Gson().toJson(body);
	}
}
