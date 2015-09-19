package black.door.testdbp;

import black.door.dbp.DBP;
import black.door.dbp.StandardChannelName;
import black.door.dbp.formatters.JsonFormatter;
import black.door.dbp.handlers.LogglyHandler;
import black.door.dbp.writers.LogglyWriter;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by nfischer on 9/15/2015.
 */
public class DBPTest {

	@Test
	public void test() throws Exception{
		//DBP.info().registerHandler(new LogglyHandler("b6282d78-2c95-486c-aa42-f581591bf6bb", "test"), "loggly");
		Consumer lw = new LogglyWriter("b6282d78-2c95-486c-aa42-f581591bf6bb", "test");
		JsonFormatter jf = new JsonFormatter();
		DBP.info().registerHandler(e -> lw.accept(jf.apply(e)));

		DBP.channel("dev").log("dev\ndev");
		DBP.debug().log("debug\ndebug");
		DBP.info().log("hello world", "extra", "extra2");
		Map m = new HashMap<>();
		m.put("hello", "world");
		m.put("key", 5);
		DBP.info().log(m);
		DBP.channel("demo").log("demo\ndemo");
		Thread.sleep(10);
		DBP.channel("warning").log("warn\nwarn");
		DBP.error().log("err\n err");

		Thread.sleep(10);
		System.out.println();

		DBP.enableChannelAndAbove(StandardChannelName.DEV);
		DBP.channel("dev").log("dev\ndev");
		DBP.debug().log("debug\ndebug");
		DBP.info().log("hello world", "extra", "extra2");
		DBP.info().log(m);
		DBP.channel("demo").log("demo\ndemo");
		DBP.channel("warning").log("warn\nwarn");
		DBP.error().log("err\n err");

	}



}