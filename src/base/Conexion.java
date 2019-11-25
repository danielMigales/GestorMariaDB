package base;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Conexion {
//propiedades de la clase

    private static Connection conn;
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String user = "root";
    private static final String password = "";
    private static final String url = "jdbc:mysql://localhost:3306/";

    public Conexion() {//constructor vacio
        conn = null;
        try {
            Scanner teclado = new Scanner(System.in);
            System.out.println("¿Desea crear una nueva base de datos?  SI/NO");
            String respuesta = teclado.nextLine();
            switch (respuesta) {

                case "SI":
                    String bd = teclado.nextLine();
                    Class.forName(driver);
                    conn = (Connection) DriverManager.getConnection(url, user, password);
                    PreparedStatement ps = (PreparedStatement) conn.prepareStatement("CREATE DATABASE " + bd);
                    ps.executeUpdate();
                    ps.close();
                    break;

                case "NO":
                    System.out.println("Introduzca el nombre de la base de datos a la que conectarse.");
                    bd = teclado.nextLine();
                    Class.forName(driver);
                    conn = (Connection) DriverManager.getConnection(url + bd, user, password);
                    break;
            }

            if (conn != null) {
                System.out.println("Conectado a la base de datos");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error al conectar" + e);

        }
    }

    public void desconectar() {
        conn = null;
    }
}
