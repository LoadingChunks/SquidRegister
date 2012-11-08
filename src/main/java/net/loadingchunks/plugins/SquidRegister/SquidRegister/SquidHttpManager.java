package net.loadingchunks.plugins.SquidRegister.SquidRegister;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class SquidHttpManager {

	private SquidRegister plugin;
	
	public SquidHttpManager(SquidRegister plugin)
	{
		this.plugin = plugin;
	}
	
	public String registerUser(String username, String email, String mcusername)
	{
		String output = "";
		try {
			URL url;
		    URLConnection urlConn;
		    DataOutputStream printout;
		    DataInputStream input;
		    // URL of CGI-Bin script.
		    url = new URL(this.plugin.getConfig().getString("api.endpoint"));
		    // URL connection channel.
		    urlConn = url.openConnection();
		    // Let the run-time system (RTS) know that we want input.
		    urlConn.setDoInput(true);
		    // Let the RTS know that we want to do output.
		    urlConn.setDoOutput(true);
		    // No caching, we want the real thing.
		    urlConn.setUseCaches(false);
		    // Specify the content type.
		    urlConn.setRequestProperty
		    ("Content-Type", "application/x-www-form-urlencoded");
		    // Send POST output.
		    printout = new DataOutputStream(urlConn.getOutputStream ());
	
		    String content = "";
		    content += "username=" + URLEncoder.encode(username, "UTF-8");
		    content += "&email=" + URLEncoder.encode(email, "UTF-8");
		    content += "&mcusername=" + URLEncoder.encode(mcusername, "UTF-8");
		    content += "&key=" + URLEncoder.encode(this.plugin.getConfig().getString("api.key"), "UTF-8");
	
		    printout.writeBytes (content);
		    printout.flush();
		    printout.close();
		    // Get response data.
		    input = new DataInputStream(urlConn.getInputStream());
		    String str;
		    
		    while (null != ((str = input.readLine())))
		    	output += str;//System.out.println (str);
	
		    input.close ();
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		
		this.plugin.getLogger().info("Got " + output + " from Endpoint.");
		
		if(output.equals("invalid_key"))
			return output;
			
		if(output.equals(""))
			return "failed_response";
			
		if(output == null)
			return "null";
				
		if(output.equalsIgnoreCase("success"))
			return "success";
		
		if(output.equalsIgnoreCase("duplicate_user"))
			return "duplicate_user";
		
		if(output != null)
			return output;
			
		return "unknown";
	}
}
