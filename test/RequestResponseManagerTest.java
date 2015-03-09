
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import server.MultiThreadedServer;

/**
 *
 * @author petar
 */
public class RequestResponseManagerTest {

    public String host = "127.0.0.1";
    public int port = 8080;
    public static MultiThreadedServer server;

    public RequestResponseManagerTest() {

    }

    @BeforeClass
    public static void setUpClass() {
        server = new MultiThreadedServer(8080);
        new Thread(server).start();
    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println("Stopping Server");
        server.stop();
    }

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException {
        try {
            File tempFile1 = folder.newFile("request.txt");
            File tempFile2 = folder.newFile("response.txt");

            PrintWriter writer = new PrintWriter("request.txt");
            writer.println("GET / HTTP/1.1");
            writer.println("Host: 127.0.0.1");
            writer.println();
            writer.close();
            PrintWriter writerResponse = new PrintWriter("response.txt");
            writerResponse.println("HTTP/1.1 200 OK");
            writerResponse.println("The second line");
            writerResponse.close();
            NetworkInformation.setInformation(host, "request.txt", "response.txt", port);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @After
    public void tearDown() {

    }

    /**
     * Test of execute method, of class RequestResponseManager.
     */
    @Test
    public void testExecutingThreeRequests() throws Exception {
        System.out.println("execute");
        RequestResponseManager instance = new RequestResponseManager();
        for (int i = 0; i < 3; i++) {
            instance.execute();
        }
    }

    @Test
    public void testCreateSocket() throws UnExpectedResponse, IOException {
        System.out.println("create socket");
        RequestResponseManager instance = new RequestResponseManager();
        Socket s = instance.createSocket();
        assertTrue(s.isConnected());
        s.close();
    }

    @Test(expected = UnknownHostException.class)
    public void testShouldNotConnectToInvalidHost() throws Exception {
        System.out.println("execute on invalid host");
        NetworkInformation.setInformation("invalidhost", "request.txt", "response.txt", port);
        RequestResponseManager instance = new RequestResponseManager();
        instance.execute();
    }

    @Test(expected = ConnectException.class)
    public void testShouldNotConnectToInvalidPort() throws Exception {
        System.out.println("execute on invalid host");
        NetworkInformation.setInformation(host, "request.txt", "response.txt", 123);
        RequestResponseManager instance = new RequestResponseManager();
        instance.execute();
    }

    @Test(expected = UnExpectedResponse.class)
    public void testUnexpectedResponse() throws Exception {
        System.out.println("execute on invalid host");
        File tempFile = folder.newFile("expectedresponse.txt");
        PrintWriter writer = new PrintWriter("expectedresponse.txt");
        writer.println("HTTP 404 Not Found");
        writer.close();
        NetworkInformation.setInformation(host, "request.txt", "expectedresponse.txt", port);

        RequestResponseManager instance = new RequestResponseManager();
        instance.execute();
    }
}
