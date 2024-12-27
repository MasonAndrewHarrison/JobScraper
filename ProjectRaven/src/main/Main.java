package main;

public class Main {
    public static void main(String[] args) throws JobsToolsNotFoundExecption, InterruptedException {
		
			ScrapeGoogleJobs scraper = new ScrapeGoogleJobs(false, "Aerospace Engineering");
			
			//TODO fix "SocketException"
			scraper.close();
    }
}
