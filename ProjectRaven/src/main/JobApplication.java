package main;
import java.util.ArrayList;
import java.util.List;

public record JobApplication(
		ArrayList<String> jobURLs, 
		String company, 
		String location, 
		int daysAgo, 
		ArrayList<String> qualifications, 
		ArrayList<String> benefits, 
		ArrayList<String> responsibilities, 
		String description) {
}