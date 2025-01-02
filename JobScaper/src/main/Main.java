package main;

import java.util.ArrayList;

import exceptionPackage.GenerateJobListingExeception;
import exceptionPackage.JobsToolsNotFoundExecption;

public class Main {
	public static void main(String[] args){
	
    	ScrapeGoogleJobs scraper = null; 
    	try {
			scraper = new ScrapeGoogleJobs(false, "Aerospace Engineering");
			saveJobListings(scraper.getJobListings());
		//TODO make hierarchy of Execptions and throw one here that closes the scraper if it fails.
		} catch (JobsToolsNotFoundExecption | GenerateJobListingExeception e) {
			e.printStackTrace();
		} finally {
			scraper.close();
		}
    	
    }
	
	public static void saveJobListings(ArrayList<JobApplication> jobListings) {
		
	}
}
