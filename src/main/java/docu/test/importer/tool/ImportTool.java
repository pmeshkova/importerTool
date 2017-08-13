package docu.test.importer.tool;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import docu.test.arkiv.service.ArchiveService;
import docu.test.importer.tool.configuration.ImporterToolConfig;

public class ImportTool 
{
	private final static Logger log = Logger.getLogger(ArchiveService.class.getName());
	
	public static void main( String[] args )
    {
		if(args.length == 0){
			log.info("Can not find xml file. Please provide file path");
		}else{
	    	@SuppressWarnings("resource")
			ApplicationContext ctx = new AnnotationConfigApplicationContext(ImporterToolConfig.class);
	    	
	    	ArchiveService archiveService = (ArchiveService) ctx.getBean("archiveService");
	    	
	    	try {
				archiveService.proceedArchive(args[0]);
			} catch (Exception e) {
				log.error(e);
			}
		}
    }
}
