
import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

class UnExpectedResponse extends Exception {

    public UnExpectedResponse(String message) {
        super(message);
    }
}

/**
 *
 * @author petar
 */
public class NetworkStresser implements Runnable {

    private CyclicBarrier barrier = null;

    public Long currentPoolMaxTime = (long) 0;
    public Boolean isFailed = false;

    public static Long lastPoolMaxTime = (long) 0;

    public NetworkStresser(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    /**
     * Calculates the max time of a request in current thread pool and set flag
     * isFailed if a request fails.
     *
     * @throws IOException if the server connection is lost unexpectedly
     */
    private void sendRequest() {

        long startTime = System.currentTimeMillis();
        try {
            RequestResponseManager requestResponseManager = new RequestResponseManager();
            requestResponseManager.execute();
            long elapsedTime = System.currentTimeMillis() - startTime;
//            System.out.println(response);

            synchronized (currentPoolMaxTime) {
                if (elapsedTime > currentPoolMaxTime) {
                    currentPoolMaxTime = elapsedTime;
                }
            }
            System.out.println(Thread.currentThread().getName() + ", time: " + elapsedTime + " ms");

        } catch (UnExpectedResponse | IOException e) {
            synchronized (isFailed) {
                if (!isFailed) {
                    isFailed = true;
                    System.out.println(e.toString());
                }
            }
        }
    }

    @Override
    public void run() {
        try {
//            System.out.println(Thread.currentThread().getName()
//                    + " waiting at barrier");
            this.barrier.await();

            sendRequest();

        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
