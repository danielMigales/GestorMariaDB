package base;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Conexion {
//propiedades de la clase

    private static Connection conexion;
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String user = "root";
    private static final String password = "";
    private static final String url = "jdbc:mysql://localhost:3306/";

    ArrayList<String> listaCampos = new ArrayList<String>();
    ArrayList<String> tipoCampos = new ArrayList<String>();

    Scanner tecladoStrings = new Scanner(System.in);
    Scanner tecladoNumeros = new Scanner(System.in);

    public Conexion() {//constructor vacio
        conexion = null;
        try {

            System.out.println("¿Desea crear una nueva base de datos?.....SI/NO");
            String respuesta = tecladoStrings.nextLine().toUpperCase();
            switch (respuesta) {

                case "SI":
                    System.out.println("Asigne un nombre a la Base de datos...");
                    String bd = tecladoStrings.nextLine();
                    Class.forName(driver);
                    conexion = (Connection) DriverManager.getConnection(url, user, password);
                    PreparedStatement ps = (PreparedStatement) conexion.prepareStatement("CREATE DATABASE " + bd);
                    ps.executeUpdate();
                    ps.close();
                    System.out.println("Base de datos " + bd + " creada.");
                    break;

                case "NO":
                    System.out.println("Introduzca el nombre de la base de datos a la que conectarse.");
                    bd = tecladoStrings.nextLine();
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
            System.out.println("¿Desea crear una nueva tabla?.....SI/NO");
            String respuesta = tecladoStrings.nextLine().toUpperCase();
            switch (respuesta) {
                case "SI":
                    System.out.println("Introduzca el nombre de la tabla a crear.");
                    nombreTabla = tecladoStrings.nextLine();
                    Statement st = null;

                    try {
                        st = conexion.createStatement();
                        System.out.println("Es necesario borrar la tabla antes de crearla.");
                        eliminarTabla(nombreTabla);

                        String sql = "CREATE TABLE " + nombreTabla + "(id SERIAL PRIMARY KEY)";
                        st.executeUpdate(sql);

                        System.out.println("Introduzca el numero de campos de la tabla...");
                        int numeroCampos = tecladoNumeros.nextInt();

                        for (int i = 0; i < numeroCampos; i++) {
                            int contador = 1;
                            System.out.println("Introduzca el nombre del campo " + contador);
                            String nombreCampo = tecladoStrings.nextLine();

                            listaCampos.add(nombreCampo);
                            System.out.println("¿Que tipo de campo desea añadir? Texto: 1 o Numerico: 2");
                            int tipo = tecladoNumeros.nextInt();
                            if (tipo == 1) {
                                tipoCampos.add("VARCHAR (20)");
                            } else {
                                tipoCampos.add("INT");
                            }
                            contador++;
                        }
                        for (int i = 0; i < numeroCampos + 1; i++) {
                            String sqlAlter = "ALTER TABLE " + nombreTabla + " ADD COLUMN "
                                    + listaCampos.get(i) + " " + tipoCampos.get(i) + ";";
                            st.executeUpdate(sqlAlter);
                        }
                        System.out.println("Creada tabla " + nombreTabla + ".");

                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                        
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
