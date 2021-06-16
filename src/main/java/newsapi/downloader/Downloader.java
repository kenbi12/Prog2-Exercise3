package newsapi.downloader;

import newsapi.NewsApiException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public abstract class Downloader {

    public static final String HTML_EXTENSION = ".html";
    public static final String DIRECTORY_DOWNLOAD = "./download/";

    public abstract int process(List<String> urls);

    public String saveUrl2File(String urlString) throws NewsApiException {
        InputStream is = null;
        OutputStream os = null;
        String fileName = "";
        try {
            URL url4download = new URL(urlString);
            is = url4download.openStream();

            fileName = urlString.substring(urlString.lastIndexOf('/') + 1);

            if (fileName.isEmpty()) {
                fileName = url4download.getHost() + HTML_EXTENSION;
            }
            fileName = fileName.replaceFirst("\\?", "");
            fileName = fileName.replaceAll(",","");
            fileName = fileName.replaceAll(";","");
            os = new FileOutputStream(DIRECTORY_DOWNLOAD + fileName);
            byte[] b = new byte[2048];
            int length;
            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }
        } catch (IOException e) {
            throw new NewsApiException("File not found!" + fileName);
        } finally {
            try {
                Objects.requireNonNull(is).close();
                Objects.requireNonNull(os).close();
            } catch (Exception e) {
                System.out.println("Could not download some files");
            }
        }
        return fileName;
    }
}
