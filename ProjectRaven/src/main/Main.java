package main;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws JobsToolsNotFoundExecption, InterruptedException {
		
			ScrapeGoogleJobs scraper = new ScrapeGoogleJobs(false, "Aerospace Engineering");
			Thread.sleep(2000);
			scraper.selectJobListing(1);
			System.out.println(scraper.scrapeLink(1500));
			scraper.close();
    }
}
