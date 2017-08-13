package docu.test.arkiv.exception;

public class ImportException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ImportException(){
		super();
	}

	public ImportException(String message){
		super(message);
	}
	
	public ImportException(Throwable cause){
		super(cause);
	}
	
	public ImportException(String message, Throwable cause){
		super(message, cause);
	}
}
