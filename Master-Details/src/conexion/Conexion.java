package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    Connection con;
    String ur = "jdbc:mysql://localhost/sri?useSSL=false&serverTimezone=UTC";
    String uss="root";
    String pas="P@tito2004$";
    public Connection getConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(ur,uss,pas);
            System.out.println("Conexion exitosa");
           // con.close();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error al conectarse a la BD "+e);
        }
        return con;
    }
}

