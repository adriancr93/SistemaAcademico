package org.example.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * Utilidades para validación de datos
 */
public class ValidationUtil {
    
    // Patrón para validar email
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    
    // Patrón para validar identificación (solo números)
    private static final Pattern ID_PATTERN = 
        Pattern.compile("^[0-9]{9,12}$");
    
    // Estados válidos
    private static final String[] ESTADOS_VALIDOS = {"activo", "inactivo"};
    
    /**
     * Valida si una cadena no está vacía
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
    
    /**
     * Valida formato de email
     */
    public static boolean isValidEmail(String email) {
        if (!isNotEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * Valida número de identificación
     */
    public static boolean isValidIdentification(String identification) {
        if (!isNotEmpty(identification)) {
            return false;
        }
        return ID_PATTERN.matcher(identification.trim()).matches();
    }
    
    /**
     * Valida estado (activo/inactivo)
     */
    public static boolean isValidState(String estado) {
        if (!isNotEmpty(estado)) {
            return false;
        }
        String estadoLower = estado.trim().toLowerCase();
        for (String estadoValido : ESTADOS_VALIDOS) {
            if (estadoValido.equals(estadoLower)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Valida y parsea una fecha
     */
    public static LocalDate parseDate(String dateString) {
        if (!isNotEmpty(dateString)) {
            throw new IllegalArgumentException("La fecha no puede estar vacía");
        }
        
        try {
            // Intenta varios formatos de fecha
            DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy")
            };
            
            for (DateTimeFormatter formatter : formatters) {
                try {
                    return LocalDate.parse(dateString.trim(), formatter);
                } catch (DateTimeParseException ignored) {
                    // Continúa con el siguiente formato
                }
            }
            
            throw new IllegalArgumentException("Formato de fecha inválido. Use dd/MM/yyyy, yyyy-MM-dd o dd-MM-yyyy");
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al procesar la fecha: " + e.getMessage());
        }
    }
    
    /**
     * Valida que una fecha sea válida (no futura para fecha de nacimiento)
     */
    public static boolean isValidBirthDate(LocalDate date) {
        if (date == null) {
            return false;
        }
        
        LocalDate now = LocalDate.now();
        LocalDate minDate = now.minusYears(100); // Máximo 100 años atrás
        
        return !date.isAfter(now) && !date.isBefore(minDate);
    }
    
    /**
     * Normaliza el estado a lowercase
     */
    public static String normalizeState(String estado) {
        if (!isNotEmpty(estado)) {
            return "activo";
        }
        return estado.trim().toLowerCase();
    }
    
    /**
     * Limpia y normaliza texto
     */
    public static String cleanText(String text) {
        if (text == null) {
            return null;
        }
        return text.trim();
    }
    
    /**
     * Valida longitud mínima de texto
     */
    public static boolean isValidLength(String text, int minLength) {
        return isNotEmpty(text) && text.trim().length() >= minLength;
    }
    
    /**
     * Mensaje de error para campo vacío
     */
    public static String getEmptyFieldMessage(String fieldName) {
        return "El campo '" + fieldName + "' no puede estar vacío";
    }
    
    /**
     * Mensaje de error para formato inválido
     */
    public static String getInvalidFormatMessage(String fieldName) {
        return "El formato del campo '" + fieldName + "' es inválido";
    }
}