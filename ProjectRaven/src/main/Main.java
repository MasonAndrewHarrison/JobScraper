package main;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws JobsToolsNotFoundExecption, InterruptedException {
		
			ScrapeGoogleJobs scraper = new ScrapeGoogleJobs(false, "Aerospace Engineering");
			//Thread.sleep(1000);
			//TODO sometimes element can't be found
			scraper.selectJobListing(3);
    }
}
