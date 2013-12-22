package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Data {

	public static Vector<Hospital> baseHospitals;
	public static Vector<Hospital> newhopHospitals;
	public static Vector<String> illness;
	
	@SuppressWarnings("resource")
	public static void getData() throws Exception{
		baseHospitals = new Vector<Hospital>();
		newhopHospitals = new Vector<Hospital>();
		illness = new Vector<String>();
		
		BufferedReader reader;
		String line;
		// load the base data:
		reader = new BufferedReader(new FileReader("data/base.txt"));
		while((line = reader.readLine()) != null  && line.length() > 0){
			line = line.replace(" ", "");
			String strs[] = line.split("\t");
			if(strs.length != 5){
				System.err.println("You should make sure that each line in the base data has five columns...");
				System.out.println("but one line is: " + line);
			}
			Hospital hospital = new Hospital(Integer.parseInt(strs[0]),strs[1],strs[2],strs[4]);
			hospital.city_2 = strs[3];
			hospital.hos_name_copy = hospital.hos_name;
			baseHospitals.add(hospital);
		}
		// load the new data
		reader = new BufferedReader(new FileReader("data/new.txt"));
		while((line = reader.readLine()) != null && line.length() > 0){
			line = line.replace(" ", "");
			String strs[] = line.split("\t");
			if(strs.length != 3){
				System.err.println("You should make sure that each line in the new data has three columns...");
				System.out.println("but one line is: " + line);
			}
			Hospital hospital = new Hospital(0,strs[0],strs[1],strs[2]);
			hospital.hos_name_copy = hospital.hos_name;
			newhopHospitals.add(hospital);
		}
		
		// load the illness
		reader = new BufferedReader(new FileReader("data/illness.txt"));
		while((line = reader.readLine()) != null){
			line = line.replace(" ", "");
			line = line.replace("\t", "");
			line = line.replace("\r", "");
			line = line.replace("\n", "");
			illness.add(line);
		}
		
//		System.out.println(baseHospitals.size());
//		System.out.println(newhopHospitals.size());
//		System.out.println(illness.size());
	}
	
	public static void main(String[] args) throws Exception{
		getData();
	}

}
