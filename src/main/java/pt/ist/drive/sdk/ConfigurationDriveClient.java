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
package pt.ist.drive.sdk;

import org.fenixedu.bennu.DriveSdkConfiguration;
import org.fenixedu.bennu.DriveSdkConfiguration.ConfigurationProperties;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ConfigurationDriveClient extends DriveClient {

    ConfigurationDriveClient() {
    }

    private ConfigurationProperties getConfig() {
        return DriveSdkConfiguration.getConfiguration();
    }

    @Override
    protected String accessToken() {
        final String refresh_token = getConfig().refreshToken();
        final String path = "/api/docs/oauth/" + getConfig().clientAppId() + "/" + getConfig().clientAppUser();
        final String post2 = target(path, refresh_token).get(String.class);
        final JsonObject o2 = new JsonParser().parse(post2).getAsJsonObject();
        return o2.get("access_token").getAsString();
    }

    @Override
    protected String driveUrl() {
        return getConfig().driveUrl();
    }

}
