package black.door.dbp.handlers;

import black.door.dbp.Entry;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.function.Consumer;

/**
 * Created by nfischer on 9/14/2015.
 */
public class PrintStreamHandler implements Consumer<Entry>{
	private PrintStream out;
	private boolean printingLine;

	public PrintStreamHandler(PrintStream out) {
		this.out = out;
	}

	public PrintStream getOut() {
		return out;
	}

	public void setOut(PrintStream out) {
		this.out = out;
	}

	public boolean isPrintingLine() {
		return printingLine;
	}

	public PrintStreamHandler setPrintingLine(boolean printingLine) {
		this.printingLine = printingLine;
		return this;
	}

	private static SimpleDateFormat zulu;
	{
		zulu = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		zulu.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	public void accept(Entry entry) {
		String time;
		String header;
		StackTraceElement line = null;

		synchronized (zulu){
			time = zulu.format(new Date(entry.getTimestamp() * 1000));
		}
		header = "[" + time + "]" + String.format("[%-7s] ", entry.getChannel());

		if(printingLine){
			String l;
			for(StackTraceElement e :entry.getStack()){
				l = String.valueOf(e);
				if(!l.startsWith("black.door.dbp")){
					line = e;
					break;
				}
			}
		}

		try(Scanner scan = new Scanner(String.valueOf(entry.getMessage()))) {
			while (scan.hasNextLine()) {
				out.print(header + scan.nextLine());
				if (printingLine)
					out.print(" at " + line);
				out.println();
			}
		}

		if(entry.getExtras() != null)
			for(Object extra : entry.getExtras()){
				out.println(header +extra);
			}
	}
}
