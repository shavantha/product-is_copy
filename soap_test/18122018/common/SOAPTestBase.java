/*
 *Copyright (c) 2005-2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *WSO2 Inc. licenses this file to you under the Apache License,
 *Version 2.0 (the "License"); you may not use this file except
 *in compliance with the License.
 *You may obtain a copy of the License at
 *
 *http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing,
 *software distributed under the License is distributed on an
 *"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *KIND, either express or implied.  See the License for the
 *specific language governing permissions and limitations
 *under the License.
 */
package org.wso2.identity.scenarios.test.common;

import org.apache.axis2.AxisFault;

import org.wso2.carbon.um.ws.api.stub.ClaimValue;
import org.wso2.carbon.um.ws.api.stub.PermissionDTO;
import org.wso2.carbon.um.ws.api.stub.RemoteUserStoreManagerServiceStub;
import org.wso2.carbon.um.ws.api.stub.RemoteUserStoreManagerServiceUserStoreExceptionException;
import org.wso2.identity.scenarios.test.common.AuthenticateStub;


public class SOAPTestBase {

    public SOAPTestBase(){

    }

    private final String serviceName = "RemoteUserStoreManagerService";
    private RemoteUserStoreManagerServiceStub remoteUserStoreManagerServiceStub;

    public SOAPTestBase(String backEndUrl, String userName, String password)
            throws AxisFault {
        String endPoint = backEndUrl + serviceName;
        remoteUserStoreManagerServiceStub = new RemoteUserStoreManagerServiceStub(endPoint);
        AuthenticateStub.authenticateStub(userName, password, remoteUserStoreManagerServiceStub);
    }

    /**
     * Adds a role to the system.
     *
     * @param roleName    The role name
     * @param userList    the list of the users.
     * @param permissions The permissions of the role.
     * @throws RemoteUserStoreManagerServiceUserStoreExceptionException
     * @throws java.rmi.RemoteException
     *
     */

    public void provisionNewRole(String roleName, String[] userList, PermissionDTO[] permissions)
            throws RemoteUserStoreManagerServiceUserStoreExceptionException,java.rmi.RemoteException {
        remoteUserStoreManagerServiceStub.addRole(roleName, userList, permissions);
    }

    /**
     * Add a user to the user store.
     *
     * @param userName              User name of the user
     * @param credential            The credential/password of the user
     * @param roleList              The roles that user belongs
     * @param claimValues           Properties of the user
     * @param profileName           The name of the profile where claims should be added
     * @param requirePasswordChange Require the password change
     * @throws RemoteUserStoreManagerServiceUserStoreExceptionException
     * @throws java.rmi.RemoteException     */
    public void provisionNewUser(String userName, String credential, String[] roleList,
                        ClaimValue[] claimValues,
                        String profileName, boolean requirePasswordChange)
            throws RemoteUserStoreManagerServiceUserStoreExceptionException,java.rmi.RemoteException {

        remoteUserStoreManagerServiceStub.addUser(userName, credential, roleList, claimValues,
                profileName, requirePasswordChange);
    }


    /**
     * Checks whether the role name is in the user store
     *
     * @param roleName role name
     * @return true if exists, false otherwise
     * @throws RemoteUserStoreManagerServiceUserStoreExceptionException
     * @throws java.rmi.RemoteException
     */
    public boolean isExistingRole(String roleName)
            throws RemoteUserStoreManagerServiceUserStoreExceptionException,java.rmi.RemoteException {
        return remoteUserStoreManagerServiceStub.isExistingRole(roleName);
    }

    /**
     * Checks whether the user is in the user store
     *
     * @param userName
     * @return
     * @throws RemoteUserStoreManagerServiceUserStoreExceptionException
     * @throws java.rmi.RemoteException
     */
    public boolean isExistingUser(String userName)
            throws RemoteUserStoreManagerServiceUserStoreExceptionException,java.rmi.RemoteException {
        return remoteUserStoreManagerServiceStub.isExistingUser(userName);
    }

    /**
     * Get the role of user
     * @param userName
     * @return
     * @throws RemoteUserStoreManagerServiceUserStoreExceptionException
     * @throws java.rmi.RemoteException
     */
    public String[] getRoleListOfUser(String userName)
            throws RemoteUserStoreManagerServiceUserStoreExceptionException,java.rmi.RemoteException {
        return remoteUserStoreManagerServiceStub.getRoleListOfUser(userName);
    }

    /**
     * Get the role name
     * @return
     * @throws RemoteUserStoreManagerServiceUserStoreExceptionException
     * @throws java.rmi.RemoteException
     */
    public String[] getRoleNames()
            throws RemoteUserStoreManagerServiceUserStoreExceptionException,java.rmi.RemoteException {
        return remoteUserStoreManagerServiceStub.getRoleNames();
    }


    /**
     * Delete the user with the given username
     * @param username
     * @throws RemoteUserStoreManagerServiceUserStoreExceptionException
     * @throws java.rmi.RemoteException
     */
    public void deleteUser(String username)
            throws RemoteUserStoreManagerServiceUserStoreExceptionException,java.rmi.RemoteException {
        remoteUserStoreManagerServiceStub.deleteUser(username);
    }

    /**
     * Delete the role with the given role name
     * @param roleName
     * @throws RemoteUserStoreManagerServiceUserStoreExceptionException
     * @throws java.rmi.RemoteException
     */
    public void deleteRole(String roleName)
            throws RemoteUserStoreManagerServiceUserStoreExceptionException,java.rmi.RemoteException {
        remoteUserStoreManagerServiceStub.deleteRole(roleName);
    }

}
