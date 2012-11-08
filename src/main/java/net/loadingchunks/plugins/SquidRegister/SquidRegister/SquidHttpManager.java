package net.loadingchunks.plugins.SquidRegister.SquidRegister;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;

public class SquidHttpManager {

	private SquidRegister plugin;
	
	public SquidHttpManager(SquidRegister plugin)
	{
		this.plugin = plugin;
	}
	
	public String registerUser(String username, String email, String mcusername)
	{
		List<BasicNameValuePair> formParams = new ArrayList<BasicNameValuePair>();
		formParams.add(new BasicNameValuePair("username", username));
		formParams.add(new BasicNameValuePair("email", username));
		formParams.add(new BasicNameValuePair("minecraft", username));
		formParams.add(new BasicNameValuePair("apikey", this.plugin.getConfig().getString("api.key")));
		
		HttpClient http = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(this.plugin.getConfig().getString("api.endpoint"));
		
		HttpResponse response;
		
		String output = null;
		
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, "UTF-8");
			response = http.execute(httpPost);
			
			if(response == null)
				return "failed_response";;
			
		    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		    String line = "";
		    while ((line = rd.readLine()) != null) {
		    	output = output + line;
		    }
		} catch (ClientProtocolException ex) {
			this.plugin.getLogger().severe("An odd error happened while trying to communicat with the Apache Server!");
			return "odd";
		} catch (IOException ex)
		{
			this.plugin.getLogger().severe("Could not connect to Apache Server to post registration details.");
			return "disconnect";
		}
			
		if(output == null)
			return "null";
		
		if(output.equalsIgnoreCase("success"))
			return "success";
		
		if(output.equalsIgnoreCase("duplicate_user"))
			return "duplicate_user";
			
		return "unknown";
	}
}
