package edu.grinnell.csc207.blockchains;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Random;


/**
 * Blocks to be stored in blockchains.
 *
 * @author Luis Lopez
 * @author Alex Cyphers
 * @author Samuel A. Rebelsky
 */
public class Block {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The number of the block.
   */
  private int num;


  /**
   * The transaction for the block.
   */
  public Transaction transaction;


  /**
   * The hash for the previous block.
   */
  private Hash prevHash;


  /**
   * The nonce for the block.
   */
  public long nonce;


  /**
   * The hash for the current block.
   */
  private Hash currHash;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new block from the specified block number, transaction, and
   * previous hash, mining to choose a nonce that meets the requirements
   * of the validator.
   *
   * @param num
   *   The number of the block.
   * @param transaction
   *   The transaction for the block.
   * @param prevHash
   *   The hash of the previous block.
   * @param check
   *   The validator used to check the block.
   */
  public Block(int num, Transaction transaction, Hash prevHash,
      HashValidator check) {

    this.num = num;
    this.transaction = transaction;
    this.prevHash = prevHash;
    this.computeHash();
    Random random = new Random();
    this.nonce = random.nextInt(100, 100000);

    while (!check.isValid(this.currHash)) {
      this.nonce = random.nextInt(100, 100000);
      this.computeHash();
    } // while-loop
  } // Block(int, Transaction, Hash, HashValidator)

  /**
   * Create a new block, computing the hash for the block.
   *
   * @param num
   *   The number of the block.
   * @param transaction
   *   The transaction for the block.
   * @param prevHash
   *   The hash of the previous block.
   * @param nonce
   *   The nonce of the block.
   */
  public Block(int num, Transaction transaction, Hash prevHash, long nonce) {
    this.num = num;
    this.transaction = transaction;
    this.prevHash = prevHash;
    this.nonce = nonce;
    this.computeHash();
  } // Block(int, Transaction, Hash, long)

  // +---------+-----------------------------------------------------
  // | Helpers |
  // +---------+

  /**
   * Compute the hash of the block given all the other info already
   * stored in the block.
   */
  public void computeHash() {
    try {
      MessageDigest md = MessageDigest.getInstance("sha-256");

      byte[] byteNum = ByteBuffer.allocate(Integer.BYTES).putInt(num).array();

      byte[] byteTranSrc = transaction.getSource().getBytes();
      byte[] byteTranTgt = transaction.getTarget().getBytes();
      byte[] byteTranAmt = ByteBuffer.allocate(Integer.BYTES)
            .putInt(transaction.getAmount()).array();

      byte[] bytePrev = this.prevHash.getBytes();
      byte[] byteLong = ByteBuffer.allocate(Long.BYTES).putLong(nonce).array();

      md.update(byteNum);
      md.update(byteTranSrc);
      md.update(byteTranTgt);
      md.update(byteTranAmt);
      md.update(bytePrev);
      md.update(byteLong);

      this.currHash = new Hash(md.digest());
    } catch (Exception e) {
      // No Exception Thrown
    } // try/catch
  } // computeHash()

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Get the number of the block.
   *
   * @return the number of the block.
   */
  public int getNum() {
    return this.num;
  } // getNum()

  /**
   * Get the transaction stored in this block.
   *
   * @return the transaction.
   */
  public Transaction getTransaction() {
    return new Transaction(this.transaction.getSource(),
                           this.transaction.getTarget(),
                           this.transaction.getAmount());
  } // getTransaction()

  /**
   * Get the nonce of this block.
   *
   * @return the nonce.
   */
  public long getNonce() {
    return this.nonce;
  } // getNonce()

  /**
   * Get the hash of the previous block.
   *
   * @return the hash of the previous block.
   */
  Hash getPrevHash() {
    return this.prevHash;
  } // getPrevHash

  /**
   * Get the hash of the current block.
   *
   * @return the hash of the current block.
   */
  Hash getHash() {
    return this.currHash;
  } // getHash

  /**
   * Get a string representation of the block.
   *
   * @return a string representation of the block.
   */
  public String toString() {
    String blk = "Block " + this.getNum() + " (" + this.getTransaction().toString() + " Nonce: " + this.getNonce() + " prevHash: " + this.prevHash + " hash: " + this.getHash() + ")";
    return blk;
  } // toString()
} // class Block
