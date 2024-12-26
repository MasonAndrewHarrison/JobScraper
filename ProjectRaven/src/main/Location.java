package main;
import lombok.Data;

@Data
public class Location {
	
	private String city;
	private int zipCode;
	private String road;
	
	Location(String city, int zipCode, String road){
		this.city = city; 
		this.zipCode = zipCode;
		this.road = road;
	}
	
}
