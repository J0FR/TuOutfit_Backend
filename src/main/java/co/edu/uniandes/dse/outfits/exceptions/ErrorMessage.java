package co.edu.uniandes.dse.outfits.exceptions;

public final class ErrorMessage {
  public static final String UBICACION_NOT_FOUND = "La ubicacion no existe";
  public static final String OUTFIT_NOT_FOUND = "El outfit no existe";
  public static final String COMENTARIO_NOT_FOUND = "El comentario no existe";
  public static final String TIENDA_FISICA_NOT_FOUND = "La tienda fisica no existe";
  public static final String USUARIO_NOT_FOUND = "El usuario no existe";
  public static final String PRENDA_NOT_FOUND = "La prenda no existe";
  public static final String MARCA_NOT_FOUND = "La marca no existe";

  private ErrorMessage() {
    throw new IllegalStateException("Utility class");
  }
}
