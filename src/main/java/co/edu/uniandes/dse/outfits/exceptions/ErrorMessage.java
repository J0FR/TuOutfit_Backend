package co.edu.uniandes.dse.outfits.exceptions;

public final class ErrorMessage {
    public static final String UBICACION_NOT_FOUND = "La ubicacion no existe";
    
    private ErrorMessage() {
		throw new IllegalStateException("Utility class");
	}
}
