package docu.test.arkiv.exception;

public class XmlParsingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -636825938577468220L;
	
	public XmlParsingException(){
		super();
	}

	public XmlParsingException(String message){
		super(message);
	}
	
	public XmlParsingException(Throwable cause){
		super(cause);
	}
	
	public XmlParsingException(String message, Throwable cause){
		super(message, cause);
	}
}
