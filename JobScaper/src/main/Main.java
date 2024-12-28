package main;

import exceptionPackage.GenerateJobListingExeception;
import exceptionPackage.JobsToolsNotFoundExecption;

public class Main {
	public static void main(String[] args){
	
    	ScrapeGoogleJobs scraper = null; 
    	try {
			scraper = new ScrapeGoogleJobs(false, "Aerospace Engineering");
		} catch (JobsToolsNotFoundExecption | GenerateJobListingExeception e) {
			e.printStackTrace();
		} finally {
			scraper.close();
		}
			
    	
    	
    }
}
