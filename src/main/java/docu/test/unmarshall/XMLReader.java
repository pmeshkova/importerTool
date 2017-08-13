package docu.test.unmarshall;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.XmlMappingException;
import org.xml.sax.SAXException;

import docu.test.arkiv.exception.XmlParsingException;
import docu.test.generated.entities.arkivstruktur.Arkiv;

public class XMLReader {

	private final static Logger log = Logger.getLogger(XMLReader.class.getName());
	
	@Value("${schema.location}")
	private String schemaLocation;

	public Arkiv readXml(final String filePath) throws XmlMappingException, FileNotFoundException, XmlParsingException {
		Arkiv arkiv = null;

		JAXBElement<Arkiv> root;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Arkiv.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = sf.newSchema(new File(schemaLocation));
			jaxbUnmarshaller.setSchema(schema);
			jaxbUnmarshaller.setEventHandler(new ArchiveValidationEventHandler());
			root = (JAXBElement<Arkiv>) jaxbUnmarshaller.unmarshal(new StreamSource(new FileInputStream(filePath)),
					Arkiv.class);
			arkiv = root.getValue();
		} catch (JAXBException | SAXException e) {
			log.error(e.getMessage());
			throw new XmlParsingException(e.getMessage());
		}
		return arkiv;
	}
}
