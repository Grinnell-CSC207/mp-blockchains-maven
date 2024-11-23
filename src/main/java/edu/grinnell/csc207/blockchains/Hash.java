package edu.grinnell.csc207.blockchains;
import java.util.Arrays;

/**
 * Encapsulated hashes.
 *
 * @author Alex Cyphers
 * @author Luis Lopez
 * @author Samuel A. Rebelsky
 */
public class Hash {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  byte[] byteData;

  int length;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new encapsulated hash.
   *
   * @param data
   *   The data to copy into the hash.
   */
  public Hash(byte[] data) {
    this.byteData = Arrays.copyOf(data, data.length);
    this.length = this.byteData.length;
  } // Hash(byte[])

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Determine how many bytes are in the hash.
   *
   * @return the number of bytes in the hash.
   */
  public int length() {
    return this.byteData.length;
  } // length()

  /**
   * Get the ith byte.
   *
   * @param i
   *   The index of the byte to get, between 0 (inclusive) and
   *   length() (exclusive).
   *
   * @return the ith byte
   */
  public byte get(int i) {
    return this.byteData[i];
  } // get()

  /**
   * Get a copy of the bytes in the hash. We make a copy so that the client
   * cannot change them.
   *
   * @return a copy of the bytes in the hash.
   */
  public byte[] getBytes() {
    byte[] copy = Arrays.copyOf(byteData, this.length);
    return copy;
  } // getBytes()

  /**
   * Convert to a hex string.
   *
   * @return the hash as a hex string.
   */
  public String toString() {
    String str = "";
    for (int i = 0; i < this.length; i++) {
      str = str + String.format("%02X", this.byteData[i]);
    }
    return str;
  } // toString()

  /**
   * Determine if this is equal to another object.
   *
   * @param other
   *   The object to compare to.
   *
   * @return true if the two objects are conceptually equal and false
   *   otherwise.
   */
  public boolean equals(Object other) {
    if (other instanceof Hash) {
      return Arrays.equals(((Hash) other).byteData, this.byteData);
    } else{
      return false;
    }
  } // equals(Object)


  /**
   * Get the hash code of this object.
   *
   * @return the hash code.
   */
  public int hashCode() {
    return this.toString().hashCode();
  } // hashCode()
} // class Hash
