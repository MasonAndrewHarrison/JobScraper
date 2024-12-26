package main;
import java.util.ArrayList;
import java.util.List;

public record JobApplication(String jobID, String description, String details, List<String> benefits, Location location) {

	public JobApplication(String jobID, String description, String details, List<String> benefits, Location location){
		this.jobID = jobID;
		this.description = description;
		this.details = details;
		this.benefits = benefits;
		this.location = location;
	}
	
	public JobApplication(String jobID){
		this(jobID, null, null, null, null);
	}
  	
}