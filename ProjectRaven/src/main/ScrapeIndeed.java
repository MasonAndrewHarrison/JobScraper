package main;


import java.io.File;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import lombok.Data;

@Data
public class ScrapeIndeed {
	
	private WebDriver driver;
	private String city;
	private String state;
	private String search;
	private int currentPage;
	
	ScrapeIndeed(Boolean isHeadless, String city, String state, String search){ 
		ChromeOptions options = new ChromeOptions();
		currentPage = 1;
		if(isHeadless) {
			options.addArguments("--headless=new");
		}
		
		options.setPageLoadStrategy(PageLoadStrategy.EAGER);
    	options.addArguments("--window-size=1920x1080");  
    	String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36";;
    	options.addArguments("user-agent=/" + userAgent);
        options.addArguments("--disable-gpu");
    	driver = new ChromeDriver(options);
    	
		this.city = city;
		this.state = state;
		this.search = search;
		driver.get("https://www.indeed.com/jobs?q=" + search + "&l=" + city + "%2C+" + state + "&from=searchOnHP&vjk=");
	}
	
	ScrapeIndeed(Boolean headless){
		this(headless, "", "", "");
	}
	
	public List<WebElement> generateTitleElements() throws NoSuchElementException { 
		List<WebElement> jobElements = new ArrayList<WebElement>();
		WebElement element = null;
		element = driver.findElement(By.id("mosaic-jobResults"));
		List<WebElement> elementList = element.findElements(By.tagName("a"));		

		for(WebElement e: elementList) {
			if(e.isDisplayed() && (!e.getText().equals("View similar jobs with this employer"))) { 
				jobElements.add(e); 
			}
		}
		return jobElements;
	}
	
	public WebElement getElementByXPath(String xPath) throws NoSuchElementException{
		return driver.findElement(By.xpath(xPath));
	}
	
	public String getTextByXPath(String xPath) throws NoSuchElementException{
		return getElementByXPath(xPath).getText(); 
	}
	
	//TODO divide the one long string into indiviual elements
	public List<String> getListFromElement(String xPath){
		WebElement element;
		List<String> stringList = new ArrayList<>(); 
		element = driver.findElement(By.xpath(xPath));
		String text = element.getText();
		int boilerPlateLength = ("Benefits Pulled from the full job description").length();
		String fullList = text.substring(boilerPlateLength);
		System.out.println(fullList);
		stringList.add(fullList);
		return stringList;		
	}

	//TODO make getListFromElement work
	public String getBenefits(){
		try {
			//return getListFromElement("/html/body/main/div/div[2]/div/div[5]/div/div[2]/div/div/div[2]/div[2]/div[2]/div/div[2]/div[7]/div[1]/div/span/ul");
			return getTextByXPath("/html/body/main/div/div[2]/div/div[5]/div/div[2]/div/div/div[2]/div[2]/div[2]/div/div[2]/div[7]/div[1]/div/span/ul");
		} catch(NoSuchElementException e1) {
			try { 
				//return getListFromElement("//*[@id=\"benefits\"]");
				return getTextByXPath("//*[@id=\"benefits\"]");
			
			} catch(NoSuchElementException e2) {
				return null; 
			}
		}
	}
	
	public String getDetails() {
		try {
			return getTextByXPath("/html/body/main/div/div[2]/div/div[5]/div/div[2]/div/div/div[2]/div[2]/div[2]/div/div[2]/div[4]/div/div/div/div/div[1]/div[2]");		
		} catch(NoSuchElementException e1) {
			try { 
				return getTextByXPath("//*[@id=\"details\"]"); 
			} catch(NoSuchElementException e2) { 
				return null; 
			}
		}
	}
	
	public String getDescription() {
		try {
			return getTextByXPath("/html/body/main/div/div[2]/div/div[5]/div/div[2]/div/div/div[2]/div[2]/div[2]/div/div[2]/div[10]/div[2]/div");
		} catch(NoSuchElementException e1) {
			try {
				return getTextByXPath("//*[@id=\"jobDescriptionText\"]");
			} catch(NoSuchElementException e2) {
				return null; 
			}
		}
	}
	
