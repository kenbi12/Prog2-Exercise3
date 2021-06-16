package newsapi.downloader;

import java.util.concurrent.Callable;

public class DownloaderCallable implements Callable<String> {

    private final String url;
    private final Downloader downloader;

    DownloaderCallable(String s, Downloader downloader){
        this.url = s;
        this.downloader = downloader;
    }

    @Override
    public String call() throws Exception {
       return downloader.saveUrl2File(url);
    }
}
