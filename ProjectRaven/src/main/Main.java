package main;

public class Main {
    @SuppressWarnings("null")
	public static void main(String[] args){
			
    	ScrapeGoogleJobs scraper = null;
    	try {
			scraper = new ScrapeGoogleJobs(false, "Aerospace Engineering");
		} catch (JobsToolsNotFoundExecption | GenerateJobListingExeception e) {
			e.printStackTrace();
		} finally {
			//scraper.close();
		}
			
    	
    	
    }
}
