package black.door.dbp.handlers;

/**
 * Created by nfischer on 9/15/2015.
 */
public class StandardErrorHandler extends PrintStreamHandler {
	public StandardErrorHandler() {
		super(System.err);
	}
}
