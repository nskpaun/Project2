import java.io.IOException;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;


public class runserver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Sender sender = new Sender("AIzaSyD-17zlwlrLFjqIuASpMrNPJ65aWiediRE");
		Message message = new Message.Builder().collapseKey("1").timeToLive(3).delayWhileIdle(true).addData("message", "this text will be in the notificationbar!!").build();//.build();
		Result result = null;
		try {
			result = sender.send(message,"APA91bEz8N8U5vueC_7ZCiOU5xncII6xJSbBXw2ULZsk-D0-gK6yUq2z0MBOlp_SRhRbQmAMLa2C4Oye3lMF6pl7XzWmKTwTCzBBYxEQIPsAudFuVx6aBWmImzqFYL2y_Gnxyayo-ljfDQjum4DPpoozGl200KCUTA", 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(result.toString());

	}

}
