package genomics;


/**
 * Gene represents a single gene, represented as a single byte.  This class
 * serves mainly as a utility for printing out a sequence of genes easily.
 */
public class Gene {
  private byte value;

  public Gene(byte value) {
    this.value = value;
  }

  /**
   * Return the ACTG representation of a byte
   */
  public String toString() {
    if (value == 0x00) return "A";
    if (value == 0x01) return "C";
    if (value == 0x02) return "T";
    if (value == 0x03) return "G";
    else return "N";
  }

  /**
   * Return the ACTG representation of a byte
   */
  public static String toString(byte value) {
    if (value == 0x00) return "A";
    if (value == 0x01) return "C";
    if (value == 0x02) return "T";
    if (value == 0x03) return "G";
    else return "N";
  }
}