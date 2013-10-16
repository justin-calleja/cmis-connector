/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

package org.mule.module.cmis.automation.testcases;

import static org.junit.Assert.fail;

import java.util.HashMap;

import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class DeleteTestCases extends CMISTestParent {
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		try {
			testObjects = (HashMap<String, Object>) context.getBean("delete");
			testObjects.put("parentObjectId", rootFolderId());
			ObjectId result = createFolder((String) testObjects.get("folderName"), (String) testObjects.get("parentObjectId"));
			
			testObjects.put("objectId", result.getId());
			testObjects.put("cmisObjectRef", getObjectById(result.getId()));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Category({SmokeTests.class, RegressionTests.class})
	@Test
	public void testDelete() {
		try {
			String objectId = (String) testObjects.get("objectId");
			Boolean allVersions = (Boolean) testObjects.get("allVersions");
			
			delete(objectId, allVersions);			
			
			// expect getObjectById to throw exception
			getObjectById(objectId);
			fail("Object should not have been found");
		} catch (Exception e) {
			if (!(e.getCause() instanceof CmisObjectNotFoundException)) {
				e.printStackTrace();
				fail();
			}
		}
	}
	
	@Category({SmokeTests.class, RegressionTests.class})
	@Test
	public void testDelete_with_cmisObjectRef() {
		try {
			String objectId = (String) testObjects.get("objectId");
			Boolean allVersions = (Boolean) testObjects.get("allVersions");
			Object cmisObjectRef = testObjects.get("cmisObjectRef");
			
			delete(objectId, cmisObjectRef, allVersions);

			// expect getObjectById to throw exception
			getObjectById(objectId);
			fail("Object should not have been found");
		} catch (Exception e) {
			if (!(e.getCause() instanceof CmisObjectNotFoundException)) {
				e.printStackTrace();
				fail();
			}
		}
	}
}
