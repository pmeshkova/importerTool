package docu.test.unmarshall;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

import org.apache.log4j.Logger;

public class ArchiveValidationEventHandler implements ValidationEventHandler {

	private final static Logger log = Logger.getLogger(ArchiveValidationEventHandler.class.getName());
	
	@Override
	public boolean handleEvent(ValidationEvent event) {
	        log.info("SEVERITY:  " + event.getSeverity());
	        log.info("MESSAGE:  " + event.getMessage());
	        log.info("LINKED EXCEPTION:  " + event.getLinkedException());
	        

	        
		return (event.getSeverity() == ValidationEvent.ERROR) ? false : true;
	}

}
