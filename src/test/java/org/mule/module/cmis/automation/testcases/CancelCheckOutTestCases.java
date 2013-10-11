/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

package org.mule.module.cmis.automation.testcases;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.api.processor.MessageProcessor;
import org.mule.module.cmis.VersioningState;

public class CancelCheckOutTestCases extends CMISTestParent {

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		try {
			testObjects = (HashMap<String, Object>) context.getBean("cancelCheckOut");
			
			String rootFolderId = rootFolderId();
			String filename = testObjects.get("filename").toString();
			String mimeType = testObjects.get("mimeType").toString();
			String content = testObjects.get("content").toString();
			String objectType = testObjects.get("objectType").toString();
			Map<String, Object> propertiesRef = (Map<String, Object>) testObjects.get("propertiesRef");
			VersioningState versioningState = (VersioningState) testObjects.get("versioningState");
			
			ObjectId documentObjectId = createDocumentById(rootFolderId, filename, content, mimeType, versioningState, objectType, propertiesRef);
			testObjects.put("documentId", documentObjectId.getId());
			
			checkOut(documentObjectId.getId());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Category({RegressionTests.class})
	@Test
	public void testCancelCheckOut() {
		try {
			String documentId = (String) testObjects.get("documentId");
			
			MessageProcessor flow = lookupFlowConstruct("cancel-check-out");
			flow.process(getTestEvent(testObjects));

			ItemIterable<Document> checkedOutDocs = getCheckedOutDocuments();		
			for (Document doc : checkedOutDocs) {
				assertFalse(doc.getId().equals(documentId));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@After
	public void tearDown() {
		try {
			String documentId = (String) testObjects.get("documentId");
			delete(documentId, true);
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
