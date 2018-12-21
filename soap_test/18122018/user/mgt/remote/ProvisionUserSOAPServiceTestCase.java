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

package org.wso2.identity.scenarios.test.user.mgt.remote;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.apache.http.impl.client.HttpClients;
import org.wso2.carbon.um.ws.api.stub.RemoteUserStoreManagerServiceStub;
import java.rmi.RemoteException;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.transport.http.HTTPConstants;
import org.testng.annotations.Test;
import org.wso2.carbon.um.ws.api.stub.RemoteUserStoreManagerServiceUserStoreExceptionException;
import org.apache.axis2.transport.http.HttpTransportProperties;

import org.wso2.carbon.um.ws.api.stub.PermissionDTO;
import org.wso2.carbon.CarbonConstants;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Options;
import org.wso2.carbon.um.ws.api.stub.ClaimValue;
import java.util.Properties;
import java.io.FileInputStream;
import org.wso2.identity.scenarios.test.common.SOAPTestBase;

public class ProvisionUserSOAPServiceTestCase extends SOAPTestBase {

    private static Log log = LogFactory.getLog(ProvisionUserSOAPServiceTestCase.class);
    private RemoteUserStoreManagerServiceStub stub = null;
    private SOAPTestBase soapTestBase;

    private String ROLE_NAME;
    private String USER_NAME = "shavantha";
    private String USER_PASSWORD ="welcome";

    private String SERVICE_NAME;
    private String userName = "admin";
    private String passWord = "admin";
    private String sessionCookie="";
    private String endPoint;
    private String[] users = {"admin"};

    private CloseableHttpClient client;

    private String claimURI;
    private String claimVal;
    private String claimURL2;
    private String claimVal2;
    private String claimURL3;
    private String claimVal3;
    private static String HOST;
    private static String SERVER_PORT;
    private static String PROTOCOL;
    private static String CARBON_HOME;
    private static String ADMIN_USERNAME ="admin";
    private static String ADMIN_PASSWORD = "admin";

    private String service="RemoteUserStoreManagerService";
    private String extension="?wsdl";
    private String fileName ="data.properties";
    private String trustStore;
    ConfigurationContext configContext;
    RemoteUserStoreManagerServiceStub adminStub;
    boolean  authenticate;


    @BeforeClass(alwaysRun = true)
    public void testInit() throws Exception {

        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String configPath = rootPath + fileName;

        Properties prop = new Properties();
        prop.load(new FileInputStream(configPath));


        PROTOCOL = prop.getProperty("PROTOCOL");
        HOST = prop.getProperty("HOST");
        SERVER_PORT = prop.getProperty("PORT");
        CARBON_HOME = prop.getProperty("CARBON_HOME");
        SERVICE_NAME = prop.getProperty("SERVICE_NAME");
        claimURI = prop.getProperty("CLAIM_URL");
        claimVal = prop.getProperty("CLAIM_VAL");
        claimURL2 = prop.getProperty("CLAIM_URL2");
        claimVal2 = prop.getProperty("CLAIM_VAL2");
        claimURL3 = prop.getProperty("CLAIM_URL3");
        claimVal3 = prop.getProperty("CLAIM_VAL3");
        ROLE_NAME = prop.getProperty("ROLE_NAME");


        endPoint = PROTOCOL +"://"+HOST+":"+SERVER_PORT+"/services/"+service+""+extension;
/*        setKeyStoreProperties();*/
        client = HttpClients.createDefault();

        addUserRole();
    }

