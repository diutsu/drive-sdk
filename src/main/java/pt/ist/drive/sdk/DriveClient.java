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

import java.io.IOException;
import java.io.InputStream;import java.io.OutputStream;<<<<<<<Updated upstream
import java.util.Arrays;
import java.util.Objects;import java.util.UUID;>>>>>>>Stashed changes

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class DriveClient {

    protected static final Client CLIENT = ClientBuilder.newClient();

    static {
        CLIENT.register(MultiPartFeature.class);
    }

    protected abstract String accessToken();

    protected abstract String driveUrl();

    /**
     * Upload content to a file in your drive installation.
     * 
     * @param directory remote directory to which the content will be uploaded
     * @param filename name to give the file after upload
     * @param inputStream input stream with the contents to be uploaded
     * @param contentType the type of the content being uploaded
     */
    @Deprecated
    public void upload(final String directory, final String filename, final InputStream inputStream, final String contentType) {
        uploadWithInfo(directory, filename, inputStream, contentType);
    }

    public JsonObject uploadWithInfo(final String directory, final String filename, final InputStream inputStream,
            final String contentType) {
        final String[] mediaType = contentType.split("/");
        final StreamDataBodyPart streamDataBodyPart =
                new StreamDataBodyPart("file", inputStream, filename, new MediaType(mediaType[0], mediaType[1]));
        try (final FormDataMultiPart formDataMultiPart = new FormDataMultiPart()) {
            final MultiPart entity = formDataMultiPart.bodyPart(streamDataBodyPart);
            final String path = "/api/drive/directory/" + directory;
            return target(path).post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE), JsonObject.class);
        } catch (final IOException e) {
            throw new Error(e);
        }
    }

    private void download(final String path, final HttpServletResponse response) throws IOException {
        final Response r = target(path).get();
        setHeader(response, r, "Content-Disposition");
        setHeader(response, r, "Date");
        setHeader(response, r, "Content-Type");
        final InputStream inputStream = (InputStream) r.getEntity();
        final ServletOutputStream outputStream = response.getOutputStream();
        copy(inputStream, outputStream);
        outputStream.close();
        inputStream.close();
    }

    public static int copy(final InputStream input, final OutputStream output) throws IOException {
        final byte[] buffer = new byte[4096];
        long count = 0;
        for (int n = 0; -1 != (n = input.read(buffer)); count += n) {
            output.write(buffer, 0, n);
        }
        return count > Integer.MAX_VALUE ? -1 : (int) count;
    }

    private static void setHeader(final HttpServletResponse response, final Response r, final String header) {
        response.setHeader(header, r.getHeaderString(header));
    }

    /**
     * Download a file from your Drive installation and send place it in an HTTP response
     * 
     * @param id ID of the file in you Drive installation
     * @param response HTTP response where to dump the file
     * @throws IOException If unable to process input from Drive installation or
     *             unable to fill in response
     */
    public void downloadFile(final String id, final HttpServletResponse response) throws IOException {
        download("/api/drive/file/" + id + "/download", response);
    }

    /**
     * Download a document from your Drive installation and place it in an HTTP Response
     * 
     * @param documentUuid - unique identifier of the document
     * @param response
     * @throws IOException
     */
    public void downloadDocument(final UUID documentUuid, final HttpServletResponse response) throws IOException {
        Objects.requireNonNull(documentUuid);
        download("/api/drive/document/" + documentUuid.toString() + "/download", response);
    }

    /**
     * Download an entire directory as a ZIP file from your Drive installation and send
     * place it in an HTTP response
     * 
     * @param id ID of the directory in you Drive installation
     * @param response HTTP response where to dump the file
     * @throws IOException If unable to process input from Drive installation or
     *             unable to fill in response
     */
    public void downloadDir(final String id, final HttpServletResponse response) throws IOException {
        download("/api/drive/directory/" + id + "/download", response);
    }

    /**
     * Create a directory in your Drive installation
     * 
     * @param parent ID of the directory in which you want to create a sub-directory
     * @param name Name of the sub-directory to create
     * @return The ID of the created sub-directory
     */
    public String createDirectory(final String parent, final String name) {
        final String putDir = target("/api/drive/directory/" + parent)
                .put(Entity.entity("{name: \"" + name + "\"}", MediaType.APPLICATION_JSON), String.class);
        final JsonObject o3 = new JsonParser().parse(putDir).getAsJsonObject();
        return o3.get("id").getAsString();
    }

    /**
     * Retrieve information regarding the contents of a directory
     * 
     * @param directory ID of the directory to list
     * @return A JSON array with information of the contents of a directory
     */
    public JsonArray listDirectory(final String directory) {
        final String post3 = target("/api/drive/directory/" + directory).get(String.class);
        final JsonObject o3 = new JsonParser().parse(post3).getAsJsonObject();
        return o3.get("items").getAsJsonArray();
    }

    /**
     * Delete a directory from your Drive installation
     * 
     * @param directory ID of the directory to delete
     */
    public void deleteDirectory(final String directory) {
        target("/api/drive/directory/" + directory).delete();
    }

    /**
     * Delete a file from your Drive installation
     * 
     * @param file ID of the file to delete
     */
    public void deleteFile(final String file) {
        target("/api/drive/file/" + file).delete();
    }

    protected Builder target(final String path) {
        return target(path, accessToken());
    }

    protected Builder target(final String path, final String token, final String... params) {
        final WebTarget target = CLIENT.target(driveUrl() + path).queryParam("access_token", token);
        return addParams(target, params).request().header("X-Requested-With", "XMLHttpRequest");
    }

    private WebTarget addParams(final WebTarget target, final String[] params) {
        return params == null || params.length < 2 ? target : addParams(target.queryParam(params[0], params[1]),
                Arrays.copyOfRange(params, 2, params.length));
    }

    /**
     * Test weather a directory contains an item. This method does not recurse into
     * sub-directories. It only tests the direct children of the specified directory.
     * 
     * @param directory ID of the directory to search
     * @param item ID of the file or sub-directory to search for
     * @return true if the directory contains the item, false otherwise
     */
    public boolean dirContainsItem(final String directory, final String item) {
        for (final JsonElement e : listDirectory(directory)) {
            final JsonObject o = e.getAsJsonObject();
            final JsonElement id = o.get("id");
            if (id != null && id.getAsString().equals(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieve log information regarding the contents of a directory or file
     * 
     * @param directory or file ID of the directory to list
     * @return A JSON array with log information of the directory or file
     */
    public JsonObject logs(final String directory, final int pageSize) {
        final String post3 =
                target("/api/drive/directory/" + directory + "/logs", accessToken(), "pageSize", Integer.toString(pageSize))
                        .get(String.class);
        return new JsonParser().parse(post3).getAsJsonObject();
    }

}
