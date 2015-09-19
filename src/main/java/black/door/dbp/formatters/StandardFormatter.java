package black.door.dbp.formatters;

import black.door.dbp.Entry;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.function.Function;

/**
 * Created by nfischer on 9/18/2015.
 */
public class StandardFormatter implements Function<Entry, CharSequence> {

	private boolean printingLine = false;

	public boolean isPrintingLine() {
		return printingLine;
	}

	public StandardFormatter setPrintingLine(boolean printingLine) {
		this.printingLine = printingLine;
		return this;
	}

	@Override
	public CharSequence apply(Entry entry) {
		String time;
		String header;
		StackTraceElement line = null;
		StringBuilder out = new StringBuilder();

		time = OffsetDateTime.ofInstant(
				Instant.ofEpochSecond(entry.getTimestamp()), ZoneOffset.UTC)
				.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

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
				out.append(header + scan.nextLine());
				if (printingLine) {
					out.append(" at ");
					out.append(line);
				}
				out.append('\n');
			}
		}

		if(entry.getExtras() != null)
			for(Object extra : entry.getExtras()){
				out.append(header);
				out.append(extra);
				out.append('\n');
			}
		return out;
	}
}
