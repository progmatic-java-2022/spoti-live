package hu.progmatic.spotilive.felhasznalo;

public class FelhasznaloLetrehozasException extends RuntimeException {
  private final String mezoNev;
  public FelhasznaloLetrehozasException(String mezoNev, String message) {
    super(message);
    this.mezoNev = mezoNev;
  }
  public FelhasznaloLetrehozasException(String message) {
    super(message);
    this.mezoNev = null;
  }

  public String getMezoNev() {
    return mezoNev;
  }
}
