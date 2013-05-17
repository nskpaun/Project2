import java.util.Calendar;


public class RunNate {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
		
		GoogleShop gs = new GoogleShop();
		gs.gQuery("iPad");
		
		System.out.println(c.getTimeInMillis());
		

	}

}
