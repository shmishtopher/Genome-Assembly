package genomics;


import java.util.Arrays;
import debruijn.Splittable;


/**
 * KMer represents a sequence of arbitrary length of genes.  To improve space
 * efficency, the k-mer uses a byte-array internally, where each byte can store
 * up to four genes.  This is implemented in a simmilar manner to BigInt, where
 * genes are "little-endian" indexed.  The gene sequence follows this order:
 * index: 0 1 2 3 4 5 6 7  8  9 ...
 * order: 3 2 1 0 7 6 5 4 11 10 ...
 */
public class KMer implements Splittable<KMer> {
  private byte[] buffer;
  private int length;


  public KMer(int k) {
    this.buffer = new byte[k / 4 + 1];
    this.length = 0;
  }


  /**
   * Converts a string of alphabet "ACTG" into a kmer for more efficiant
   * space usage.
   *
   * @param string
   * @return
   */
  public static KMer from(String string) {
    KMer kmer = new KMer(string.length());
    string.chars().forEach(c -> {
      if (c == 'A') kmer.append((byte) 0x00);
      if (c == 'C') kmer.append((byte) 0x01);
      if (c == 'T') kmer.append((byte) 0x02);
      if (c == 'G') kmer.append((byte) 0x03);
    });

    return kmer;
  }


  /**
   * Converts a KMer into a string for printing
   */
  public String toString() {
    String string = new String();

    for (int i = 0; i < length; i += 1) {
      string += Gene.toString(get(i));
    }

    return string;
  }


  /**
   * appends a single gene (represented as a byte) to the k-mer
   * @param gene - a byte representing the gene
   */
  public void append(byte gene) {
    byte segment = buffer[length / 4];
    byte shift = (byte) (length % 4 * 2);

    buffer[length / 4] = (byte) (segment | gene << shift);
    length += 1;
  }


  /**
   * gets a single gene (represented as a byte) from the specified offset in
   * the k-mer
   * @param offset
   * @return
   */
  public byte get(int offset) {
    if (offset < length) {
      byte segment = buffer[offset / 4];
      byte shift = (byte) (offset % 4 * 2);

      return (byte) ((segment & 0x03 << shift) >> shift);
    }
    else {
      throw new IndexOutOfBoundsException();
    }
  }


  /**
   * Use the byte arrays hash code and xor it with the length of the k-mer to
   * ensure that each sequence has a unique hash code
   */
  public int hashCode() {
    return Arrays.hashCode(buffer) ^ length;
  }


  /**
   * Sequences are equal when thier hash codes are the same
   */
  public boolean equals(Object obj) {
    return hashCode() == obj.hashCode();
  }


  /**
   * Return the prefix of a sequence, which is the sequence subtract the last
   * element
   */
  public KMer getPrefix() {
    KMer prefix = new KMer(length - 1);

    for (int i = 0; i < length - 1; i += 1) {
      prefix.append(get(i));
    }

    return prefix;
  }


  /**
   * Return the suffix of a sequence, which is the sequence subtract the first
   * element
   */
  public KMer getSuffix() {
    KMer suffix = new KMer(length  -1);

    for (int i = 1; i < length; i += 1) {
      suffix.append(get(i));
    }

    return suffix;
  }


  /**
   * return the size of the kmer
   */
  public int size() {
    return length;
  }
}