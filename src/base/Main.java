package base;

import java.util.Scanner;

/**
 * @Daniel Migales
 */
public class Main {

    public static void main(String[] args) {

        Conexion conectar = null;
        Scanner teclado = new Scanner(System.in);
        boolean salir = true;

        try {

            conectar = new Conexion();
            do {
                System.out.println("\n----------------MENU PRINCIPAL-------------\n");
                System.out.println("0. Salir del programa.");
                System.out.println("1. Crear tabla.");
                System.out.println("2. Insertar datos en la tabla.");
                System.out.println("3. Editar datos.");
                System.out.println("4. Consultar datos de la tabla.");
                System.out.println("5. Borrar un registro de la tabla.");
                System.out.println("6. Borrar la tabla.");
                System.out.println("7. Mostrar las tablas de la base de datos.");

                System.out.println("\n" + "Elija una opcion.");
                int opcion = teclado.nextInt();

                switch (opcion) {

                    case 0:
                        salir = false;
                    case 1:
                        conectar.crearTabla();
                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:

                        break;
                    case 5:

                        break;
                    case 6:

                        break;
                    case 7:

                        break;
                }
            } while (salir);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conectar != null) {
                conectar.desconectar();
            }
        }
    }
}
