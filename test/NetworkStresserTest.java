import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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
public class NetworkStresserTest {

    public String host = "127.0.0.1";
    public int port = 8080;
    public static MultiThreadedServer server;

    public NetworkStresserTest() {
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
     * Test of run method, of class NetworkStresser.
     */
    @Test
    public void testRunTwoSimultaneousRequests() throws InterruptedException {
        System.out.println("run");
        int numberOfRequests = 2;
        CyclicBarrier barrier = new CyclicBarrier(numberOfRequests);
        NetworkStresser networkStresser = new NetworkStresser(barrier);
        ExecutorService executor = Executors.newFixedThreadPool(numberOfRequests);

        for (int j = 0; j < numberOfRequests; j++) {
            Thread t = new Thread(networkStresser);
            executor.execute(t);
        }
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        System.out.println("HERE");

        assertFalse(networkStresser.isFailed);

    }

}
