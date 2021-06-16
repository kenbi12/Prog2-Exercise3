package newsanalyzer.ctrl;

import newsapi.NewsApi;
import newsapi.NewsApiBuilder;
import newsapi.NewsApiException;
import newsapi.beans.Article;
import newsapi.beans.NewsResponse;
import newsapi.downloader.Downloader;
import newsapi.enums.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Controller {

	public static final String APIKEY = "57921b7703a54d128d168651c2abbd33";
	private NewsResponse newsResponse = null;


	public void process(String query,
						Endpoint endpoint,
						Category category,
						Language language,
						Country country,
						SortBy sortBy,
						boolean shortest,
						boolean articleCount,
						boolean titlesSorted,
						boolean mostArticleProvider) throws NewsApiException {

		System.out.println("Start process");

		NewsApi newsApi = new NewsApiBuilder()
				.setApiKey(APIKEY)
				.setQ(query)
				.setEndPoint(endpoint)
				.setFrom("2021-04-10")
				.setSourceCountry(country)
				.setSourceCategory(category)
				.setLanguage(language)
				.createNewsApi();

		try {
			newsResponse = (NewsResponse) getData(newsApi);
		}catch (ClassCastException e){
			throw new NewsApiException("An Error has occurred");
		}

		List<Article> articles;
		if(newsResponse != null){

			articles = newsResponse.getArticles();
			articles.stream().forEach(article -> System.out.println(article.toString()));
			System.out.print("\nDatenanalyse:\n");

			if(shortest){
				System.out.println(System.lineSeparator() + "Kürzester Autorenname: " + getShortestNameInData(articles));
			}
			if(articleCount){
				System.out.println(System.lineSeparator() + "Anzahl Artikel: " + getNumberOfArticlesInData(articles));
			}
			if(mostArticleProvider){
				System.out.println(System.lineSeparator() + "Meiste Artikel von " + getMostFrequentProviderInData(articles));
			}
			if(titlesSorted){
				List<Article> sortedTitles = getTitlesSortedByLength(articles);

				System.out.println(System.lineSeparator() + "");
				Collections.reverse(sortedTitles);
				sortedTitles.forEach(e -> System.out.println(e.getTitle()));
			}


		}else{
			throw new NewsApiException("Error: News response is null");
		}

		System.out.println("End process");
	}

	public String downloadLastSearch(Downloader downloader) throws NewsApiException{
		if (newsResponse != null){
			downloader.process(getURLList(newsResponse.getArticles()));
			return "success";
		}else{
			return "error";
		}
	}

	public List<String> getURLList(List<Article> data){

		return Stream.concat(
				data
						.stream()
						.map(Article::getUrl)
						.filter(Objects::nonNull)
						.collect(Collectors.toList())
						.stream(),
				data
						.stream()
						.map(Article::getUrlToImage)
						.filter(Objects::nonNull)
						.collect(Collectors.toList())
						.stream()
		).collect(Collectors.toList());
	}

	public String getShortestNameInData(List<Article> data) throws NewsApiException {
		String authorShortestName;
		try {
			authorShortestName = data.stream().filter(article -> article.getAuthor() != null).max(Comparator.comparing(Article::getAuthor)).get().getAuthor();
		}catch(NoSuchElementException exception){
			throw new NewsApiException(exception.getMessage());
		}
		return authorShortestName;
	}

	public String getMostFrequentProviderInData(List<Article> data) {
		return data
				.stream()
				.collect(Collectors.groupingBy(article -> article.getSource().getName(), Collectors.counting()))
				.entrySet()
				.stream()
				.max(Comparator.comparing(Map.Entry::getValue)).orElseThrow(NoSuchElementException::new ).getKey();
	}

	public long getNumberOfArticlesInData(List<Article> data) {
		return data.stream().count();
	}

	public  List<Article> getTitlesSortedByLength(List<Article> data){
		return data
				.stream()
				.sorted(Comparator.comparing(Article::getTitle))
				.collect(Collectors.toList());
	}

	public Object getData(NewsApi newsApi) throws NewsApiException {

		return newsApi.getNews();
	}
}
