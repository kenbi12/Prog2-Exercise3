package newsanalyzer.ui;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import newsanalyzer.ctrl.Controller;

import newsapi.NewsApiException;
import newsapi.downloader.*;
import newsapi.enums.*;


public class UserInterface
{

	private String query ="";

	private Controller ctrl = new Controller();

	/*
	Booleans could be used for switch analysis on and off
	 */
	private boolean shortest;
	private boolean articleCount;
	private boolean titlesSorted;
	private boolean mostArticleProvider;

	private Downloader downloader;

	public void setParallelDownloader() {
		downloader = new ParallelDownloader();
		startDownload();
	}

	public void setSequentialDownloader(){
		downloader = new SequentialDownloader();
		startDownload();
	}

	public void getDataFromCtrl1(){
		System.out.println("Headlines Österreich - Allgemein:");
		try {
			ctrl.process(
					query,
					Endpoint.TOP_HEADLINES,
					null,
					Language.de,
					Country.at,
					SortBy.POPULARITY,
					true,
					true,
					true,
					true
			);
		}catch(NewsApiException e){
			System.out.println("An Error has ocurred");
			System.out.println(e.getMessage());
		}
	}

	public void getDataFromCtrl2(){
		try {
			ctrl.process(
					query,
					Endpoint.TOP_HEADLINES,
					null,
					Language.de,
					Country.de,
					SortBy.POPULARITY,
					true,
					true,
					true,
					true
			);
		}catch(NewsApiException e){
			System.out.println("An Error has ocurred");
			System.out.println(e.getMessage());
		}
	}

	public void getDataFromCtrl3(){
		try {
			ctrl.process(
					query,
					Endpoint.TOP_HEADLINES,
					Category.health,
					Language.de,
					Country.at,
					SortBy.POPULARITY,
					true,
					true,
					true,
					true
			);
		}catch(NewsApiException e){
			System.out.println("An Error has occurred");
			System.out.println(e.getMessage());
		}
	}



	public void startDownload() {
		String result = "error";

		try{
			result = ctrl.downloadLastSearch(downloader);
		}catch (NewsApiException e) {
			System.out.println(e.getMessage());
		}

		if ( result.equals("success")){
			System.out.println("Download successful!");
		}else{
			System.out.println("Download error");
		}
	}
	
	public void getDataForCustomInput() {
		System.out.print("Please enter search term: ");
		query = readLine();

	}




	public void start() {
		Menu<Runnable> menu = new Menu<>("User Interface");
		menu.setTitel("Wählen Sie aus:");
		menu.insert("a", "Headlines Österreich - Allgemein", this::getDataFromCtrl1);
		menu.insert("b", "Headlines Österreich - Health", this::getDataFromCtrl2);
		menu.insert("c", "News Österreich - Allgemein", this::getDataFromCtrl3);
		menu.insert("d", "Download last search - parallel",this::setParallelDownloader);
		menu.insert("e", "Download last search - sequentiell",this::setSequentialDownloader);
		menu.insert("f", "Set query:",this::getDataForCustomInput);
		menu.insert("q", "Quit", null);
		Runnable choice;
		while ((choice = menu.exec()) != null) {
			 choice.run();
		}
		System.out.println("Program finished");
	}




	protected String readLine() {
		String value = "\0";
		BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
		try {
			value = inReader.readLine();
        } catch (IOException ignored) {
		}
		return value.trim();
	}

	protected Double readDouble(int lowerlimit, int upperlimit) 	{
		Double number = null;
        while (number == null) {
			String str = this.readLine();
			try {
				number = Double.parseDouble(str);
            } catch (NumberFormatException e) {
                number = null;
				System.out.println("Please enter a valid number:");
				continue;
			}
            if (number < lowerlimit) {
				System.out.println("Please enter a higher number:");
                number = null;
            } else if (number > upperlimit) {
				System.out.println("Please enter a lower number:");
                number = null;
			}
		}
		return number;
	}
}
