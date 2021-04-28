
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DemoServlet
 */
@WebServlet("/DmoWebApplication")
public class DemoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * Default constructor. 
	 */
	public DemoServlet() {
		// TODO Auto-generated constructor stub
	}
	protected void doPost(HttpServletRequest request, 
			HttpServletResponse response)
					throws ServletException, IOException
	{
		int count = 0;
		try {
			PreparedStatement st = null;
			// Initialize the database
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/task","root","root");
			String data = request.getParameter("value");
			Statement stmt = con.createStatement();
			String sql;
			sql = "SELECT * FROM palindrome";
			ResultSet rs = stmt.executeQuery(sql);
			//checking for duplicates
			while(rs.next()) {
				if(rs.getString(1).toLowerCase() == data.toLowerCase()) {
					count++;
				}
			}
			//other conditions for validation
			if(count == 0 && validate(data) == true && data != "") {
				st = con.prepareStatement("insert into palindrome values(?)");
				st.setString(1, request.getParameter("value"));
				st.executeUpdate();
				PrintWriter out = response.getWriter();
				out.println("<html><body><b>Successfully Inserted" + "</b></body></html>");
				st.close();
				con.close();
			}else {
				PrintWriter out = response.getWriter();
				out.println("<html><body><b>Duplicate data" + "</b></body></html>");
				System.out.println("false");
			}
			// Close all the connections
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * validate() checks if the data is palindrome, length doesnt exceed 100 
	 * and contains only alphabets
	 */
	public boolean validate(String data) {
		if(data.matches("^[a-zA-Z]*$") && data.length() <= 100 && isPalindrome(data) == true ){
			return true;
		}else {
			return false;
		}
	}
	public boolean isPalindrome(String str)
	{
		int i = 0, j = str.length() - 1;
		while (i < j) {
			if (str.charAt(i) != str.charAt(j))
				return false;
			i++;
			j--;
		}
		return true;
	}
}