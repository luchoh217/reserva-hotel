import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

public class SistemaReservaHotel {

    private static final String[] TIPOS_HABITACION = {"Suite", "Doble", "Individual", "Presidencial"};
    private static final double[] PRECIOS_HABITACION = {280000, 180000, 120000, 450000};
    private static final int[] CAPACIDAD_HABITACION = {2, 2, 1, 4};
    private static final ArrayList<Reserva> reservas = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    static class Reserva {
        String huespedNombre;
        String numeroHabitacion;
        String tipoHabitacion;
        LocalDate fechaEntrada;
        LocalDate fechaSalida;
        int numeroNoches;
        int numeroPersonas;
        double precioNoche;
        double precioTotal;

        @Override
        public String toString() {
            return String.format(
                    "Huésped: %s%n" +
                    "Habitación: %s | Tipo: %s%n" +
                    "Fechas: %s a %s | Noches: %d%n" +
                    "Personas: %d | Precio/Noche: $%.0f%n" +
                    "PRECIO TOTAL: $%.0f%n",
                    huespedNombre, numeroHabitacion, tipoHabitacion,
                    fechaEntrada, fechaSalida, numeroNoches, numeroPersonas,
                    precioNoche, precioTotal
            );
        }
    }

    public static void main(String[] args) {
        boolean ejecutando = true;

        while (ejecutando) {
            mostrarMenu();
            int opcion = leerEntero(1, 4);

            switch (opcion) {
                case 1:
                    crearReserva();
                    break;
                case 2:
                    consultarReservas();
                    break;
                case 3:
                    mostrarTarifas();
                    break;
                case 4:
                    System.out.println("Gracias por usar HOTEL GRAN COLOMBIA.");
                    ejecutando = false;
                    break;
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n=================================");
        System.out.println("      HOTEL GRAN COLOMBIA");
        System.out.println("      SISTEMA DE RESERVAS");
        System.out.println("=================================");
        System.out.println("1. Crear nueva reserva");
        System.out.println("2. Consultar reservas");
        System.out.println("3. Ver tarifas");
        System.out.println("4. Salir");
        System.out.println("=================================");
        System.out.print("Seleccione una opción: ");
    }

    private static void crearReserva() {
        Reserva reserva = new Reserva();

        System.out.print("Nombre del huésped: ");
        reserva.huespedNombre = scanner.nextLine().trim();

        System.out.print("Número de personas: ");
        reserva.numeroPersonas = leerEntero(1, 10);

        System.out.println("\nTipos de habitación:");
        for (int i = 0; i < TIPOS_HABITACION.length; i++) {
            System.out.printf("%d. %s - $%.0f / noche (capacidad %d)%n",
                    i + 1, TIPOS_HABITACION[i], PRECIOS_HABITACION[i], CAPACIDAD_HABITACION[i]);
        }

        System.out.print("Seleccione un tipo de habitación: ");
        int tipo = leerEntero(1, TIPOS_HABITACION.length);
        reserva.tipoHabitacion = TIPOS_HABITACION[tipo - 1];
        reserva.precioNoche = PRECIOS_HABITACION[tipo - 1];

        if (reserva.numeroPersonas > CAPACIDAD_HABITACION[tipo - 1]) {
            System.out.println("La habitación seleccionada no tiene capacidad suficiente.");
            return;
        }

        System.out.print("Número de habitación: ");
        reserva.numeroHabitacion = scanner.nextLine().trim();

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        while (true) {
            System.out.print("Fecha de entrada (dd/MM/yyyy): ");
            String entrada = scanner.nextLine();
            try {
                reserva.fechaEntrada = LocalDate.parse(entrada, formato);
                break;
            } catch (Exception e) {
                System.out.println("Fecha inválida. Intente nuevamente.");
            }
        }

        while (true) {
            System.out.print("Fecha de salida (dd/MM/yyyy): ");
            String salida = scanner.nextLine();
            try {
                reserva.fechaSalida = LocalDate.parse(salida, formato);
                break;
            } catch (Exception e) {
                System.out.println("Fecha inválida. Intente nuevamente.");
            }
        }

        if (!reserva.fechaSalida.isAfter(reserva.fechaEntrada)) {
            System.out.println("La fecha de salida debe ser posterior a la fecha de entrada.");
            return;
        }

        if (reserva.fechaEntrada.isBefore(LocalDate.now())) {
            System.out.println("La fecha de entrada no puede ser anterior a hoy.");
            return;
        }

        reserva.numeroNoches = (int) ChronoUnit.DAYS.between(reserva.fechaEntrada, reserva.fechaSalida);
        reserva.precioTotal = reserva.precioNoche * reserva.numeroNoches;

        System.out.println("\nResumen de la reserva:");
        System.out.println(reserva);

        System.out.print("¿Confirmar reserva? (s/n): ");
        String opcion = scanner.nextLine().trim().toLowerCase();

        if (opcion.equals("s")) {
            reservas.add(reserva);
            System.out.println("✅ Reserva registrada correctamente.");
        } else {
            System.out.println("❌ Reserva cancelada.");
        }
    }

    private static void consultarReservas() {
        if (reservas.isEmpty()) {
            System.out.println("No hay reservas registradas.");
            return;
        }

        System.out.println("\nReservas actuales:");
        for (int i = 0; i < reservas.size(); i++) {
            System.out.println("\n----- Reserva #" + (i + 1) + " -----");
            System.out.println(reservas.get(i));
        }

        double total = 0;
        for (Reserva r : reservas) {
            total += r.precioTotal;
        }

        System.out.printf("Total de ingresos acumulados: $%.0f%n", total);
    }

    private static void mostrarTarifas() {
        System.out.println("\nTarifas disponibles:");
        for (int i = 0; i < TIPOS_HABITACION.length; i++) {
            System.out.printf("%s: $%.0f / noche | capacidad %d personas%n",
                    TIPOS_HABITACION[i], PRECIOS_HABITACION[i], CAPACIDAD_HABITACION[i]);
        }
    }

    private static int leerEntero(int min, int max) {
        while (true) {
            try {
                String entrada = scanner.nextLine().trim();
                int valor = Integer.parseInt(entrada);
                if (valor >= min && valor <= max) {
                    return valor;
                }
                System.out.printf("Ingrese un número entre %d y %d: ", min, max);
            } catch (NumberFormatException e) {
                System.out.printf("Entrada inválida. Ingrese un número entre %d y %d: ", min, max);
            }
        }
    }
}
