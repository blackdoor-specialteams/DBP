package black.door.dbp.handlers;

import black.door.dbp.Entry;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by nfischer on 9/15/2015.
 */
public class LogglyHandler implements Consumer<Entry> {

	private static final String PROTOCOL = "https";
	private static final String HOSTNAME = "logs-01.loggly.com";

	private String token;
	private String tag;

	public LogglyHandler(String token, String tag){
		this.token = token;
		this.tag = tag;
	}

	public LogglyHandler(String token){
		this(token, "dbp");
	}

	private static SimpleDateFormat zulu;
	{
		zulu = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		zulu.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	@Override
	public void accept(Entry entry) {
		Map<String, Object> body = new HashMap<>();
		synchronized (zulu){
			body.put("timestamp",
					zulu.format(new Date(entry.getTimestamp() * 1000)));
		}
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
					.map(o -> String.valueOf(o))
					.collect(Collectors.toCollection(LinkedList::new));
			body.put("extras", extras);
		}

		try {
			HttpResponse response = Unirest.post(PROTOCOL +"://" +HOSTNAME
					+"/inputs/" +token +"/tag/" +tag)
					.header("Content-Type", "application/json")
					.body(new Gson().toJson(body))
					.asString();
			assert response.getStatus() == 200;
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}
}
