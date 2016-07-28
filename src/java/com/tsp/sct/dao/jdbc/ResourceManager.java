package com.tsp.sct.dao.jdbc;


import com.tsp.sct.config.Configuration;
import java.sql.*;

public class ResourceManager
{
    private static String JDBC_DRIVER   = "com.mysql.jdbc.Driver";
    /*
    private static String JDBC_URL      = "jdbc:mysql://localhost/mysql";

    private static String JDBC_USER     = "root";
    private static String JDBC_PASSWORD = "root";
    */
    private static Driver driver = null;

    public static synchronized Connection getConnection()
	throws SQLException
    {
        if (driver == null)
        {
            try
            {
                Class jdbcDriverClass = Class.forName( JDBC_DRIVER );
                driver = (Driver) jdbcDriverClass.newInstance();
                DriverManager.registerDriver( driver );
            }
            catch (Exception e)
            {
                System.out.println( "Failed to initialise JDBC driver" );
                e.printStackTrace();
            }
        }

        /*
        return DriverManager.getConnection(
                JDBC_URL,
                JDBC_USER,
                JDBC_PASSWORD
        );
         */
        
        Configuration appConfig = new Configuration();
        return DriverManager.getConnection(
                appConfig.getJdbc_url(),
                appConfig.getJdbc_user(),
                appConfig.getJdbc_password()
        );
    }


	public static void close(Connection conn)
	{
		try {
			if (conn != null) conn.close();
		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
		}
	}

	public static void close(PreparedStatement stmt)
	{
		try {
			if (stmt != null) stmt.close();
		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
		}
	}

	public static void close(ResultSet rs)
	{
		try {
			if (rs != null) rs.close();
		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
		}

	}

}
