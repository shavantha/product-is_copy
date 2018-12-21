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

package org.wso2.identity.scenarios.test.scim2;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.message.BasicHeader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.identity.scenarios.commons.util.Constants;

import static org.testng.Assert.assertEquals;
import static org.wso2.identity.scenarios.commons.util.IdentityScenarioUtil.*;
import org.wso2.identity.scenarios.commons.ProcessJsonFile;
import org.wso2.identity.scenarios.commons.SCIMProvisioningTestBase;


public class ProvisionedUserDeleteSelfSCIM2TestCase extends SCIMProvisioningTestBase {

    private CloseableHttpClient client;
    private String scimUsersEndpoint;
    private final String SEPARATOR = "/";
    private String userId;
    private String userEndPoint;
    private String fileName = "userdelete.json";


    HttpResponse response;
    JSONObject responseObj;

    @BeforeClass(alwaysRun = true)
    public void testInit() throws Exception {

        setKeyStoreProperties();
        client = HttpClients.createDefault();
        super.init();
        scimUsersEndpoint = backendURL + SEPARATOR +  Constants.SCIMEndpoints.SCIM2_ENDPOINT + SEPARATOR +
                Constants.SCIMEndpoints.SCIM_ENDPOINT_USER;
        createUser();
    }

    private void createUser() throws Exception {

        ProcessJsonFile.readFile(fileName);

        response = provisionUser(backendURL, ProcessJsonFile.getJsonObject(),
                Constants.SCIMEndpoints.SCIM2_ENDPOINT,
                Constants.SCIMEndpoints.SCIM_ENDPOINT_USER, ADMIN_USERNAME, ADMIN_PASSWORD);
        assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED, "User has not been created " +
                "successfully");
    }


    @Test(description = "1.1.2.1.2.21")
    public void testDeleteSelf() throws Exception {

        JSONObject responseObj = getJSONFromResponse(response);
        userId = responseObj.get(SCIMConstants.ID_ATTRIBUTE).toString();

        userEndPoint = backendURL + SEPARATOR  + Constants.SCIMEndpoints.SCIM2_ENDPOINT + SEPARATOR +
                Constants.SCIMEndpoints.SCIM_ENDPOINT_USER + SEPARATOR + userId;

        response = deleteUserRequest(client,userEndPoint,getUnauthorizeHeaders());
        assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_FORBIDDEN, "User has been " +
                "deleted successfully");
    }

    @AfterClass(alwaysRun = true)
    public void cleanUp() throws Exception {

        response = deleteUser(backendURL, userId, Constants.SCIMEndpoints.SCIM2_ENDPOINT,
                Constants.SCIMEndpoints.SCIM_ENDPOINT_USER, ADMIN_USERNAME, ADMIN_PASSWORD);
        assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_NO_CONTENT, "User has been " +
                "deleted successfully");
    }


    public static HttpResponse deleteUserRequest(HttpClient client, String url, Header[] headers) throws Exception {

        HttpDelete request = new HttpDelete(url);
        if (headers != null) {
            request.setHeaders(headers);
        }

        return client.execute(request);
    }

    private static Header[] getUnauthorizeHeaders()throws org.json.simple.parser.ParseException, java.io.IOException {

        Header[] headers = {
                new BasicHeader(HttpHeaders.CONTENT_TYPE, Constants.CONTENT_TYPE_APPLICATION_JSON),
                new BasicHeader(HttpHeaders.AUTHORIZATION,
                        constructBasicAuthzHeader(ProcessJsonFile.getJsonObject().get("userName").toString(),
                                ProcessJsonFile.getJsonObject().get("password").toString()))
        };
        return headers;
    }


}
