package models;

import java.util.*;
import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;
import play.api.db.DB;
import java.sql.*;

/**
 * User entity managed by Mysql
 */

public class User extends Model {

    @Id
    @Constraints.Required
    @Formats.NonEmpty
    public String email;
    
    @Constraints.Required
    public String password;
    
    @Constraints.Required
    public String registration_date;
    
    public int ID;
    
    public User() {
		email = "";
		password = "";
		registration_date = "";
		ID = 0;
	}
    
    
    // -- Queries
    
    //public static Model.Finder<String,User> find = new Model.Finder(String.class, User.class);
    
    /**
     * Retrieve all users.
     */
    public static List<User> findAll() {
		Connection conn = play.db.DB.getConnection();
		User tmp = new User();
		List<User> listed = new ArrayList<User>();
		try {
				ResultSet rs;
				Statement stat = conn.createStatement();
				rs = stat.executeQuery("SELECT * FROM account");
				while (rs.next()) {
					tmp.ID = rs.getInt("ID");
					tmp.email = rs.getString("email");
					tmp.password = rs.getString("password");
					tmp.registration_date = rs.getString("registration_date");
					//User tmp_ = tmp;
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
					return listed;
				}
    }

    /**
     * Retrieve a User from email.
     */
    public static User findByEmail(String email) {
        Connection conn = play.db.DB.getConnection();
		User tmp = new User();
		try {
				ResultSet rs;
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM account WHERE email = ?");
				stmt.setString(1, email);
				rs = stmt.executeQuery();
				if (rs==null)
				{	
					conn.close();
					return null;
				}
				else{
					while (rs.next()) {
						tmp.ID = rs.getInt("ID");
						tmp.email = rs.getString("email");
						tmp.password = rs.getString("password");
						tmp.registration_date = rs.getString("registration_date");
					}
				}
			    conn.close();
				}
			catch (SQLException e)
				{
					e.printStackTrace();
				}
			finally 
				{
					return tmp;
				}
    
    }
    
    /**
     * Authenticate a User.
     */
    public static User authenticate(String email, String password) {
		Connection conn = play.db.DB.getConnection();
		User tmp = new User();
		boolean empty = false;
		try {
				ResultSet rs;
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM account WHERE email = ? AND password = ?");
				stmt.setString(1, email);
				stmt.setString(2, password);
				rs = stmt.executeQuery();
				if (!rs.next())
				{	
					conn.close();
					empty = true;
				}
				else{
					while (rs.next()) {
						tmp.ID = rs.getInt("ID");
						tmp.email = rs.getString("email");
						tmp.password = rs.getString("password");
						tmp.registration_date = rs.getString("registration_date");
					}
				}
			    conn.close();
				}
			catch (SQLException e)
				{
					e.printStackTrace();
				}
			finally 
				{
				if (!empty)
					return tmp;
				else
					return null;
				}
    }
    
    // --
    
    public String toString() {
        return "User(" + email + ")";
    }

}