    public void addUserRole()throws Exception {

        endPoint = PROTOCOL +"://"+HOST+":"+SERVER_PORT+"/services/"+service+""+extension;
        soapTestBase =new SOAPTestBase(endPoint,ADMIN_USERNAME,ADMIN_PASSWORD);

        configContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem( null, null);
        trustStore = CARBON_HOME + "/repository/resources/security/wso2carbon.jks";
        System.setProperty("javax.net.ssl.trustStore",  trustStore );
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");

        adminStub = new RemoteUserStoreManagerServiceStub(configContext, endPoint);

        ServiceClient client = adminStub._getServiceClient();
        Options option = client.getOptions();

        option.setProperty(HTTPConstants.COOKIE_STRING, null);

        HttpTransportProperties.Authenticator auth = new HttpTransportProperties.Authenticator();
        auth.setUsername(userName);
        auth.setPassword(passWord);
        auth.setPreemptiveAuthentication(true);
        option.setProperty(org.apache.axis2.transport.http.HTTPConstants.AUTHENTICATE, auth);
        option.setManageSession(true);
        authenticate = false;

        try{
            authenticate = adminStub.authenticate(userName, passWord);
        } catch (Exception e){
            e.printStackTrace();
        }

        if(authenticate){
            log.debug("User is authenticated successfully");
        } else {
            log.error("User is authentication failed");
        }

        String[] userList = new String[2];
        userList[0] = new String("admin");

        String[] permissions = {
                "/permission/admin/login",
                "/permission/admin/manage",
                "/permission/admin/configure/security/usermgt/profiles"};

        PermissionDTO permissionDTO;
        PermissionDTO[] permissionDTOs = new PermissionDTO[permissions.length];

        for (int i = 0; i < permissions.length; i++)
        {
            permissionDTO = new PermissionDTO();
            permissionDTO.setAction(CarbonConstants.UI_PERMISSION_ACTION);
            permissionDTO.setResourceId(permissions[i]);
            permissionDTOs[i] = permissionDTO;

        }

        soapTestBase.provisionNewRole(ROLE_NAME,null,permissionDTOs);
    }


    @Test(description = "1.2.3.1 adding a user ")
    public void testAddUser()throws Exception {

        endPoint = PROTOCOL +"://"+HOST+":"+SERVER_PORT+"/services/"+service+""+extension;
        soapTestBase =new SOAPTestBase(endPoint,ADMIN_USERNAME,ADMIN_PASSWORD);

        configContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem( null, null);
        trustStore = CARBON_HOME + "/repository/resources/security/wso2carbon.jks";
        System.setProperty("javax.net.ssl.trustStore",  trustStore );
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");

        RemoteUserStoreManagerServiceStub adminStub = new RemoteUserStoreManagerServiceStub(configContext, endPoint);

        ServiceClient client = adminStub._getServiceClient();
        Options option = client.getOptions();

        option.setProperty(HTTPConstants.COOKIE_STRING, null);

        HttpTransportProperties.Authenticator auth = new HttpTransportProperties.Authenticator();
        auth.setUsername(userName);
        auth.setPassword(passWord);
        auth.setPreemptiveAuthentication(true);
        option.setProperty(org.apache.axis2.transport.http.HTTPConstants.AUTHENTICATE, auth);
        option.setManageSession(true);

        authenticate = false;

        try{
            authenticate = adminStub.authenticate(userName, passWord);
        } catch (Exception e){
            e.printStackTrace();
        }

        if(authenticate){
            log.debug("User is authenticated successfully");
        } else {
            log.error("User is authentication failed");
        }

        //Add the user roles
        String[] userRoleList = new String[2];
        userRoleList[1] = new String("admin");
        userRoleList[0] = new String(ROLE_NAME);

        //Configure the claims
        ClaimValue[] claims = new ClaimValue[3];

        ClaimValue claim1 = new ClaimValue();
        claim1.setClaimURI(claimURI);
        claim1.setValue(claimVal);
        claims[0] = claim1;

        ClaimValue claim2 = new ClaimValue();
        claim2.setClaimURI(claimURL2);
        claim2.setValue(claimVal2);
        claims[1] = claim2;

        ClaimValue claim3 = new ClaimValue();
        claim3.setClaimURI(claimURL3);
        claim3.setValue(claimVal3);
        claims[2] = claim3;

        soapTestBase.provisionNewUser(USER_NAME, USER_PASSWORD, userRoleList, claims, "default", false);

    }


    @AfterClass(alwaysRun = true)
    public void cleanUp() throws java.rmi.RemoteException,
            org.wso2.carbon.um.ws.api.stub.RemoteUserStoreManagerServiceUserStoreExceptionException{

        adminStub.deleteUser(USER_NAME);
        adminStub.deleteRole(ROLE_NAME);
    }

}
