package handle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import data.Data;
import data.Hospital;
import data.Keywords;

public class Handle {
	
	public static void modify(){
		//simplify the province and city
		modify_address(Data.baseHospitals);
		modify_address(Data.newhopHospitals);
		
		//simplify the name of the hospital
		modify_hname(Data.baseHospitals);
		modify_hname(Data.newhopHospitals);
		
		//generate the matchString
		generate_match(Data.baseHospitals);
		generate_match(Data.newhopHospitals);
		
		//output
//		output(Data.baseHospitals);
//		output(Data.newhopHospitals);
		
	}
	
	public static void modify_address(Vector<Hospital> vector){
		for(Hospital hospital:vector){
			String strips[] = {"省","市","自治区","县","区"};
			for(String strip:strips){
				hospital.province = hospital.province.replace(strip, "");
				hospital.city = hospital.city.replace(strip, "");
				hospital.city_2 = hospital.city_2.replace(strip, "");
			}
		}
	}
	
	public static void modify_hname(Vector<Hospital> vector){
		String strips[] = {"省","自治区"};
		String citys[] = {"县","区"};
		for(Hospital hospital:vector){
			// delete the strips
			int last = -1;
			for(String strip:strips){
				int index = hospital.hos_name.indexOf(strip);
				if(index >= 0 && index > last){
					last = index;
				}
			}
			if(last >= 0)
				hospital.hos_name = hospital.hos_name.substring(last+1);
			
			// modify the city_2
			int first = 100;
			int index = hospital.hos_name.indexOf("市");
			if(index >= 0 && index < first){
				first = index;
			}
			if(first >= 0 && first != 100 && first < 6){
				if(!hospital.city.equals(hospital.hos_name.substring(0,first)))
					hospital.city_2 = hospital.hos_name.substring(0,first);
				hospital.hos_name = hospital.hos_name.substring(first+1);
			}
			first = 100;
			for(String city:citys){
				index = hospital.hos_name.indexOf(city);
				if(index >= 0 && index < first){
					first = index;
				}
			}
			if(first >= 0 && first != 100 && first < 4){
				if(!hospital.city.equals(hospital.hos_name.substring(0,first)))
					hospital.city_2 = hospital.hos_name.substring(0,first);
				hospital.hos_name = hospital.hos_name.substring(first+1);
			}
			
			// delete the address
			if(hospital.hos_name.indexOf("大学") < 0 && hospital.hos_name.indexOf("医学院") < 0){
				hospital.hos_name = hospital.hos_name.replace(hospital.province, "");
				if(hospital.hos_name.indexOf(hospital.city) == 0)
					hospital.hos_name = hospital.hos_name.replace(hospital.city, "");
//				if(hospital.hos_name.indexOf(hospital.city_2) == 0)
//					hospital.hos_name = hospital.hos_name.replace(hospital.city_2, "");
			}
			
			// get the numString
			hospital.numString = Calculate.get_Num(hospital.hos_name);
			
			// get the illness
			for(String ill:Data.illness){
				if(hospital.hos_name.indexOf(ill) >= 0)
					hospital.ills.add(ill);
			}
		}
	}
	
	public static void generate_match(Vector<Hospital> vector){
		for(Hospital hospital:vector){
			hospital.matchString = hospital.province + hospital.hos_name;
			hospital.matchString = Keywords.simple(hospital.matchString);
		}
	}
	
