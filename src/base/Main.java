package base;

import java.util.Scanner;

/**
 * @Daniel Migales
 */
public class Main {

    public static void main(String[] args) {

        Conection ua = null;
        Scanner teclado = new Scanner(System.in);
        boolean salir = true;

        try {

            ua = new Conection();
            do {
                System.out.println("\n----------------MENU PRINCIPAL-------------\n");
                System.out.println("0. Salir del programa.");
                System.out.println("1. Crear tabla.");
                System.out.println("2. Ver campos de una tabla.");
                System.out.println("3. Insertar datos en la tabla.");
                System.out.println("4. Editar datos.");
                System.out.println("5. Consultar datos de la tabla.");
                System.out.println("6. Borrar un registro de la tabla.");
                System.out.println("7. Borrar la tabla.");
                System.out.println("8. Mostrar las tablas de la base de datos.");

                System.out.println("\n" + "Elija una opcion.");
                int opcion = teclado.nextInt();

                switch (opcion) {

                    case 0:
                        salir = false;
                    case 1:
                        ua.crearTabla();
                        break;
                    case 2:
                        System.out.println("Introduzca el nombre de la base de datos.");
                        String database = teclado.next();
                        System.out.println("Introduzca el nombre de la tabla.");
                        String nombreTabla = teclado.next();
                        ua.getCampos(database, nombreTabla);
                        break;
                    case 3:
                        ua.añadirRegistro();
                        break;
                    case 4:
                        ua.editarTabla();
                        break;
                    case 5:
                        ua.consultaDatos();
                        break;
                    case 6:
                        ua.borrarRegistro();
                        break;
                    case 7:
                        System.out.println("Inserte el nombre de la tabla a eliminar:");
                        nombreTabla = teclado.nextLine();
                        ua.eliminarTabla(nombreTabla);
                        break;
                    case 8:
                        ua.getTabla();
                        break;
                }
            } while (salir);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ua != null) {
                ua.desconectar();
            }
        }
    }
}
