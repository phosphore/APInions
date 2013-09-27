package controllers;

import play.*;
import play.mvc.*;
import play.api.db.DB;

import java.net.InetAddress;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import views.html.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;


import static play.data.Form.*;
import play.data.*;
import models.*;
import java.text.DateFormat;

import net.firefang.ip2c.*;
import org.codehaus.jackson.JsonNode;


public class Application extends Controller {
  
	public static String Jstable;
	public static String Imgc;
	public static String percentages;
	public static int rft;
	public static int totalvotetab;
	public static String perchart;
	public static String perchartend;
	

	
    public static Result index() {
		//String user = session("connected");
		// if(user != null) {
			  return redirect(routes.Application.dashboard());
		//  } else {
		//return redirect(routes.Application.login());
    }

    
    public static Result login() {
		return ok(login.render(form(Login.class)));
	}
    
    public static Result logout() {
		
		session().clear();
		flash("success", "You've been logged out");
		return redirect(
			routes.Application.Votes()
		);
	}
	
	public static Result authenticate() {
		Form<Login> loginForm = form(Login.class).bindFromRequest();
		if (loginForm.hasErrors()) {
			return badRequest(login.render(loginForm));
		} else {
			session().clear();
			session("email", loginForm.get().email);
			return redirect(
				routes.Application.dashboard()
			);
		}
	}

    public static Result postVote() {
    	
    	int vote;
    	String par_a, par_b, par_c;
    	JsonNode json = request().body().asJson();
    	  if(json == null) {
    	    return badRequest("Expecting Json data");
    	  } else {
    		//JSON TREE -> TOBEDEFINED
    	    vote = json.findPath("vote").getIntValue();
    	    par_a = json.findPath("par_a").getTextValue();
    	    par_b = json.findPath("par_b").getTextValue();
    	    par_c = json.findPath("par_c").getTextValue();
    	    
    	    if(par_a == null || par_b == null || par_c == null) {
    	      return badRequest("Missing parameters");
    	    } else {
    	    	String remote = request().remoteAddress();
    			if (remote.contains(":"))
    				remote = "127.0.0.1";
    			Connection conn = play.db.DB.getConnection();
    			String timeStamp = new SimpleDateFormat("HH:mm:ss yyyy/MM/dd").format(Calendar.getInstance().getTime());

    			
    				if (vote <= 5 && vote >= 1) 
    				{
    				try {
    					Statement stat = conn.createStatement();
    					stat.executeUpdate("INSERT INTO Votes VALUES (DEFAULT, "+vote+", '"+remote+"', '"+timeStamp+"','"+par_a+"','"+par_b+"','"+par_c+"')");
    				    conn.close();
    				} catch (SQLException e)
    				{
    					e.printStackTrace();
    					
    				} finally {
    							return ok();
    						  }
    				}
    				else
    				{
    					//400: Bad request
    				return badRequest("Vote must be between 1 and 5");
    				}
    	    }
    	  }
		
}

@Security.Authenticated(Secured.class)
	public static Result deleteVote(String id) {
		Connection conn = play.db.DB.getConnection();
		if (id.matches("\\d+"))
		{
		int idclean = Integer.parseInt(id);
			try {
				Statement stat = conn.createStatement();
				stat.executeUpdate("DELETE FROM votes WHERE ID="+id+";");
			    conn.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
				
			} finally {
						return ok();
					  }
			}
			else
			{
				//400: Bad request
			return badRequest("ID must be a number!");
			}
	
}

@Security.Authenticated(Secured.class)
	public static Result requests() {
		return ok(feedback.render());
	}

	
@Security.Authenticated(Secured.class)
	public static Result Votes() {
    
		String[] row = new String[4];
		
		List<String[]> listed = new ArrayList<String[]>();
		Connection conn = play.db.DB.getConnection();
		String tablecontent = "";
		    try {
				ResultSet rs;
				Statement stat = conn.createStatement();
				rs = stat.executeQuery("SELECT * FROM Votes");
				while (rs.next()) {
					row[0] = rs.getString("Vote");
					row[1] = rs.getString("IP");
					row[2] = rs.getString("Time");
					row[3] = rs.getString("ID");
					String[] tmp = row.clone();
					listed.add(tmp);
				}
			    conn.close();
				}
			catch (SQLException e)
				{
					e.printStackTrace();
				}
			finally 
				{
					tablecontent = tabber(listed);
					return ok(Votes.render(tablecontent, Jstable, percentages, Integer.toString(rft), perchart, Integer.toString(totalvotetab)));

				}
  

}


	@Security.Authenticated(Secured.class)
    public static Result dashboard() {
			return ok(dashboard.render());
	}


