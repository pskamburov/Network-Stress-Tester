
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

/**
 *
 * @author petar
 */
public class RequestResponseManager {

    private String host;
    private int port;
    private String request;
    private String expectedResponse;

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public void setExpectedResponse(String expectedResponse) {
        this.expectedResponse = expectedResponse;
    }

    public RequestResponseManager() {
        setHost(NetworkInformation.getHost());
        setPort(NetworkInformation.getPort());
        setRequest(NetworkInformation.getRequestFilename());
        setExpectedResponse(NetworkInformation.getResponseFilename());
    }

    /**
     * Create socket
     *
     * @return Socket
     * @throws IOException
     */
    public Socket createSocket() throws IOException {
        Socket socket = new Socket(host, port);
        return socket;
    }

    /**
     * Connect to the server, send request and compare the response
     *
     * @throws ConnectException
     * @throws FileNotFoundException request file or response file can't be
     * found
     * @throws UnExpectedResponse if the response is not expected
     * @throws IOException
     */
    public void execute() throws ConnectException, FileNotFoundException, UnExpectedResponse, IOException {
        try (
                Socket socket = createSocket();
                PrintWriter requestWriter = new PrintWriter(socket.getOutputStream());
                InputStream inStream = socket.getInputStream();
                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(inStream));) {

            requestWriter.print(request);
            requestWriter.flush();

            String response = rd.readLine();
            System.out.println(response);
            if (!expectedResponse.equals(response)) {
                throw new UnExpectedResponse("Unexpected Response");
            }
        }
    }

}