	//TODO get this to return a location object
	public String getLocation() { 
		try {
			return getTextByXPath("/html/body/main/div/div[2]/div/div[5]/div/div[2]/div/div/div[2]/div[2]/div[1]/div/div[2]/div[2]/div/div/div/div[2]/div");
		} catch(NoSuchElementException e1) {
			try {
				return getTextByXPath("/html/body/main/div/div[2]/div/div[5]/div/div[2]/div/div/div[2]/div[2]/div[1]/div/div[1]/div[2]/div/div/div/div[2]/div");
			} catch(NoSuchElementException e2){ 
				return null;
			}
		}
	}
	
	public void clickElement(WebElement element) throws ButtonNotFoundException{
		try {
			element.click();
		} catch(ElementClickInterceptedException e1) {
			throw new ButtonNotFoundException();
		} catch(StaleElementReferenceException e2) {
			throw new ButtonNotFoundException();
		}
	}
	
	public void navigateBack() {
		driver.navigate().back();
	}
	
	public void refreshPage() {
		driver.navigate().refresh();
	}
	
	public void close() {
		driver.quit();
	} 
	
	public String getLinkToJob() {
		return city;
	}
	
	public Boolean isInViewJobMode() {
		String URL = driver.getCurrentUrl();
		return URL.contains("https://www.indeed.com/viewjob?jk=");
	}
	
