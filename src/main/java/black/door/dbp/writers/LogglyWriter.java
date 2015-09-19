package black.door.dbp.writers;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * Created by nfischer on 9/19/2015.
 */
public class LogglyWriter implements Consumer<CharSequence> {

	private static final String PROTOCOL = "http";
	private static final String HOSTNAME = "logs-01.loggly.com";

	private String token;
	private String tag;

	public LogglyWriter(String token, String tag){
		this.token = token;
		this.tag = tag;
	}

	public LogglyWriter(String token){
		this(token, "dbp");
	}

	@Override
	public void accept(CharSequence charSequence) {
		Future<HttpResponse<String>> response = Unirest.post(PROTOCOL + "://" + HOSTNAME
				+ "/inputs/" + token + "/tag/" + tag)
				.body(charSequence.toString())
				.asStringAsync();
	}
}
