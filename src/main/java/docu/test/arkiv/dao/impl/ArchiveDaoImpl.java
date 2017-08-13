package docu.test.arkiv.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import docu.test.arkiv.dao.ArchiveDao;
import docu.test.arkiv.service.ArchiveParentType;
import docu.test.generated.entities.arkivstruktur.Arkiv;
import docu.test.generated.entities.arkivstruktur.Arkivdel;
import docu.test.generated.entities.arkivstruktur.Arkivskaper;
import docu.test.generated.entities.arkivstruktur.Basisregistrering;
import docu.test.generated.entities.arkivstruktur.Dokumentbeskrivelse;
import docu.test.generated.entities.arkivstruktur.Dokumentobjekt;
import docu.test.generated.entities.arkivstruktur.Mappe;
import docu.test.generated.entities.arkivstruktur.Registrering;

public class ArchiveDaoImpl implements ArchiveDao {

	private final static Logger log = Logger.getLogger(ArchiveDaoImpl.class.getName());

	@Autowired
	DriverManagerDataSource dataSource;

	private SimpleJdbcCall insertArchiveMetadataCall;
	private SimpleJdbcCall insertChildArchiveMetadataCall;
	private SimpleJdbcCall insertArchiveDelMetadataCall;
	private SimpleJdbcCall insertFolderMetadataCall;
	private SimpleJdbcCall insertRegisterMetadataCall;
	private SimpleJdbcCall insertBaseRegisterMetadataCall;
	private SimpleJdbcCall insertDokumentbeskrivelseMetadataCall;
	private SimpleJdbcCall insertDocumentObjectMetadataCall;
	private SimpleJdbcCall insertUser;

	@Override
	public int insertArchiveMetadata(final Arkiv archive) {
		log.info("Insert archive matadata");

		insertUser(archive.getArkivskaper());
		
		insertArchiveMetadataCall = new SimpleJdbcCall(dataSource);
		Integer archiveId = insertArchiveMetadataCall.withSchemaName(SCHEMA).withFunctionName(INSERT_ARCHIVE)
				.executeFunction(Integer.class, archive.getSystemID(), archive.getTittel(),
						archive.getArkivstatus().value(), archive.getDokumentmedium().value(),
						archive.getOpprettetDato().toGregorianCalendar(), archive.getOpprettetAv(),
						archive.getAvsluttetDato().toGregorianCalendar(), archive.getAvsluttetAv());
		return archiveId;
	};

	@Override
	public int insertArchiveMetadata(Arkiv archive, int parentArchiveId) {
		log.info("Insert archive matadata");

		insertChildArchiveMetadataCall = new SimpleJdbcCall(dataSource);
		Integer archiveId = insertChildArchiveMetadataCall.withSchemaName(SCHEMA).withFunctionName(INSERT_CHILD_ARCHIVE)
				.executeFunction(Integer.class, archive.getSystemID(), archive.getTittel(),
						archive.getArkivstatus().value(), archive.getDokumentmedium().value(),
						archive.getOpprettetDato().toGregorianCalendar(), archive.getOpprettetAv(),
						archive.getAvsluttetDato().toGregorianCalendar(), archive.getAvsluttetAv(), parentArchiveId);
		return archiveId;
	}

	@Override
	public int insertArchivedelMetadata(final Arkivdel arkivDel, final int archiveId) {
		log.info("Insert archive del matadata");

		insertArchiveDelMetadataCall = new SimpleJdbcCall(dataSource);
		Integer archiveDevId = insertArchiveDelMetadataCall.withSchemaName(SCHEMA).withFunctionName(INSERT_ARCHIVEDEL)
				.executeFunction(Integer.class, arkivDel.getSystemID(), arkivDel.getTittel(),
						arkivDel.getArkivdelstatus().value(), arkivDel.getDokumentmedium().value(),
						arkivDel.getOpprettetDato().toGregorianCalendar(), arkivDel.getOpprettetAv(),
						arkivDel.getAvsluttetDato().toGregorianCalendar(), arkivDel.getAvsluttetAv(), archiveId);
		return archiveDevId;
	}

	@Override
	public void insertFolderMetadata(Mappe folder, String parentId, final ArchiveParentType parentType) {
		log.info("Insert folder matadata");

		insertFolderMetadataCall = new SimpleJdbcCall(dataSource);
		insertFolderMetadataCall.withSchemaName(SCHEMA).withFunctionName(INSERT_FOLDER).execute(folder.getSystemID(),
				Integer.valueOf(folder.getMappeID()), folder.getTittel(), folder.getOffentligTittel(),
				folder.getDokumentmedium().value(), folder.getOpprettetDato().toGregorianCalendar(),
				folder.getOpprettetAv(), folder.getAvsluttetDato().toGregorianCalendar(), folder.getAvsluttetAv(),
				parentId, parentType.name());
	}

