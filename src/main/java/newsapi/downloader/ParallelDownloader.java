package newsapi.downloader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


public class ParallelDownloader extends Downloader {

    ExecutorService executor = Executors.newCachedThreadPool();

    @Override
    public int process(List<String> urls) {
        int count = 0;
        String fileName = null;
        long begin = System.currentTimeMillis();
        List<Future<String>> futures = new ArrayList<>();
        List<Callable<String>> callableList = new ArrayList<>();
        for (String url : urls) {

            Callable<String> callable = new DownloaderCallable(url, this);
            callableList.add(callable);

            //futures.add(executor.submit(callable));
        }
        try {
            futures = executor.invokeAll(callableList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Future<String> future : futures){
            try {
                fileName = future.get();

                if (fileName != null){
                    count++;
                }
            } catch (Exception ignore) {}


        }
        long end = System.currentTimeMillis();

        System.out.println("Parallel execution time was: " + (end-begin) + " milliseconds" + System.lineSeparator());
        return count;
    }
}
