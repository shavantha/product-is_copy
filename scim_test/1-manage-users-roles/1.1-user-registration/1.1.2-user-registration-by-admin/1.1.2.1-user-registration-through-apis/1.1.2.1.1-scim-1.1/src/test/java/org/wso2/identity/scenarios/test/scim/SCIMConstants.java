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
public class SCIMConstants {

    static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";
    static final String SCIM_ENDPOINT = "/wso2/scim";
    static final String SCHEMAS_ATTRIBUTE = "schemas";
    static final String GIVEN_NAME_ATTRIBUTE = "givenName";
    static final String NAME_ATTRIBUTE = "name";
    static final String USER_NAME_ATTRIBUTE = "userName";
    static final String PASSWORD_ATTRIBUTE = "password";
    static final String ID_ATTRIBUTE = "id";
    static final String GIVEN_NAME_CLAIM_VALUE = "user1";
    static final String USERNAME = "scim1user";
    static final String PASSWORD = "scim1pwd";
    static final String URL_PATH_SEPARATOR = "/";
    public static final String FAMILY_NAME_ATTRIBUTE = "familyName";
    public static final String FAMILY_NAME_CLAIM_VALUE = "scim2";
    public static final String TYPE_PARAM = "type";
    public static final String EMAIL_TYPE_WORK_ATTRIBUTE = "work";
    public static final String EMAIL_TYPE_HOME_ATTRIBUTE = "home";
    public static final String VALUE_PARAM = "value";
    public static final String PRIMARY_PARAM = "primary";
    public static final String EMAILS_ATTRIBUTE = "emails";
    public static final String ROLE_NAME = "Engineering";
    public static final String ADMIN_ROLE = "Admin";
    public static final String DISPLAY = "display";
    public static final String ROLE_DISPLAY_NAME_ATTRIBUTE = "displayName";
    public static final String ERROR_SCHEMA = "urn:ietf:params:scim:api:messages:2.0:Error";
    public static final String GROUP_ENDPOINT = "Groups";
    public static final String MEMBERS = "members";

    /**
     * Operators
     */
    public static class Operators {

        public static final String EQUAL = "+Eq+";
        public static final String STARTWITH = "+Sw+";
        public static final String ENDWITH = "+Ew+";
        public static final String CONTAINS = "+Co+";
    }
}