	@Override
	public int insertDokumentbeskrivelseMetadata(Dokumentbeskrivelse dokumentbeskrivelse, int parentId) {
		log.info("Insert dokumentbeskrivelse matadata");

		insertDokumentbeskrivelseMetadataCall = new SimpleJdbcCall(dataSource);
		Integer id = insertDokumentbeskrivelseMetadataCall.withSchemaName(SCHEMA)
				.withFunctionName(INSERT_DOKUMENTBESKRIVELSE).executeFunction(Integer.class,
						dokumentbeskrivelse.getSystemID(), dokumentbeskrivelse.getDokumenttype(),
						dokumentbeskrivelse.getDokumentstatus().value(), dokumentbeskrivelse.getTittel(),
						dokumentbeskrivelse.getOpprettetDato().toGregorianCalendar(),
						dokumentbeskrivelse.getOpprettetAv(), dokumentbeskrivelse.getDokumentmedium().value(),
						dokumentbeskrivelse.getTilknyttetRegistreringSom(), dokumentbeskrivelse.getDokumentnummer(),
						dokumentbeskrivelse.getTilknyttetDato().toGregorianCalendar(),
						dokumentbeskrivelse.getTilknyttetAv(), parentId);
		return id;
	}

	@Override
	public int insertDocumentObject(Dokumentobjekt documentObject, int dokumentbeskrivelseId) {
		log.info("Insert document object matadata");

		insertDocumentObjectMetadataCall = new SimpleJdbcCall(dataSource);
		insertDocumentObjectMetadataCall.withSchemaName(SCHEMA).withFunctionName(INSERT_DOCUMENT_OBJECT).executeObject(
				Integer.class, documentObject.getVersjonsnummer(), documentObject.getVariantformat().value(),
				documentObject.getFormat(), documentObject.getOpprettetDato().toGregorianCalendar(),
				documentObject.getOpprettetAv(), documentObject.getReferanseDokumentfil(), documentObject.getSjekksum(),
				documentObject.getSjekksumAlgoritme(), documentObject.getFilstoerrelse(), dokumentbeskrivelseId);
		return 0;
	}

	@Override
	public void insertUser(List<Arkivskaper> users) {
		insertUser = new SimpleJdbcCall(dataSource);
		insertUser.withSchemaName(SCHEMA).withFunctionName(INSERT_USER);
		
		for(Arkivskaper user : users){
			insertUser(insertUser, user);
		}
	}
	
	private void insertUser(final SimpleJdbcCall jdbcCaall, Arkivskaper user){
		jdbcCaall.execute(user.getArkivskaperID(),
				user.getArkivskaperNavn(), user.getBeskrivelse() != null ? user.getBeskrivelse() : "");
	}

	@Override
	public int insertBaseRegisterMetadata(Basisregistrering baseregistrering, String parentID, final ArchiveParentType parentType) {
		insertBaseRegisterMetadataCall = new SimpleJdbcCall(dataSource);
		Integer id = insertBaseRegisterMetadataCall.withSchemaName(SCHEMA).withFunctionName(INSERT_BASE_REFISTERING).executeFunction(Integer.class,
				baseregistrering.getSystemID(), baseregistrering.getOpprettetDato().toGregorianCalendar(),
				baseregistrering.getOpprettetAv(), baseregistrering.getArkivertDato().toGregorianCalendar(),
				baseregistrering.getArkivertAv(), Integer.valueOf(baseregistrering.getRegistreringsID()),
				baseregistrering.getTittel(), baseregistrering.getOffentligTittel(),
				baseregistrering.getDokumentmedium().value(), parentID, parentType.name());
		return id;
		
	}

	@Override
	public int insertRegisterMetadata(Registrering registrering, String parentId, final ArchiveParentType parentType) {
		insertRegisterMetadataCall = new SimpleJdbcCall(dataSource);
		Integer id = insertRegisterMetadataCall.withSchemaName(SCHEMA)
				.withFunctionName(INSERT_REFISTERING).executeFunction(Integer.class, registrering.getSystemID(), registrering.getOpprettetDato().toGregorianCalendar(),
						registrering.getOpprettetAv(), registrering.getArkivertDato().toGregorianCalendar(),
						registrering.getArkivertAv(), parentId, parentType.name());
		return id;
	}
}
