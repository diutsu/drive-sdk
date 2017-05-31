package pt.ist.drive.sdk;

import pt.ist.drive.sdk.utils.RandomGeneratedStream;

import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import com.google.gson.JsonObject;

import static org.fenixedu.bennu.DriveSdkConfiguration.getConfiguration;
import static org.junit.Assert.assertEquals;

/**
 * Created by diutsu on 26/05/17.
 */
public class IntegrationTestConnection {
    static String username = "ist166993";
    
    
    private MockHttpServletResponse response;
    
    @Before
    public void setUp() {
        response = new MockHttpServletResponse();
    }
    
    @Test
    public void testConnection() throws NoSuchAlgorithmException, IOException {
        DriveClient client =  ClientFactory.driveCLient(getConfiguration().driveUrl(), getConfiguration().clientAppId(),
            username,
            getConfiguration().refreshToken());
        client.listDirectory("");
        MessageDigest digest = MessageDigest.getInstance("MD5");
        DigestInputStream stream = new DigestInputStream(new RandomGeneratedStream((long) (1024 * 1024)),digest);

         String rootDirId = client.createDirectory("",UUID.randomUUID()+"-test");
        JsonObject uploadJson =
            client.uploadWithInfo(rootDirId, "test-file" + UUID.randomUUID().toString().substring(0, 4), stream, "image/jpg");
    
        String fileId = uploadJson.get("id").getAsString();
        client.downloadFile(fileId, response);
        assertEquals("image/jpeg", response.getContentType());
    
        client.downloadDir(rootDirId, response);
        assertEquals("application/zip", response.getContentType());
    
    
    }
}
