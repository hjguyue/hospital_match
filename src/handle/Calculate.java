package handle;

public class Calculate {
	
	public static void main(String args[]){
		String s = "延边市人民医院";
		System.out.println(strip(s));
		System.out.println(get_Num("第一人民医院"));
	}
	
	public static String strip(String str){
		// modify the city_2
		String citys[] = { "市", "县", "区" };
		int first = 100;
		for (String city : citys) {
			int index = str.indexOf(city);
			if (index >= 0 && index < first) {
				first = index;
			}
		}
		if (first >= 0){
			str = str.substring(first + 1);
		}
		return str;
	}
	
	public static double similarity(String str1, String str2){
		
		return 0;
	}
	
	private static int Minimum(int a, int b, int c) {
        int mi = a;
        if (b < mi)	mi = b;
        if (c < mi)	mi = c;
        return mi;
    }
	
	public static String get_Num(String str){
		int index = str.indexOf("第");
		if(index < 0 || str.length() < index+2)
			return "";
		return str.substring(index,index+2);
	}
   
    public static int getEditDistance(String s, String t) {
        int d[][]; // matrix
        int n; // length of s
        int m; // length of t
        int i; // iterates through s
        int j; // iterates through t
        char s_i; // ith character of s
        char t_j; // jth character of t
        int cost; // cost

        // Step 1

        n = s.length();
        m = t.length();
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        d = new int[n + 1][m + 1];

        // Step 2

        for (i = 0; i <= n; i++) {
            d[i][0] = i;
        }

        for (j = 0; j <= m; j++) {
            d[0][j] = j;
        }

        // Step 3

        for (i = 1; i <= n; i++) {
            s_i = s.charAt(i - 1);
            // Step 4
            for (j = 1; j <= m; j++) {
                t_j = t.charAt(j - 1);
                // Step 5
                if (s_i == t_j) {
                    cost = 0;
                } else {
                    cost = 1;
                }
                // Step 6
                d[i][j] = Minimum(d[i - 1][j] + 1, d[i][j - 1] + 1,
                        d[i - 1][j - 1] + cost);
            }
        }
        // Step 7
        return d[n][m];

    }
}
