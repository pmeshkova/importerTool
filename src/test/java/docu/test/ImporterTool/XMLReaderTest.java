package docu.test.ImporterTool;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.oxm.XmlMappingException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import docu.test.arkiv.exception.XmlParsingException;
import docu.test.unmarshall.XMLReader;
import docu.test.generated.entities.arkivstruktur.Arkiv;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class)
public class XMLReaderTest {
	
	private static final String VALID_FILE= "src/test/resources/arkivstruktur.xml";
	private static final String INVALID_FILE = "src/test/resources/arkivstrukturInvalid.xml";
	private static final String INVALID_PATH = "src/test/resources/arkivstruktur1.xml";
	
    @Configuration
    @PropertySource("classpath:test.properties")
    static class ContextConfiguration {
        // this bean will be injected into the XMLReaderTest class
        @Bean
        public XMLReader xmlReader() {
        	XMLReader xmlReader = new XMLReader();
            return xmlReader;
        }
    }

	@Autowired
	private XMLReader xmlReader;
	
	@Test
	public void readXmlTest() throws XmlMappingException, FileNotFoundException, XmlParsingException{
			Arkiv arkiv = xmlReader.readXml(VALID_FILE);
			assertTrue("Expected element should not be null", arkiv != null);
	}
	
	@Test(expected = XmlParsingException.class)
	public void readXmlWithInvalidFileTest() throws XmlMappingException, FileNotFoundException, XmlParsingException{
		 xmlReader.readXml(INVALID_FILE);
	}
	
	@Test(expected = FileNotFoundException.class)
	public void readXmlWithInvalidFilePathTest() throws XmlMappingException, FileNotFoundException, XmlParsingException{
			 xmlReader.readXml(INVALID_PATH);
	}
}
