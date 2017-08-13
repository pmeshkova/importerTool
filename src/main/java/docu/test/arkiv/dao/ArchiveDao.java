package docu.test.arkiv.dao;

import java.util.List;

import docu.test.arkiv.service.ArchiveParentType;
import docu.test.generated.entities.arkivstruktur.Arkiv;
import docu.test.generated.entities.arkivstruktur.Arkivdel;
import docu.test.generated.entities.arkivstruktur.Arkivskaper;
import docu.test.generated.entities.arkivstruktur.Basisregistrering;
import docu.test.generated.entities.arkivstruktur.Dokumentbeskrivelse;
import docu.test.generated.entities.arkivstruktur.Dokumentobjekt;
import docu.test.generated.entities.arkivstruktur.Mappe;
import docu.test.generated.entities.arkivstruktur.Registrering;

public interface ArchiveDao {

	public static final String SCHEMA = "arkivstruktur";
	public static final String INSERT_ARCHIVE = "insert_arkiv";
	public static final String INSERT_CHILD_ARCHIVE = "insert_arkiv_with_parent_id";
	public static final String INSERT_ARCHIVEDEL = "insert_arkivdel";
	public static final String INSERT_FOLDER = "insert_mappe";
	public static final String INSERT_BASE_REFISTERING = "insert_base_registrering";
	public static final String INSERT_REFISTERING = "insert_registrering";
	public static final String INSERT_DOCUMENT_OBJECT = "insert_dokument_objekt";
	public static final String INSERT_DOKUMENTBESKRIVELSE = "insert_dokumentbeskrivelse";
	public static final String INSERT_USER = "insert_arkivskaper";

	public int insertArchiveMetadata(final Arkiv archive);

	public void insertUser(final List<Arkivskaper> users);

	public int insertArchiveMetadata(final Arkiv archive, final int parentArchiveId);

	public int insertArchivedelMetadata(final Arkivdel arkivDel, final int archiveId);

	public void insertFolderMetadata(final Mappe folder, final String parentId, final ArchiveParentType parentType);

	public int insertBaseRegisterMetadata(final Basisregistrering baseregistrering, final String parentID,
			final ArchiveParentType parentType);

	public int insertRegisterMetadata(Registrering registrering, String parrentId, final ArchiveParentType parentType);

	public int insertDokumentbeskrivelseMetadata(final Dokumentbeskrivelse dokumentbeskrivelse, final int parentId);

	public int insertDocumentObject(final Dokumentobjekt documentObject, final int dokumentbeskrivelseId);
}
