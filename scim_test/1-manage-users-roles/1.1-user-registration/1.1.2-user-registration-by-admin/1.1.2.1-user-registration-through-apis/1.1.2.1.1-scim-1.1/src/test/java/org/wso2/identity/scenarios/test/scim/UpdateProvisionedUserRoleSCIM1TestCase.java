/*
 *  Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.identity.scenarios.test.scim;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.identity.scenarios.commons.ProcessJsonFile;
import org.wso2.identity.scenarios.commons.SCIMProvisioningTestBase;
import org.wso2.identity.scenarios.commons.util.Constants;
import org.apache.http.client.methods.HttpPut;

import static org.testng.Assert.assertEquals;
import static org.wso2.identity.scenarios.commons.util.IdentityScenarioUtil.getJSONFromResponse;
import static org.wso2.identity.scenarios.commons.util.IdentityScenarioUtil.constructBasicAuthzHeader;
import static org.wso2.identity.scenarios.commons.util.IdentityScenarioUtil.sendPostRequestWithJSON;


public class UpdateProvisionedUserRoleSCIM1TestCase extends SCIMProvisioningTestBase {

    private CloseableHttpClient client;
    private String userNameResponse;
    private String roleNameFromResponse;
    private String userId;
    private String groupId;
    private String groupURL;
    private String firstName;
    private String SEPARATOR = "/";
    private String GROUP_NAME_ATTRIBUTE = "Groups";
    private String ROLE_NAME = "Engineering1";
    private String DISPLAY = "display";
    private String MEMBERS = "members";
    private String fileName = "provision.json";

    JSONObject responseObj;
    JSONObject groupObject;

    HttpResponse response,roleResponse;

    @BeforeClass(alwaysRun = true)
    public void testInit() throws Exception {

        setKeyStoreProperties();
        client = HttpClients.createDefault();
        super.init();
        createUser();
    }

    public void createUser() throws Exception {

        ProcessJsonFile.readFile(fileName);

        response = provisionUser(backendURL, ProcessJsonFile.getJsonObject(),
                Constants.SCIMEndpoints.SCIM1_ENDPOINT,
                Constants.SCIMEndpoints.SCIM_ENDPOINT_USER, ADMIN_USERNAME, ADMIN_PASSWORD);
        assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED, "User has not been created " +
                "successfully");

        userNameResponse = ProcessJsonFile.getJsonObject().get(SCIMConstants.USER_NAME_ATTRIBUTE).toString();
        assertEquals(userNameResponse, SCIMConstants.USERNAME, "username not found");

        firstName = ProcessJsonFile.getJsonObject().get(SCIMConstants.NAME_ATTRIBUTE).toString();
        assertEquals(firstName.substring(14,19),SCIMConstants.GIVEN_NAME_CLAIM_VALUE,"The given first name " +
                "does not exist");
        createRole();
    }

    public void createRole() throws Exception {

        groupObject = new JSONObject();

        JSONArray schemas = new JSONArray();
        groupObject.put(SCIMConstants.SCHEMAS_ATTRIBUTE, schemas);
        groupObject.put(SCIMConstants.ROLE_DISPLAY_NAME_ATTRIBUTE, ROLE_NAME);

        groupURL = backendURL + SEPARATOR  + Constants.SCIMEndpoints.SCIM1_ENDPOINT + SEPARATOR +
                GROUP_NAME_ATTRIBUTE + SEPARATOR;

        roleResponse = sendPostRequestWithJSON(client,groupURL,groupObject,getCommonHeaders());
        roleNameFromResponse = groupObject.get(SCIMConstants.ROLE_DISPLAY_NAME_ATTRIBUTE).toString();
        assertEquals(roleNameFromResponse, ROLE_NAME,"Expected Role name does not exist");
    }

    @Test(description = "1.1.2.1.1.16")
    public void testAddMemberToRole() throws Exception {

        responseObj = getJSONFromResponse(this.response);
        userId = responseObj.get(SCIMConstants.ID_ATTRIBUTE).toString();
        responseObj = getJSONFromResponse(this.roleResponse);
        groupId = responseObj.get(SCIMConstants.ID_ATTRIBUTE).toString();

        JSONArray schemas = new JSONArray();
        groupObject.put(SCIMConstants.SCHEMAS_ATTRIBUTE, schemas);
        groupObject.put(SCIMConstants.ROLE_DISPLAY_NAME_ATTRIBUTE, ROLE_NAME);

        JSONArray members = new JSONArray();
        for (int i = 0; i < 1; i++) {
            JSONObject member = new JSONObject();
            member.put(DISPLAY, ProcessJsonFile.getJsonObject().get("userName"));
            member.put(SCIMConstants.VALUE_PARAM, userId);
            members.add(member);
        }

        groupObject.put(MEMBERS, members);

        groupURL = backendURL + SEPARATOR  + Constants.SCIMEndpoints.SCIM1_ENDPOINT + SEPARATOR +
                GROUP_NAME_ATTRIBUTE + SEPARATOR + groupId;

        updateRoleRequest(client,groupURL,groupObject,getCommonHeaders());
        roleNameFromResponse = groupObject.get(SCIMConstants.ROLE_DISPLAY_NAME_ATTRIBUTE).toString();
        assertEquals(roleNameFromResponse,ROLE_NAME);
    }

    @AfterClass(alwaysRun = true)
    public void cleanUp() throws Exception {

        deleteUser(backendURL, userId, Constants.SCIMEndpoints.SCIM1_ENDPOINT,
                Constants.SCIMEndpoints.SCIM_ENDPOINT_USER, ADMIN_USERNAME, ADMIN_PASSWORD);

        groupURL = backendURL + SEPARATOR  + Constants.SCIMEndpoints.SCIM1_ENDPOINT + SEPARATOR +
                SCIMConstants.GROUP_ENDPOINT + SEPARATOR + groupId;
        deleteRoleRequest(client,groupURL,getCommonHeaders());
    }

    public static HttpResponse deleteRoleRequest(HttpClient client, String url, Header[] headers) throws Exception {
        HttpDelete request = new HttpDelete(url);
        if (headers != null) {
            request.setHeaders(headers);
        }
        return client.execute(request);
    }

    public static HttpResponse updateRoleRequest(HttpClient client, String url, JSONObject jsonObject,
                                               Header[] headers) throws Exception {

        HttpPut request = new HttpPut(url);
        if (headers != null) {
            request.setHeaders(headers);
        }

        request.setEntity(new StringEntity(jsonObject.toString()));
        return client.execute(request);
    }

    private static Header[] getCommonHeaders() {

        Header[] headers = {
                new BasicHeader(HttpHeaders.CONTENT_TYPE, Constants.CONTENT_TYPE_APPLICATION_JSON),
                new BasicHeader(HttpHeaders.AUTHORIZATION, constructBasicAuthzHeader(ADMIN_USERNAME, ADMIN_PASSWORD))
        };
        return headers;
    }

}
