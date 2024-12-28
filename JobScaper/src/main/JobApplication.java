package main;
import java.util.ArrayList;
import java.util.List;

public record JobApplication(
		String jobLink, 
		String company, 
		String location, 
		String dateUpLoaded, 
		ArrayList<String> qualifications, 
		ArrayList<String> benefits, 
		ArrayList<String> responsibilities, 
		String description) {
}