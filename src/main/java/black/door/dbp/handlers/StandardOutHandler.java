package black.door.dbp.handlers;

/**
 * Created by nfischer on 9/15/2015.
 */
public class StandardOutHandler extends PrintStreamHandler{
	public StandardOutHandler() {
		super(System.out);
	}
}
