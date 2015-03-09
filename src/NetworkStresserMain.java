
import java.io.IOException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author petar
 */
public class NetworkStresserMain {

    public static void main(String[] args) {
        int numberOfRequests = 1;
        int loops = 0;

        try {
//            NetworkInformation.setInformation(
//                    "java.voidland.org",
//                    "example_request.txt",
//                    "example_expected_response.txt",
//                    80
//            );
            
            NetworkInformation.setInformation(
                    "127.0.0.1",
                    "lexample_request.txt",
                    "lexample_expected_response.txt",
                    8080
            );
            
//            NetworkInformation.setInformation(
//                    "Tmall.com",
//                    "example_request2.txt",
//                    "example_expected_response2.txt",
//                    80
//            );
            
//            NetworkInformation.setInformation(
//                    "127.0.0.1",
//                    "example_request3.txt",
//                    "example_expected_response3.txt",
//                    8000
//            );
            
            while (true) {
                CyclicBarrier barrier = new CyclicBarrier(numberOfRequests);
                NetworkStresser networkStresser = new NetworkStresser(barrier);
                ExecutorService executor = Executors.newFixedThreadPool(numberOfRequests);

                for (int j = 0; j < numberOfRequests; j++) {
                    executor.execute(networkStresser);
                }
                executor.shutdown();
                executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                if (networkStresser.isFailed) {
                    break;
                } else {
                    NetworkStresser.lastPoolMaxTime = networkStresser.currentPoolMaxTime;
                }
                numberOfRequests++;
                loops++;
            }
            numberOfRequests = loops > 0 ? numberOfRequests - 1 : 0;
            System.out.printf("%d simultaneous successful requests!\n", numberOfRequests);
            System.out.printf("Longest time for response: %d ms\n", NetworkStresser.lastPoolMaxTime);

        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

    }

}
