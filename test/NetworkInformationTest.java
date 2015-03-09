
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 *
 * @author petar
 */
public class NetworkInformationTest {

    public String host = "127.0.0.1";
    public int port = 80;

    public NetworkInformationTest() {

    }

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
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
            writer.close();
            writer = new PrintWriter("response.txt");
            writer.println("HTTP/1.1 200 OK");
            writer.println("The second line");
            writer.close();
            NetworkInformation.setInformation(host, "request.txt", "response.txt", port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of setInformation method, of class NetworkInformation.
     */
    @Test(expected = NullPointerException.class)
    public void testSetInformationNullFile() throws IOException {
        System.out.println("setInformation");
        String requestFilename = null;
        String responseFilename = null;
        NetworkInformation.setInformation(host, requestFilename, responseFilename, port);
    }

    @Test(expected = FileNotFoundException.class)
    public void testSetInformationInvalidFile() throws IOException {
        System.out.println("setInformation");
        String requestFilename = "/\\.";
        String responseFilename = "response.txt";
        NetworkInformation.setInformation(host, requestFilename, responseFilename, port);
    }

    /**
     * Test of getHost method, of class NetworkInformation.
     */
    @Test
    public void testGetHost() {
        System.out.println("getHost");
        String expResult = "127.0.0.1";
        String result = NetworkInformation.getHost();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRequestFilename method, of class NetworkInformation.
     */
    @Test
    public void testGetRequestFilename() {
        System.out.println("getRequestFilename");
        StringBuilder expResult = new StringBuilder("GET / HTTP/1.1");
        expResult.append(System.lineSeparator());
        expResult.append("Host: 127.0.0.1");
        expResult.append(System.lineSeparator());
        String result = NetworkInformation.getRequestFilename();
        assertEquals(expResult.toString(), result);
    }

    /**
     * Test of getResponseFilename method, of class NetworkInformation.
     */
    @Test
    public void testGetResponseFilename() {
        System.out.println("getResponseFilename");
        String expResult = "HTTP/1.1 200 OK";
        String result = NetworkInformation.getResponseFilename();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPort method, of class NetworkInformation.
     */
    @Test
    public void testGetPort() {
        System.out.println("getPort");
        int expResult = 80;
        int result = NetworkInformation.getPort();
        assertEquals(expResult, result);
    }

}
