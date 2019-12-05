package base;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Conection {
//propiedades de la clase

    private static Connection conection;
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String user = "root";
    private static final String password = "";
    private static final String url = "jdbc:mysql://localhost:3306/";

    ArrayList<String> listaCampos = new ArrayList<String>();
    ArrayList<String> tipoCampos = new ArrayList<String>();
    ArrayList<String> valorCampos = new ArrayList<String>();

    public Conection() {//constructor vacio
        conection = null;
        try {
            Scanner teclado = new Scanner(System.in);
            System.out.println("¿Desea crear una nueva base de datos?.....SI/NO");
            String respuesta = teclado.nextLine().toUpperCase();
            switch (respuesta) {

                case "SI":
                    System.out.println("Asigne un nombre a la Base de datos...");
                    String bd = teclado.nextLine();
                    Class.forName(driver);
                    conection = (Connection) DriverManager.getConnection(url, user, password);
                    PreparedStatement ps = (PreparedStatement) conection.prepareStatement("CREATE DATABASE " + bd);
                    ps.executeUpdate();
                    ps.close();
                    System.out.println("Base de datos " + bd + " creada.");
                    conection = (Connection) DriverManager.getConnection(url + bd, user, password);
                    break;

                case "NO":
                    System.out.println("Introduzca el nombre de la base de datos a la que conectarse.");
                    bd = teclado.nextLine();
                    Class.forName(driver);
                    conection = (Connection) DriverManager.getConnection(url + bd, user, password);
                    break;
            }

            if (conection != null) {
                System.out.println("Conectado a la base de datos.");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Error al conectar" + ex);

        }
    }

    public void desconectar() {
        conection = null;
    }

    public void crearTabla() throws SQLException {

        boolean salir = true;

        do {
            Scanner teclado = new Scanner(System.in);
            System.out.println("¿Desea crear una nueva tabla?.....SI/NO");
            String respuesta = teclado.nextLine().toUpperCase();
            switch (respuesta) {
                case "SI":
                    System.out.println("Introduzca el nombre de la tabla a crear.");
                    String nombreTabla = teclado.nextLine();
                    Statement st = null;

                    try {
                        st = conection.createStatement();
                        System.out.println("Es necesario borrar la tabla antes de crearla.");
                        eliminarTabla(nombreTabla);

                        String sql = "CREATE TABLE " + nombreTabla + "(id SERIAL PRIMARY KEY);";
                        st.executeUpdate(sql);

                        System.out.println("Introduzca el numero de campos de la tabla...");
                        int numeroCampos = teclado.nextInt();

                        for (int i = 0; i < numeroCampos; i++) {
                            System.out.println("Introduzca el nombre del campo.");
                            teclado.nextLine();
                            String nombreCampo = teclado.nextLine();
                            listaCampos.add(nombreCampo);
                            System.out.println("¿Que tipo de campo desea añadir? Texto: 1 o Numerico: 2");
                            int tipo = teclado.nextInt();
                            if (tipo == 1) {
                                tipoCampos.add("VARCHAR (20)");
                            } else {
                                tipoCampos.add("INT");
                            }
                            String sqlAlter = "ALTER TABLE " + nombreTabla + " ADD COLUMN "
                                    + listaCampos.get(i) + " " + tipoCampos.get(i) + ";";
                            System.out.println(sqlAlter);
                            st.executeUpdate(sqlAlter);
                        }
                        System.out.println("Creada tabla " + nombreTabla + ".");

                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());

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

    public void añadirRegistro() throws SQLException {

        Scanner teclado = new Scanner(System.in);
        System.out.println("Introduzca el nombre de la base de datos en la cual desea insertar valores.");
        String database = teclado.nextLine();
        System.out.println("Introduzca el nombre de la tabla en la cual desea insertar valores.");
        String nombreTabla = teclado.nextLine();
        getCampos(database, nombreTabla);
        boolean camposOK = false;
        do {
            Statement st = null;
            String sql = "SHOW COLUMNS FROM " + database + "." + nombreTabla + ";";

            try {
                st = conection.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {
                    listaCampos.add(rs.getString(1));
                }
            } finally {
                if (st != null) {
                    st.close();
                }
            }
            String datosArray = "";
            for (String elemento : listaCampos) {
                datosArray += elemento + ", ";
            }
            System.out.println(quitarComa(datosArray));

            System.out.println("Introduzca los valores del campo:");
            String valor = teclado.nextLine();
            valorCampos.add(valor);

        } while (camposOK);

        Statement st = null;

        try {
            st = conection.createStatement();
            String sql = "INSERT INTO " + nombreTabla
                    + "(" + listaCampos.toString() + ") values (" + valorCampos.toString() + ");";
            st.executeUpdate(sql);
            System.out.println("Registro añadido.");
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public void getTabla() throws SQLException {

        System.out.println("¿Que base de datos desea consultar?");
        Scanner teclado = new Scanner(System.in);
        String nombreBD = teclado.nextLine();

        Statement st = null;
        String sql = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = '" + nombreBD + "'";

        try {
            st = conection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            System.out.println("\nLas tablas en la base de datos son las siguientes: \n");
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public void getCampos(String database, String nombreTabla) throws SQLException {

        Statement st = null;
        String sql = "SHOW COLUMNS FROM " + database + "." + nombreTabla + ";";

        try {
            st = conection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            System.out.println("\nLos campos son las siguientes: \n");
            while (rs.next()) {
                System.out.println(rs.getString(1) + ":" + rs.getString(2));
            }
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public void eliminarTabla(String nombreTabla) throws SQLException {

        try (Statement st = conection.createStatement()) {
            String sql = "DROP TABLE IF EXISTS " + nombreTabla + " ";
            st.executeUpdate(sql);
            System.out.println("Eliminada tabla " + nombreTabla);
        }
    }

    public void consultaDatos() throws SQLException {

        Scanner teclado = new Scanner(System.in);
        System.out.println("Introduzca el nombre de la tabla a consultar.");
        String nombreTabla = teclado.nextLine();

        String sql = "SELECT * FROM " + nombreTabla;
        Statement st = null;
        try {
            st = conection.createStatement();
            ResultSet rs = st.executeQuery(sql);

            int nSuperGuerreros = 0;
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");
                int poder = rs.getInt("poder");
                System.out.println("ID: " + id + " Nombre: " + nombre
                        + " Descripcion: " + descripcion + " Poder: " + poder);
                nSuperGuerreros++;
            }
            if (nSuperGuerreros == 0) {
                System.out.println("No hay superguerreros");
            }
            rs.close();
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public String quitarComa(String datosArray) {

        datosArray = datosArray.trim();
        if (datosArray != null && datosArray.length() > 0 && datosArray.charAt(datosArray.length() - 1) == ',') {
            datosArray = datosArray.substring(0, datosArray.length() - 1);
        }
        return datosArray;
    }

}
