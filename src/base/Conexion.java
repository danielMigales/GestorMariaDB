package base;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Conexion {
//propiedades de la clase

    private static Connection conexion;
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String user = "root";
    private static final String password = "";
    private static final String url = "jdbc:mysql://localhost:3306/";

    public Conexion() {//constructor vacio
        conexion = null;
        try {
            Scanner teclado = new Scanner(System.in);
            System.out.println("¿Desea crear una nueva base de datos?.....SI/NO");
            String respuesta = teclado.nextLine().toUpperCase();
            switch (respuesta) {

                case "SI":
                    System.out.println("Asigne un nombre a la Base de datos...");
                    String bd = teclado.nextLine();
                    Class.forName(driver);
                    conexion = (Connection) DriverManager.getConnection(url, user, password);
                    PreparedStatement ps = (PreparedStatement) conexion.prepareStatement("CREATE DATABASE " + bd);
                    ps.executeUpdate();
                    ps.close();
                    System.out.println("Base de datos " + bd + " creada.");
                    break;

                case "NO":
                    System.out.println("Introduzca el nombre de la base de datos a la que conectarse.");
                    bd = teclado.nextLine();
                    Class.forName(driver);
                    conexion = (Connection) DriverManager.getConnection(url + bd, user, password);
                    break;
            }

            if (conexion != null) {
                System.out.println("Conectado a la base de datos.");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Error al conectar" + ex);

        }
    }

    public void desconectar() {
        conexion = null;
    }

    public void crearTabla() throws SQLException {

        String nombreTabla;
        boolean salir = false;

        do {
            Scanner teclado = new Scanner(System.in);
            System.out.println("¿Desea crear una nueva tabla?.....SI/NO");
            String respuesta = teclado.nextLine().toUpperCase();
            switch (respuesta) {
                case "SI":
                    System.out.println("Introduzca el nombre de la tabla a crear.");
                    nombreTabla = teclado.next();
                    Statement st = null;

                    try {
                        st = conexion.createStatement();
                        System.out.println("Es necesario borrar la tabla antes de crearla.");
                        eliminarTabla(nombreTabla);

                        String sql = "CREATE TABLE " + nombreTabla + "(id SERIAL PRIMARY KEY)";

                        st.executeUpdate(sql);
                        System.out.println("Creada tabla " + nombreTabla + ".");
                    } finally {

                        if (st != null) {
                            st.close();
                        }

                    }
                    break;
                case "NO":
                    salir = true;
            }

        } while (!salir);
    }

    public void eliminarTabla(String nombreTabla) throws SQLException {

        try (Statement st = conexion.createStatement()) {

            String sql = "DROP TABLE IF EXISTS " + nombreTabla + " ";
            st.executeUpdate(sql);
            System.out.println("Eliminada tabla " + nombreTabla);
        }
    }

}
