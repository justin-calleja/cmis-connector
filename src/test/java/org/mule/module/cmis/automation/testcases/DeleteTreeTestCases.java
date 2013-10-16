/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

package org.mule.module.cmis.automation.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.module.cmis.VersioningState;

public class DeleteTreeTestCases extends CMISTestParent {

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		try {
			testObjects = (HashMap<String, Object>) context
					.getBean("deleteTree");
			testObjects.put("parentObjectId", rootFolderId());
			ObjectId folderObjectId = createFolder(
					(String) testObjects.get("folderName"),
					(String) testObjects.get("parentObjectId"));
			String folderId = folderObjectId.getId();

			createDocumentById(folderId, (String) testObjects.get("filename"),
					(String) testObjects.get("contentRef"),
					(String) testObjects.get("mimeType"),
					(VersioningState) testObjects.get("versioningState"),
					(String) testObjects.get("objectType"),
					(Map<String, Object>) testObjects.get("propertiesRef"));

			testObjects.put("folderId", folderId);
			CmisObject folderRef = getObjectById(folderId);
			testObjects.put("folderRef", folderRef);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Category({ SmokeTests.class, RegressionTests.class })
	@Test
	public void testDeleteTree_by_folder_id() {
		try {
			List<String> objectsFailedToDelete = deleteTreeByFolderId((String) testObjects.get("folderId"),
					(Boolean) testObjects.get("allversions"),
					(Boolean) testObjects.get("continueOnFailure"));
			assertNotNull(objectsFailedToDelete);
			assertEquals(0, objectsFailedToDelete.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Category({ SmokeTests.class, RegressionTests.class })
	@Test
	public void testDeleteTree_by_folder_ref() {
		try {
			List<String> objectsFailedToDelete = deleteTreeByFolderRef((CmisObject) testObjects.get("folderRef"),
					(Boolean) testObjects.get("allversions"),
					(Boolean) testObjects.get("continueOnFailure"));
			assertNotNull(objectsFailedToDelete);
			assertEquals(0, objectsFailedToDelete.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
