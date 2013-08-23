package org.mule.module.cmis.automation.testcases;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.MuleEvent;
import org.mule.api.processor.MessageProcessor;
import org.mule.module.cmis.VersioningState;

public class GetAllVersionsTestCases extends CMISTestParent {

	@Before
	public void setUp() {
		try {
			testObjects = (Map<String, Object>) context.getBean("getAllVersions");

			String filename = (String) testObjects.get("filename");
			String folderPath = (String) testObjects.get("folderPath");
			String mimeType = (String) testObjects.get("mimeType");
			VersioningState versioningState = (VersioningState) testObjects.get("versioningState");
			String objectType = (String) testObjects.get("objectType");
			Boolean force = (Boolean) testObjects.get("force");
			String content = (String) testObjects.get("content");
			Map<String, Object> properties = (Map<String, Object>) testObjects.get("propertiesRef");

			ObjectId documentId = createDocumentByPathFromContent(folderPath, filename, content, mimeType, versioningState, objectType, properties, force);
			testObjects.put("documentId", documentId);
			
			List<HashMap<String, Object>> versions = (List<HashMap<String, Object>>) testObjects.get("versions");
			for (HashMap<String, Object> version : versions) {
				String checkInContent = (String) version.get("content");
				String checkInComment = (String) version.get("checkinComment");
				Boolean major = (Boolean) version.get("major");
				
				ObjectId checkInId = checkIn(checkInComment, documentId.getId(), filename, checkInContent, mimeType, major, properties);
				checkOut(documentId.getId());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Category({RegressionTests.class})
	@Test
	public void testGetAllVersions() {
		try {
			Map<String, Object> versions = (Map<String, Object>) testObjects.get("versions");
			
			MessageProcessor flow = lookupFlowConstruct("get-all-versions");
			MuleEvent response = flow.process(getTestEvent(testObjects));
			
			List<Document> documentVersions = (List<Document>) response.getMessage().getPayload();
			assertTrue(documentVersions.size() == versions.size());
			
			fail("Revisit test when other processors are fixed");
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@After
	public void tearDown() {
		try {
			String documentId = (String) testObjects.get("documentId");
			delete(getObjectById(documentId), documentId, true);
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
}