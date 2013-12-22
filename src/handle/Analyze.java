package handle;


import data.Data;
import data.Hospital;

public class Analyze {

	public static void analyze() throws Exception{
		Data.getData();
		int sum = 0;
		for(Hospital hospital:Data.newhopHospitals){
			for(Hospital baseHospital:Data.baseHospitals){
				if(baseHospital.hos_name.equals(hospital.hos_name)){
					sum++;
					break;
				}
			}
		}
		System.out.println(sum);
//		Set<String> set = new TreeSet<String>();
//		int num = 0;
//		for(Hospital hospital:Data.baseHospitals){
//			if(set.contains(hospital.hos_name)){
//				num++;
//				System.out.println(hospital.hos_name);
//			}
//			else {
//				set.add(hospital.hos_name);
//			}
//		}
//		System.out.println(num);
	}
	
	public static void main(String[] args) throws Exception{
		analyze();
	}
}