	public Boolean isCaptchaVisible() {
		String URL = driver.getCurrentUrl();
		String webTitle = driver.getTitle();
		if (URL.contains("https://www.indeed.com/rc/clk")){
			return true;
		}else if(webTitle.equals("Just a moment...")) {
			return true;
		} else if(webTitle.equals("Security Check - Indeed.com")) {
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean isInSearchMode() {
		String URL = driver.getCurrentUrl();
		return URL.contains("https://www.indeed.com/jobs?q=");		
	}
	
	public String getJobID() throws CloudFlareCaughtYouException{
		String URL = driver.getCurrentUrl();
		String jobID = null;
		if(isInSearchMode()) {
			String textBeforeJobID = "&vjk";
			int startIndex = URL.indexOf(textBeforeJobID);
			jobID = URL.substring(startIndex + textBeforeJobID.length() + 1); 
		} else if(isCaptchaVisible()) {
			throw new CloudFlareCaughtYouException();
		} else if(isInViewJobMode()) {
			String textBeforeJobID = "viewjob?jk=";
			int startIndex = URL.indexOf(textBeforeJobID);
			jobID = URL.substring(startIndex + textBeforeJobID.length());
		}
		
		int jobIDLength = jobID.length();
		if(jobIDLength == 16) {
			return jobID;
		} else if(jobIDLength > 16) {
			return jobID.substring(0, 16);
		} else {
			return"Error job ID is not 16 or more characters long";
		}
	}
	
	public JobApplication getJobApplication() throws CloudFlareCaughtYouException {
		String jobID = getJobID();
		List<String> benefits = new ArrayList<>();
		benefits.add(getBenefits());
		String details = getDetails();
		String description = getDescription();
		//String location = getLocation();
		return new JobApplication(jobID, description, details, benefits, null);
	}
	
	/**
	 * Scrapes through the current page. This will throws a CloudFlareCaughtYouException if it is caught by
	 * CloudFlare. 
	 * @return
	 * @throws ButtonNotFoundException
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws CloudFlareCaughtYouException
	 */
	public ArrayList<JobApplication> getJobsFromPage() throws ButtonNotFoundException, IOException, InterruptedException, CloudFlareCaughtYouException{
		List<WebElement> jobElements = new ArrayList<>();
		try {
			jobElements = generateTitleElements();
		} catch(NoSuchElementException e) {
			if(isCaptchaVisible()) {
				throw new CloudFlareCaughtYouException();
			}
		}
		
		ArrayList<JobApplication> jobList = new ArrayList<>();
		for(int i = 0; i < jobElements.size(); i++) {
			try {
				clickElement(jobElements.get(i));
			takeScreenShot();
			//TODO make this wait to load until URL has changed instead of just 600
			Thread.sleep(600);
			jobList.add(getJobApplication());
			System.out.println("Page: " + currentPage + " index: " + i + " jobID: " + getJobID());
			} catch(CloudFlareCaughtYouException e) {
				return jobList;
			}
		}
		return jobList;		
	}
	
	/**
	 * This function will loop through Indeed pages until it is caught by CloudFlare. Until 
	 * it is caught it will scraper all data and save it in a .json file. Once it is caught
	 * the function will terminate. The first page starts on 1.
	 * @return	
	 * @throws ButtonNotFoundException
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws CloudFlareCaughtYouException
	 */
	public ArrayList<JobApplication> loopThroughPages(int startPage) throws ButtonNotFoundException, InterruptedException, IOException{
		Thread.sleep(500);
		ArrayList<JobApplication> jobList = new ArrayList<>();
		
		try {
			if(currentPage != startPage) {
				navigateToThisPage(startPage);
			}
			currentPage = startPage;
			while(!isCaptchaVisible()) {
				List<JobApplication> jobListOnPage = getJobsFromPage();
				jobList.addAll(jobListOnPage);
				navigateToNextPage();
				Thread.sleep(1000);
			}			
		} catch (CloudFlareCaughtYouException e) {
			return jobList;
		}
		return jobList;
	}
	
	public void navigateToNextPage() throws ButtonNotFoundException, CloudFlareCaughtYouException {
		currentPage++;
		navigateToThisPage(currentPage);
	}
	
	public ArrayList<Integer> getPossiblePagesNaviagation(){
		
		ArrayList<Integer> arrayOfNums = new ArrayList<>();
		String nums = driver
				.findElement(By.xpath("/html/body/main/div/div[2]/div/div[5]/div/div[1]/nav/ul")).getText()
				.replaceAll("\n", ", ");
		
		for(int i = 0; i < nums.length(); i++) {
			Boolean endOfNumFound = false;
			for(int j = 0;!endOfNumFound; j++) {
				
				if(i == nums.length()-1) {
					endOfNumFound = true;
					String stringNum = nums.substring(i, i+1);
					arrayOfNums.add(Integer.parseInt(stringNum));
					
				} else if(nums.substring(i, i + j).contains(", ")) {
					endOfNumFound = true;
					String stringNum = nums
							.substring(i, i + j)
							.replace(", ", "");
					arrayOfNums.add(Integer.parseInt(stringNum));
					i = i + j - 1;			
				}
			}
		}
		return arrayOfNums;
	}
	
	private void uncheckedNavigateToThisPage(int pageNumber) {
		if(5 == getPossiblePagesNaviagation().getLast() && pageNumber <= 5) {
			driver.findElement(By.xpath("/html/body/main/div/div[2]/div/div[5]/div/div[1]/nav/ul/li[" + pageNumber + "]/a")).click();
		} else {
			int indexOfPageNumber = getPossiblePagesNaviagation().indexOf(pageNumber)+2;
			driver.findElement(By.xpath("/html/body/main/div/div[2]/div/div[5]/div/div[1]/nav/ul/li[" + indexOfPageNumber + "]/a")).click();
		}
	}
	
	/**
	 * This navigates to the desired page number. The first page starts on 1.
	 * @param pageNumber
	 * @throws ButtonNotFoundException
	 * @throws CloudFlareCaughtYouException 
	 */
	public void navigateToThisPage(int pageNumber) throws ButtonNotFoundException, CloudFlareCaughtYouException {
		try {
			
			Boolean myPageIsFound = false;
			while(!myPageIsFound) {
				int highestPossiblePage = getPossiblePagesNaviagation().getLast();
				if(pageNumber > highestPossiblePage) {
					uncheckedNavigateToThisPage(highestPossiblePage);
				} else if(getPossiblePagesNaviagation().contains(pageNumber)) {
					myPageIsFound = true;
					uncheckedNavigateToThisPage(pageNumber);
				}else {
					System.out.println("error");
				} 
			}
			
		}catch (NoSuchElementException e) {
			if(isCaptchaVisible()) {
				throw new CloudFlareCaughtYouException();
			} else {
				throw new ButtonNotFoundException();
			}
		}		
	}
	
	public void searchJobID(String JobID) {
		driver.navigate().to("https://www.indeed.com/viewjob?jk=" + JobID);
	}
	
	public void takeScreenShot() throws IOException {
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scrFile, new File("c:\\tmp\\screenshot.png"));
	}
	
}

//sneaky sneaky
