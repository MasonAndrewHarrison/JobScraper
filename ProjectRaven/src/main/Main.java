package main;

import java.io.IOException;
import java.util.ArrayList;

//TODO fix the exception madness
//TODO make this be able to handle asking you to sign in
//TODO make the delay inbetween actions able to be changed
public class Main {
    public static void main(String[] args) throws JobsToolsNotFoundExecption {
		
			ScrapeGoogleJobs scraper = new ScrapeGoogleJobs(false, "Aerospace Engineering");
			scraper.selectJobListing(10);
    }
}
