package docu.test.arkiv.service;

import java.io.FileNotFoundException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.XmlMappingException;

import docu.test.arkiv.dao.ArchiveDao;
import docu.test.arkiv.exception.ImportException;
import docu.test.arkiv.exception.XmlParsingException;
import docu.test.unmarshall.XMLReader;
import docu.test.generated.entities.arkivstruktur.Arkiv;
import docu.test.generated.entities.arkivstruktur.Arkivdel;
import docu.test.generated.entities.arkivstruktur.Basisregistrering;
import docu.test.generated.entities.arkivstruktur.Dokumentbeskrivelse;
import docu.test.generated.entities.arkivstruktur.Dokumentobjekt;
import docu.test.generated.entities.arkivstruktur.Mappe;
import docu.test.generated.entities.arkivstruktur.Registrering;


public class ArchiveService {

	private final static Logger log = Logger.getLogger(ArchiveService.class.getName());

	@Autowired
	private XMLReader xmlReader;

	@Autowired
	private ArchiveDao archiveDao;

	public void proceedArchive(final String filePath) throws XmlParsingException, ImportException {

		log.info("Start processing archive");

		try {
			Arkiv archive = readXml(filePath);

			int archiveId = archiveDao.insertArchiveMetadata(archive);

			if (archive.getArkiv() != null) {
				saveArchive(archive.getArkiv(), archiveId);
			}

			if (archive.getArkivdel() != null) {
				saveArchivedel(archive.getArkivdel(), archiveId);
			}
		} catch (XmlMappingException | FileNotFoundException e) {
			log.error(e.getMessage(), e);
			throw new XmlParsingException(e.getMessage());
		}catch(Exception ex){
			log.error(ex.getMessage(), ex);
			throw new ImportException();
		}
	}

	private Arkiv readXml(final String filePath) throws XmlMappingException, FileNotFoundException, XmlParsingException {
		return xmlReader.readXml(filePath);
	}

	private void saveArchive(List<Arkiv> archives, int parentID) {
		for (Arkiv archive : archives) {
			int archiveId = archiveDao.insertArchiveMetadata(archive, parentID);
			if (archive.getArkiv() != null) {
				saveArchive(archive.getArkiv(), archiveId);
			}
		}
	}

	private void saveArchivedel(List<Arkivdel> arkivdels, int archiveId) {
		for (Arkivdel archiveDel : arkivdels) {
			int archiveDelId = archiveDao.insertArchivedelMetadata(archiveDel, archiveId);
			if (archiveDel.getMappe() != null) {
				saveFolder(archiveDel.getMappe(), String.valueOf(archiveDelId), ArchiveParentType.ARCHIVE_DEL);
			}

			if (archiveDel.getRegistrering() != null) {
				saveRegistering(archiveDel.getRegistrering(), String.valueOf(archiveDelId), ArchiveParentType.ARCHIVE_DEL);
			}
		}
	}

	private void saveFolder(List<Mappe> folders, String parrentID, final ArchiveParentType parentType) {
		for (Mappe folder : folders) {
			String folderId = folder.getMappeID();
			archiveDao.insertFolderMetadata(folder, parrentID, parentType);
			if (folder.getMappe() != null) {
				saveFolder(folder.getMappe(), folderId, ArchiveParentType.FOLDER);
			}

			if (folder.getRegistrering() != null) {
				saveRegistering(folder.getRegistrering(), folderId, ArchiveParentType.FOLDER);
			}
		}
	}

	private void saveRegistering(List<Registrering> registers, String parentID, ArchiveParentType parentType) {
		for (Registrering register : registers) {
			int registerId = 0;
			if (register instanceof Basisregistrering) {
				Basisregistrering basisRegistrering = (Basisregistrering) register;
				registerId = archiveDao.insertBaseRegisterMetadata(basisRegistrering, parentID, parentType);
			} else {
				registerId = archiveDao.insertRegisterMetadata(register, parentID, parentType);
			}

			if (register.getDokumentbeskrivelse() != null) {
				saveDokumentbeskrivelse(register.getDokumentbeskrivelse(), registerId);
			}
		}
	}

	private void saveDokumentbeskrivelse(List<Dokumentbeskrivelse> dokumentbeskrivelses, final int parentId) {

		for (Dokumentbeskrivelse dokumentbeskrivelse : dokumentbeskrivelses) {
			int id = archiveDao.insertDokumentbeskrivelseMetadata(dokumentbeskrivelse, parentId);
			if (dokumentbeskrivelse.getDokumentobjekt() != null) {
				saveDocumentObject(dokumentbeskrivelse.getDokumentobjekt(), id);
			}
		}
	}

	private void saveDocumentObject(List<Dokumentobjekt> documentObjects, int parentId) {
		for (Dokumentobjekt documentObject : documentObjects) {
			archiveDao.insertDocumentObject(documentObject, parentId);
		}
	}
}
