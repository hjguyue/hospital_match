package data;

import java.util.Vector;

public class Hospital {
	public int id;
	public String province;
	public String city;
	public String city_2;
	public String hos_name;
	public String numString;
	public String matchString;
	public String hos_name_copy;
	public Vector<String> ills;
	
	public Hospital(int i, String p, String c, String h){
		id = i;
		province = p;
		city = c;
		city_2 = "";
		numString = "";
		hos_name = h;
		ills = new Vector<String>();
	}
}
