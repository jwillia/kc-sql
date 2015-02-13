package co.kuali.coeus.data.migration.workflow;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.impex.xml.StreamXmlDoc;
import org.kuali.rice.core.api.impex.xml.XmlDoc;
import org.kuali.rice.core.api.impex.xml.XmlDocCollection;
import org.kuali.rice.core.api.impex.xml.XmlIngesterService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;


public class KcKewAutoIngestorService {
	
	private static final Logger LOG = Logger.getLogger(KcKewAutoIngestorService.class);
	
	private String cfgPathsToScan;
	private List<String> pathsToScan = new ArrayList<>();
	private String principalIdToIngestAs;
	private Boolean enabled;
	private DataSource riceDataSource;
	private XmlIngesterService xmlIngesterService;

	public void ingest() throws Exception {
		if (Boolean.FALSE.equals(enabled)) {
			return;
		}
		if (StringUtils.isNotBlank(cfgPathsToScan)) {
			this.pathsToScan.addAll(Arrays.asList(cfgPathsToScan.split(",")));
		}
		Map<String, Integer> newChecksums = new HashMap<>();
		try (Connection conn = riceDataSource.getConnection()) {
			conn.setAutoCommit(false);
			Map<String, Integer> previousChecksums = readChecksums(conn);
			for (String path : pathsToScan) {
				Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath*:" + path + "/**/*.xml");
				try (AutoXmlDocCollection docCollection = new AutoXmlDocCollection(new File(path))) {
					for (Resource file : resources) {
						byte[] encoded = null;
						try (BufferedInputStream is = new BufferedInputStream(file.getInputStream())) {
							encoded = IOUtils.toByteArray(is);
						}
						Integer checkSum = calculateChecksum(encoded);
						String baseName = new File(file.getFilename()).getName();
						if (previousChecksums.containsKey(baseName) && previousChecksums.get(baseName).equals(checkSum)) {
							LOG.info("Skipping " + file + " as it is unchanged.");
							continue;
						} else {
							LOG.info("Adding " + file + " for ingestion.");
							newChecksums.put(baseName, checkSum);
							docCollection.addXmlDoc(new NamedStreamXmlDoc(baseName, new ByteArrayInputStream(encoded), docCollection));
						}
					}
					if (!docCollection.getXmlDocs().isEmpty()) {
						Collection<XmlDocCollection> failed = xmlIngesterService.ingest(Collections.singletonList((XmlDocCollection)docCollection), principalIdToIngestAs);
						if (failed != null && !failed.isEmpty()) {
							throw new RuntimeException("Failed to ingest KEW files from - " + failed);
						} else {
							recordChecksums(newChecksums, previousChecksums, conn);
							previousChecksums.putAll(newChecksums);
							newChecksums.clear();
							LOG.info("Successfully ingested " + docCollection.getXmlDocs().size() + " KEW files.");
						}
					}
				}
			}
			conn.commit();
		}
	}
	
	protected Map<String, Integer> readChecksums(Connection conn) throws SQLException {
		Map<String, Integer> result = new HashMap<>();
		try (Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("select file_name, checksum from krew_auto_ingested_checksum")) {
			while (rs.next()) {
				result.put(rs.getString("file_name"), rs.getInt("checksum"));
			}
		}
		return result;
	}
	
	protected void recordChecksums(Map<String, Integer> newChecksums, Map<String, Integer> previousChecksums, Connection conn) throws SQLException {
		if (!newChecksums.isEmpty()) {
			try (PreparedStatement insertStmt = conn.prepareStatement("insert into krew_auto_ingested_checksum (file_name, checksum) values (?, ?)");
					PreparedStatement updateStmt = conn.prepareStatement("update krew_auto_ingested_checksum set checksum = ? where file_name = ?")) {
				for (Map.Entry<String, Integer> entry : newChecksums.entrySet()) {
					if (previousChecksums.containsKey(entry.getKey())) {
						updateStmt.setInt(1, entry.getValue());
						updateStmt.setString(2, entry.getKey());
						updateStmt.execute();
					} else {
						insertStmt.setString(1, entry.getKey());
						insertStmt.setInt(2, entry.getValue());
						insertStmt.execute();
					}
				}
			}
		}
	}
	
	protected class NamedStreamXmlDoc extends StreamXmlDoc {
		private String name;
		public NamedStreamXmlDoc(String name, InputStream stream, XmlDocCollection collection) {
			super(stream, collection);
			this.name = name;
		}
		public String getName() {
			return name;
		}
				
	}
	
	protected class AutoXmlDocCollection implements XmlDocCollection, AutoCloseable {
		private File file;
		private List<StreamXmlDoc> xmlDocs = new ArrayList<StreamXmlDoc>();
		
		public AutoXmlDocCollection(File file) {
			this.file = file;
		}
		
		public String toString() {
			return file.getName();
		}
		
		@Override
		public File getFile() {
			return file;
		}

		@Override
		public List<? extends XmlDoc> getXmlDocs() {
			return xmlDocs;
		}

		@Override
		public void close() throws IOException {
			for (StreamXmlDoc doc : xmlDocs) {
				doc.getStream().close();
			}
		}
		
		public void addXmlDoc(StreamXmlDoc xmlDoc) {
			xmlDocs.add(xmlDoc);
		}
		
	}
	
	protected int calculateChecksum(byte[] bytes) {
	    final CRC32 crc32 = new CRC32();
	    crc32.update(bytes);
	    return (int) crc32.getValue();
	}
    
	public DataSource getRiceDataSource() {
		return riceDataSource;
	}

	public void setRiceDataSource(DataSource riceDataSource) {
		this.riceDataSource = riceDataSource;
	}

	public List<String> getPathsToScan() {
		return pathsToScan;
	}

	public void setPathsToScan(List<String> pathsToScan) {
		this.pathsToScan = pathsToScan;
	}

	public XmlIngesterService getXmlIngesterService() {
		return xmlIngesterService;
	}

	public void setXmlIngesterService(XmlIngesterService xmlIngesterService) {
		this.xmlIngesterService = xmlIngesterService;
	}

	public String getPrincipalIdToIngestAs() {
		return principalIdToIngestAs;
	}

	public void setPrincipalIdToIngestAs(String principalIdToIngestAs) {
		this.principalIdToIngestAs = principalIdToIngestAs;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getCfgPathsToScan() {
		return cfgPathsToScan;
	}
	/**
	 * cfgPathsToScan is a comma delimieted list of classpaths that will be searched for
	 * '**\/*.xml' kew files
	 * @param cfgPathsToScan
	 */
	public void setCfgPathsToScan(String cfgPathsToScan) {
		this.cfgPathsToScan = cfgPathsToScan;
	}
}

