/**
 * Copyright © 2015 Instituto Superior Técnico
 *
 * This file is part of the Drive Client SDK.
 *
 * Drive Client SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Drive Client SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Drive Client SDK.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.bennu;

import org.fenixedu.commons.configuration.ConfigurationInvocationHandler;
import org.fenixedu.commons.configuration.ConfigurationManager;
import org.fenixedu.commons.configuration.ConfigurationProperty;

public class DriveSdkConfiguration {

    @ConfigurationManager(description = "Drive Client SDK Configuration")
    public interface ConfigurationProperties {
        @ConfigurationProperty(key = "drive.url", defaultValue = "http://localhost:8080/drive")
        public String driveUrl();

        @ConfigurationProperty(key = "drive.client.refresh.token")
        public String refreshToken();

        @ConfigurationProperty(key = "drive.client.app.id")
        public String clientAppId();

        @ConfigurationProperty(key = "drive.client.app.user")
        public String clientAppUser();
    }

    public static ConfigurationProperties getConfiguration() {
        return ConfigurationInvocationHandler.getConfiguration(ConfigurationProperties.class);
    }

}
