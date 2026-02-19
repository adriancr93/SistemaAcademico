package org.example.view;

import java.util.Scanner;

/**
 * Utilidad para entrada de datos por consola
 */
public class InputHelper {
    
    private static final Scanner scanner = new Scanner(System.in);
    
    /**
     * Leer una línea de texto
     */
    public static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    /**
     * Leer una línea de texto con valor por defecto
     */
    public static String readLine(String prompt, String defaultValue) {
        System.out.print(prompt + " [" + defaultValue + "]: ");
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? defaultValue : input;
    }
    
    /**
     * Leer un número entero
     */
    public static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Por favor ingrese un número válido.");
            }
        }
    }
    
    /**
     * Leer un número entero con valor por defecto
     */
    public static int readInt(String prompt, int defaultValue) {
        while (true) {
            try {
                System.out.print(prompt + " [" + defaultValue + "]: ");
                String input = scanner.nextLine().trim();
                return input.isEmpty() ? defaultValue : Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Por favor ingrese un número válido.");
            }
        }
    }
    
    /**
     * Confirmar una acción (s/N)
     */
    public static boolean confirm(String prompt) {
        System.out.print(prompt + " (s/N): ");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.startsWith("s");
    }
    
    /**
     * Pausar hasta que se presione Enter
     */
    public static void pause() {
        pause("Presione Enter para continuar...");
    }
    
    /**
     * Pausar con mensaje personalizado
     */
    public static void pause(String message) {
        System.out.print(message);
        scanner.nextLine();
    }
    
    /**
     * Limpiar pantalla (simulado con líneas en blanco)
     */
    public static void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
    
    /**
     * Mostrar un separador visual
     */
    public static void showSeparator() {
        System.out.println("═".repeat(80));
    }
    
    /**
     * Mostrar un separador con texto
     */
    public static void showSeparator(String text) {
        int totalLength = 80;
        int textLength = text.length();
        int padding = (totalLength - textLength - 2) / 2;
        
        System.out.println("═".repeat(padding) + " " + text + " " + "═".repeat(padding));
    }
}