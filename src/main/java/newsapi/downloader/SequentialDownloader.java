package newsapi.downloader;

import java.util.List;

public class SequentialDownloader extends Downloader {

    @Override
    public int process(List<String> urls){
        int count = 0;
        long begin = System.currentTimeMillis();
        String fileName = null;
        for (String url : urls) {

            try {
                fileName = saveUrl2File(url);
            }catch (Exception ignored){}

            if(fileName != null)
                count++;
        }
        long end = System.currentTimeMillis();
        System.out.println("Sequential execution time: " + (end - begin) + " milliseconds" + System.lineSeparator());
        return count;
    }
}
