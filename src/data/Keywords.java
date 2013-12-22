package data;

import java.util.TreeMap;

public class Keywords {

	public static TreeMap<String, Integer> keyMap;
	
	public static void analyze(){
		keyMap = new TreeMap<String, Integer>();
		for(Hospital hospital:Data.baseHospitals){
			String name = hospital.hos_name;
			name = simple(name);
			int length = name.length();
			for(int i = 0; i < length-1; i++){
				String subString = name.substring(i,i+2);
				if (!keyMap.containsKey(subString)) {
					keyMap.put(subString, 0);
				}
				keyMap.put(subString, keyMap.get(subString)+1);
			}
		}
//		for(String str:keyMap.keySet()){
//			System.out.println(str + ": " +keyMap.get(str));
//		}
//		System.out.println(keyMap.get("��ҽ"));
	}
	
	public static String simple(String str){
		String stripsString[] = {"ʡ","��","��","������","��","����"};
		for(String strip:stripsString){
			str = str.replace(strip, "");
		}
		return str;
	}

	public static String simple_2(String str){
		String stripsString[] = {"ʡ","��","��","������","��","��","����"};
		for(String strip:stripsString){
			str = str.replace(strip, "");
		}
		return str;
	}
	public static void main(String[] args) throws Exception{
		Data.getData();
		analyze();
	}
	
}
