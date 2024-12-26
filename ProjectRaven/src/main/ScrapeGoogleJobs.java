package main;

import java.util.ArrayList;
import java.util.Arrays;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ScrapeGoogleJobs {

	
	private WebDriver driver;
	
	ScrapeGoogleJobs (Boolean isHeadless, String searchInput) throws JobsToolsNotFoundExecption{
		searchInput = searchInput + " Jobs";
		ChromeOptions options = new ChromeOptions();
		
		if(isHeadless) {
			options.addArguments("--headless=new");
		}
		
		options.setPageLoadStrategy(PageLoadStrategy.EAGER);
    	options.addArguments("--window-size=1920x1080");  
    	String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36";;
    	options.addArguments("user-agent=/" + userAgent);
        options.addArguments("--disable-gpu");
        
    	driver = new ChromeDriver(options);
		driver.get("https://www.google.com/");
	
		searcher(searchInput);
		jobListingsOnly();
	}
	
	public void searcher(String searchInput){
		
		WebElement searchBarInput = driver.findElement(By.xpath("//*[@id=\"APjFqb\"]"));
		//The "\n" at the end acts as an enter for the search bar
		searchBarInput.sendKeys(searchInput + "\n");
	}
	
	public void jobListingsOnly() throws JobsToolsNotFoundExecption {
		
		String toolsXpath = "/html/body/div[3]/div/div[4]/div/div/div/div/div[1]/div/div/div/div[1]/div[1]";
		String toolsXpathBackup = "/html/body/div[3]/div/div[3]/div/div/div/div/div/div/div[1]";
		String[] tools = {""};
		
		try {
			tools = driver
					.findElement(By.xpath(toolsXpath))
					.getText()
					.split("\n");			
		} catch(NoSuchElementException e) {
			tools = driver
					.findElement(By.xpath(toolsXpathBackup))
					.getText()
					.split("\n");			
		}
		
		ArrayList<String> toolsList = new ArrayList<>(Arrays.asList(tools));
		
		int jobsButtonIndex = -1;
		for(int i = 0; i < toolsList.size() && jobsButtonIndex == -1; i++) {
			if(toolsList.get(i).equals("Jobs")) {
				jobsButtonIndex = i + 1;
			}
		}
		
		if(jobsButtonIndex == -1) {
			throw new JobsToolsNotFoundExecption();
		}
		
		String jobsXpath = "/html/body/div[3]/div/div[4]/div/div/div/div/div[1]/div/div/div/div[1]/div[1]/div/div[" + jobsButtonIndex + "]/a/div";
		String jobsXpathBackup = "/html/body/div[3]/div/div[3]/div/div/div/div/div/div/div[1]/div/div[" + jobsButtonIndex + "]/a/div";
		
		WebElement jobsButton;
		try {
			jobsButton = driver.findElement(By.xpath(jobsXpath));
		} catch (NoSuchElementException e) {
			jobsButton = driver.findElement(By.xpath(jobsXpathBackup));
		}
		jobsButton.click();
	}
	
	public void selectJobListing(int index) {
		
		String jobListingXpath = "/html/body/div[3]/div/div[13]/div/div/div[2]/div[2]/div/div/div/div/div/div/div/div[3]/div/div/div/div/infinity-scrolling/div[1]/div[1]/div[" + index + "]/div[1]/div/div/div/a";
		WebElement jobListing = driver.findElement(By.xpath(jobListingXpath));
		jobListing.click();
	}
	
	public JobApplication ScrapeCurrentListing() {
		return null;
	}
}










