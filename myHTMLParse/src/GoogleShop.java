import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;




public class GoogleShop {
	private String GOOGLE_API_KEY = "AIzaSyCeQZg095BGp-noLSTPegE9uXcNVD67vQY";
	
	public void gQuery(String query){
		String myURL = "https://www.googleapis.com/shopping/search/v1/public/products?key="+GOOGLE_API_KEY+"&country=US&q=digital+camera&alt=JSON";
		System.out.println(myURL);
        try {
            URL google = new URL(myURL);
            URLConnection yc = google.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc
                    .getInputStream()));
            String inputLine;
            String finalLine = "";
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                finalLine = finalLine+inputLine;
            }
            


        } catch (Exception e) {
            e.printStackTrace();
        }
	}

}
