package newsapi;// Mit Anas Kambal

import newsapi.beans.Article;
import newsapi.beans.NewsResponse;
import newsapi.enums.Country;
import newsapi.enums.Endpoint;

import java.util.List;

public class NewsAPIExample {

    public static final String APIKEY = "57921b7703a54d128d168651c2abbd33";

    public static void main(String[] args) throws NewsApiException {

        NewsApi newsApi = new NewsApiBuilder()
                .setApiKey(APIKEY)
                .setQ("")
                .setEndPoint(Endpoint.TOP_HEADLINES)
                .setSourceCountry(Country.at)
                .setSourceCategory(null)
                .createNewsApi();

            NewsResponse newsResponse = newsApi.getNews();
            if(newsResponse != null){
                List<Article> articles = newsResponse.getArticles();
                articles.stream().forEach(article -> System.out.println(article.toString()));
            }
    }
}
