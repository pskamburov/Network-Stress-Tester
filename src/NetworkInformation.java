
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author petar
 */
public final class NetworkInformation {

    private static String host;
    private static String request;
    private static String response;
    private static int port;

    /**
     * Get a request from a file.
     *
     * @param filename
     * @return String
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static String getRequest(String filename) throws FileNotFoundException, IOException {
        StringBuilder requestFromFile = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                requestFromFile.append(line);
                requestFromFile.append(System.lineSeparator());
            }
        }
        return requestFromFile.toString();
    }

    /**
     * Get the first line of a file
     *
     * @param filename
     * @return String
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static String getExpectedResponse(String filename)
            throws FileNotFoundException, IOException {
        String expectedFirstLine = "";
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            expectedFirstLine = br.readLine();
        }
        return expectedFirstLine;
    }

    public static void setInformation(String host,
            String requestFilename,
            String responseFilename,
            int port)
            throws FileNotFoundException, IOException {
        NetworkInformation.host = host;
        NetworkInformation.request = getRequest(requestFilename);
        NetworkInformation.response = getExpectedResponse(responseFilename);
        NetworkInformation.port = port;
    }

    public static String getHost() {
        return host;
    }

    public static String getRequestFilename() {
        return request;
    }

    public static String getResponseFilename() {
        return response;
    }

    public static int getPort() {
        return port;
    }

}
