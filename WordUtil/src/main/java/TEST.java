import org.apache.bcel.generic.NEW;

public class TEST {

	private static final String[] Session = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(calString());
		

	}
	public static String calString()
    {
        String wpg = "www.wpgholdings.com";
        try{
             String[] wpg1=wpg.split(".");
             
            String wpg2 = wpg.substring(wpg.indexOf('.'), "wpgholdings".length());

            if (wpg1[1] == wpg2)
                return "Y";
            else
                return "N";
        }catch (Exception e) {
			return "123";
		}
        
        
        
    }
  

}