	public static String tabber(List<String[]> listed) {
				  rft = 0;
                  int num = 0;
                  int numrow = 0;
                  int vote = 0;
                  int percchartnum = 0;
                  String ipadd = "";
                  String table = "";
                  String result = "";
                  double[] percentvoti = new double[5];
                  percentages = "";
                  String time = "";
                  Jstable = "";
                  String countryname = "";
                  String today = "";
                  perchart = "";
                  perchartend = "";
                  Imgc = "";
                  SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss yyyy/MM/dd");//("yyyy-MM-dd'T'HH:mm:ss':10.280Z'");
                  DateFormat df_tab = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				  DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
				  SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				  Date date = new Date();
				  today = sdf.format(date);
                  long sum = 0;
                 
                  
                  String jsdate = "";
                  if (listed.size() == 0)
                  {
					  table = "<tr class=\"warning\"><td><i>No votes yet!</i></td><td>-</td><td>-</td><td>-</td></tr>";
					  percentages = "<tr class=\"warning\"><td><i>No votes yet!</i></td><td>-</td><td>-</td><td>-</td></tr>";
				  } else {
                  for (int i = 0; i < listed.size();i++)
                  {
						if (i != 0)
							Jstable = Jstable+",";
						try {
						vote = Integer.parseInt(listed.get(i)[0]);
						num = Integer.parseInt(listed.get(i)[3]);
						} catch (Exception ex)
						{
							table = ".. Something went wrong! :(";
							break;
						} finally {
					    if (vote >= 4)
							result="success";
						else if (vote == 3)
							result="";
						else
							result="danger";
						
					    percentvoti[vote-1]++;
					    
						ipadd=listed.get(i)[1];
						time=listed.get(i)[2];
						sum += vote;
						try {
						date = df.parse(time);
						
						} catch (Exception ex)
						{
							ex.printStackTrace();
						} finally {
							if (today.equals(sdf.format(date)))
								rft++;
							jsdate = df2.format(date);
							Jstable = Jstable + "{ \"Date\": \""+jsdate+"\", \"Votes\": "+vote+" }";
							
							
							
						int caching2 = IP2Country.MEMORY_MAPPED;
						try{
						IP2Country ip2c = new IP2Country("ip2c/ip-to-country.bin",caching2);
						Country c = ip2c.getCountry(ipadd);

							if (c != null)
							{
								countryname = c.getName();
							} else 
								Imgc = "UNKNOWN";
							if (countryname == "" || c == null)
							{
								Imgc = "UNKNOWN";	
							}
							else
							{		
								Imgc = c.get2cStr().toLowerCase();	
							}
						
							
						} catch (Exception ex)
						{
						ex.printStackTrace();
						} finally {
							
						table = table + "<div id=\"del"+numrow+"\" style=\"display: block\"><tr id=\"del"+numrow+"\" class=\""+result+"\"><td>"+num+"</td><td><b>"+vote+"</b></td><td>"+ipadd+"<img src=\"/assets/images/"+Imgc+".png\" title=\""+countryname+"\" align=\"right\"></img></td><td>"+df_tab.format(date)+"<img class=\"delete\" src=\"/assets/delete.png\" onclick=\"deleteVote("+num+",this,"+numrow+");\" align=\"right\"></img></td></tr></div>";
						numrow++;
						}
					}
					
					
				}
				
			}
                  int mean = (int)Math.round((((double) sum) / listed.size()));
  				table = table + "<br/></tbody></table><table class=\"table table-striped table-bordered table-hover\" id=\"meantable\"><tr class=\"warning\"><td><p id=\"meanp\"><b>~ Overall mean</b></p></td><td><p id=\"meanp2\">"+mean+"</p></td><td>-</td><td>-</td></tr></table>";
				double size = listed.size();
				totalvotetab = (int)size;
  				for (int i = 5; i > 0; i--)
				{
					int perc = 0;
					perc = Math.round((float)((percentvoti[i-1]/size)*100));
	  				percentages = percentages + "<tr class=\""+result+"\"><td><i>"+i+"</i></td><td><b>"+perc+" %</b></td></tr>"; 
	  				perchart = perchart + "datap.setItem(0, "+percchartnum+", ("+perc+"));\r\n";
	  				perchartend = perchartend +  "chart2.getPoints().getItem("+percchartnum+").setFillMode(cfx.FillMode.Gradient);\r\n";
				    percchartnum++;
				}
				perchart = perchart + perchartend;
		 
				  }
                  return table;
	 }
	 
	 
	public static class Login {
			public String email;
			public String password;
			
			public String validate() {
				if (User.authenticate(email, password) == null) {
				  return "Invalid user or password!";
				}
				return null;
			}
	}
	


}