	public static void output(Vector<Hospital> vector){
		try {
			@SuppressWarnings("resource")
			PrintStream printStream = new PrintStream(new File(vector.size() + ".txt"));
			for(Hospital hospital:vector)
				printStream.println(hospital.id + "\t" + hospital.province + "\t" + hospital.city + "\t" + hospital.matchString);
			printStream.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void handle(){
		try {
			@SuppressWarnings("resource")
			PrintStream printStream = new PrintStream(new File("output.txt"));
			double similarity = -10000;
			String bestString = "####";
			for(Hospital hospital:Data.newhopHospitals){
				similarity = 0;
				bestString = "####";
				// the keywords:
				TreeSet<String> keywords = new TreeSet<String>();
				String name = Keywords.simple(hospital.hos_name_copy);
				int length = name.length();
				for(int i = 0; i < length-1; i++)
					keywords.add(name.substring(i,i+2));
				// foreach baseHospital
				for(Hospital baseHospital:Data.baseHospitals){
					if(hospital.province.equals(baseHospital.province) 
							&& (hospital.city.equals(baseHospital.city) || hospital.city_2.equals(baseHospital.city) 
							    || hospital.city.equals(baseHospital.city_2) || hospital.city_2.equals(baseHospital.city_2)
							    || hospital.hos_name.indexOf(baseHospital.city) >= 0 || hospital.hos_name.indexOf(baseHospital.city_2) >= 0)
							){
						int distance = Calculate.getEditDistance(Keywords.simple_2(hospital.matchString), Keywords.simple_2(baseHospital.matchString));
						int length1 = hospital.matchString.length();
						int length2 = baseHospital.matchString.length();
						// consider about numString
						double numFactor = 1;
						if(hospital.numString.length() > 0 && baseHospital.numString.length() > 0){
							if(hospital.numString.equals(baseHospital.numString))
								numFactor = 0.2;
							else
								numFactor = 5;
						}
						// consider about the city
						double cityFactor = 1;
						if(baseHospital.hos_name.indexOf(baseHospital.city_2) >= 0 && hospital.hos_name.indexOf(baseHospital.city_2) >= 0){
							cityFactor = 0.5;
						}
							
						//consider about the illness
						double illFactor = 1;
						int size1 = hospital.ills.size();
						int size2 = baseHospital.ills.size();
						Set<String> allIll = new TreeSet<String>();
						for(String str:hospital.ills)
							allIll.add(str);
						for(String str:baseHospital.ills)
							allIll.add(str);
						int size = allIll.size();
						if(size1 != 0 && size2 != 0){
							if(size1+size2 == size){
								illFactor = 10;
							}
							else {
								illFactor = 0.01;
							}
						}
						
						// consider the keywords
						double keywordFactor = 0.00001;
						String baseName = Keywords.simple(baseHospital.hos_name_copy);
						for(int i = 0; i < baseName.length()-1; i++){
							String subString = baseName.substring(i,i+2);
							if(keywords.contains(subString)){
								keywordFactor += 1/ ((double)Keywords.keyMap.get(subString)+1);
							}
						}
//						if(hospital.hos_name_copy.contains("上海第九人民医院") && baseHospital.hos_name_copy.contains("儿童医疗保健中心")){
//							System.out.println(hospital.hos_name_copy);
//							System.out.println(baseHospital.hos_name_copy);
//							System.out.println(keywordFactor);
//						}
						
						// calculate the similarity
						double temp_similarity = 1 - distance*numFactor*cityFactor*illFactor / (double)(length1+length2);
						if(temp_similarity < 1)
							temp_similarity *= keywordFactor;
						if(temp_similarity > similarity){
							similarity = temp_similarity;
							bestString = baseHospital.province +"/"+ baseHospital.city +"/"+ baseHospital.hos_name_copy+"\t"+baseHospital.id;
						}
//						if(hospital.hos_name_copy.contains("大连海港医院") && baseHospital.hos_name_copy.contains("大连海事大学医院")){
//							System.out.println(baseHospital.hos_name_copy);
//							System.out.println("dis:"+distance);
//							System.out.println("num:"+numFactor);
//							System.out.println("city:"+cityFactor);
//							System.out.println("ill:"+illFactor);
//							System.out.println("keyword:"+keywordFactor);
//							System.out.println(temp_similarity);
//							System.out.println(hospital.hos_name_copy+","+ baseHospital.hos_name_copy);
//							System.out.println(hospital.matchString+","+ baseHospital.matchString);
//						}
					}
				}
				similarity = ((double)(similarity*100))/100.0; 
				while(similarity > 1){
					similarity /= 2;
				}
				printStream.println(hospital.province + "/" + hospital.city + "/" + hospital.hos_name_copy  + "\t" + bestString + "\t" + similarity);
			}
			printStream.flush();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		Data.getData();
		Keywords.analyze();
		modify();
		handle();
	}

}
