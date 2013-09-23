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

public class Application extends Controller {
  
	public static String Jstable;
	public static String Imgc;
	public static String percentages;
	
    public static Result index() {
		return redirect(routes.Application.login());
        //return ok(index.render("API is ready"));
    }
    
    public static Result login() {
		return ok(login.render(form(Login.class)));
	}
    
    public static Result logout() {
		
		session().clear();
		flash("success", "You've been logged out");
		return redirect(
			routes.Application.login()
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
				routes.Application.Votes()
			);
		}
	}

    public static Result postVote(String vote) {
		
		String remote = request().remoteAddress();
		if (remote.contains(":"))
			remote = "127.0.0.1";
		Connection conn = play.db.DB.getConnection();
		String timeStamp = new SimpleDateFormat("HH:mm:ss yyyy/MM/dd").format(Calendar.getInstance().getTime());
		if (vote.matches("\\d+"))
		{
		int voteclean = Integer.parseInt(vote);
			if (voteclean <= 10) 
			{
			try {
				Statement stat = conn.createStatement();
				stat.executeUpdate("INSERT INTO Votes VALUES (DEFAULT, "+voteclean+", '"+remote+"', '"+timeStamp+"')");
			    conn.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
				
			} finally {
						return ok("! DEBUG ! => Your vote is " + vote + "! Your IP is " + remote);
					  }
			}
			else
			{
				//400: Bad request
			return badRequest("Vote must be between 1 and 10");
			}
		}
		else
		{
			return badRequest("Invalid vote!");
		}
		
}

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
	public static Result Votes() {
		String[] row = new String[4];
		
		List<String[]> listed = new ArrayList<String[]>();
		Connection conn = play.db.DB.getConnection();
		String tablecontent;
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
					return ok(Votes.render(tablecontent, Jstable, percentages));

				}
  
}

	public static String tabber(List<String[]> listed) {
                  int num = 0;
                  int numrow = 0;
                  int vote = 0;
                  String ipadd = "";
                  String table = "";
                  String result = "";
                  int[] percentvoti = new int[10];
                  percentages = "";
                  String time = "";
                  Jstable = "";
                  String countryname = "";
                  Imgc = "";
                  SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss yyyy/MM/dd");//("yyyy-MM-dd'T'HH:mm:ss':10.280Z'");
				  DateFormat df2 = new SimpleDateFormat("yyy-MM-dd'T'HH:mm':10.280Z'");
				  Date date = new Date();
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
					    if (vote >= 7)
							result="success";
						else if (vote < 7 && vote >= 4)
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
							jsdate = df2.format(date);
							Jstable = Jstable + "{ \"Date\": \""+jsdate+"\", \"Vote\": "+vote+" }";
							
							
							
						int caching2 = IP2Country.MEMORY_MAPPED;
						try{
						IP2Country ip2c = new IP2Country("ip2c/ip-to-country.bin",caching2);
						Country c = ip2c.getCountry(ipadd);
						countryname = c.getName();
							if (countryname == "")
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
							
						table = table + "<div id=\"del"+numrow+"\" style=\"display: block\"><tr id=\"del"+numrow+"\" class=\""+result+"\"><td>"+num+"</td><td><b>"+vote+"</b></td><td>"+ipadd+"<img src=\"/assets/images/"+Imgc+".png\" title=\""+countryname+"\" align=\"right\"></img></td><td>"+time+"<img class=\"delete\" src=\"/assets/delete.png\" onclick=\"deleteVote("+num+",this,"+numrow+");\" align=\"right\"></img></td></tr></div>";
						numrow++;
						}
					}
					
					
				}
				
			}
                  int mean = (int)Math.round((((double) sum) / listed.size()));
  				table = table + "<tr class=\"warning\"><td><p id=\"meanp\"><b>Mean</b></p></td><td><p id=\"meanp2\">"+mean+"</p></td><td>-</td><td>-</td></tr>";
				int size = listed.size();
  				for (int i = 10; i > 0; i--)
				{
					float perc = 0;
					perc = (percentvoti[i-1]*size);//100;
	  				percentages = percentages + "<tr><td>"+i+"</td><td><b>"+perc+" %</b></td></tr>"; 
				}
		 
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



