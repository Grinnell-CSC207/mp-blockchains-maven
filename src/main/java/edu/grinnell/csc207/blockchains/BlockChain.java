package edu.grinnell.csc207.blockchains;

import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.grinnell.csc207.blockchains.Node;

/**
 * A full blockchain.
 *
 * @author Alex Cyphers
 * @author Luis Lopez
 */
public class BlockChain implements Iterable<Transaction> {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+
    
    Node firstBlock;
    Node lastBlock;
    int size;
    private HashValidator validator;
  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new blockchain using a validator to check elements.
   *
   * @param check
   *   The validator used to check elements.
   */
  public BlockChain(HashValidator check) {
    this.validator = check;
    Block blk = new Block(0, new Transaction("", "", 0), new Hash(new byte[] {}), check);
    this.firstBlock = new Node(blk);
    this.lastBlock = this.firstBlock;
    this.size = 1;
  } // BlockChain(HashValidator)

  // +---------+-----------------------------------------------------
  // | Helpers |
  // +---------+

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Mine for a new valid block for the end of the chain, returning that
   * block.
   *
   * @param t
   *   The transaction that goes in the block.
   *
   * @return a new block with correct number, hashes, and such.
   */
  public Block mine(Transaction t) {
    return new Block(size, t, lastBlock.getBlock().getHash(), validator);
  } // mine(Transaction)

  /**
   * Get the number of blocks curently in the chain.
   *
   * @return the number of blocks in the chain, including the initial block.
   */
  public int getSize() {
    return this.size;
  } // getSize()

  /**
   * Add a block to the end of the chain.
   *
   * @param blk
   *   The block to add to the end of the chain.
   *
   * @throws IllegalArgumentException if (a) the hash is not valid, (b)
   *   the hash is not appropriate for the contents, or (c) the previous
   *   hash is incorrect.
   */
  public void append(Block blk) {
    Node node = new Node(blk);
    this.lastBlock.addBlock(node);
    this.size++;
  } // append()

  /**
   * Attempt to remove the last block from the chain.
   *
   * @return false if the chain has only one block (in which case it's
   *   not removed) or true otherwise (in which case the last block
   *   is removed).
   */
  public boolean removeLast() {
    if (size < 2) {
      return false;
    } // if

    Node curr = firstBlock;
    // Loop until second to last block
    while (curr.getNextNode() != lastBlock) {
      curr = curr.getNextNode();
    } // while-loop

    curr.addBlock(null); // Set the last block to null
    lastBlock = curr; // Set second-to-last block to last block.
    size--;
    return true;
  } // removeLast()

  /**
   * Get the hash of the last block in the chain.
   *
   * @return the hash of the last sblock in the chain.
   */
  public Hash getHash() {
    return this.lastBlock.getBlock().getHash();
  } // getHash()

  /**
   * Determine if the blockchain is correct in that (a) the balances are
   * legal/correct at every step, (b) that every block has a correct
   * previous hash field, (c) that every block has a hash that is correct
   * for its contents, and (d) that every block has a valid hash.
   *
   * @return true if the blockchain is correct and false otherwise.
   */
  public boolean isCorrect() {
    try {
      check();
      return true;
    } catch (Exception e) {
      return false;
    }
  } // isCorrect()

  /**
   * Determine if the blockchain is correct in that (a) the balances are
   * legal/correct at every step, (b) that every block has a correct
   * previous hash field, (c) that every block has a hash that is correct
   * for its contents, and (d) that every block has a valid hash.
   *
   * @throws Exception
   *   If things are wrong at any block.
   */
  public void check() throws Exception {
    Node curr = firstBlock;
    Hash prevHash = new Hash(new byte[] {});

    while (curr != null) {
      Block block = curr.getBlock();

      if(!block.getPrevHash().equals(prevHash)) {
        throw new Exception();
      } // if

      if(!validator.isValid(block.getHash())) {
        throw new Exception();
      } // if
      prevHash = block.getHash();
      curr = curr.getNextNode();
      prevHash = block.getHash();
      curr = curr.getNextNode();
    } // while-loop
  } // check()

  /**
   * Return an iterator of all the people who participated in the
   * system.
   *
   * @return an iterator of all the people in the system.
   */
  public Iterator<String> users() {
    return new Iterator<String>() {
      public boolean hasNext() {
        return false;   // STUB
      } // hasNext()

      public String next() {
        throw new NoSuchElementException();     // STUB
      } // next()
    };
  } // users()

  /**
   * Find one user's balance.
   *
   * @param user
   *   The user whose balance we want to find.
   *
   * @return that user's balance (or 0, if the user is not in the system).
   */
  public int balance(String user) {
    return 0;   // STUB
  } // balance()

  /**
   * Get an interator for all the blocks in the chain.
   *
   * @return an iterator for all the blocks in the chain.
   */
  public Iterator<Block> blocks() {
    return new Iterator<Block>() {
      public boolean hasNext() {
        return false;   // STUB
      } // hasNext()

      public Block next() {
        throw new NoSuchElementException();     // STUB
      } // next()
    };
  } // blocks()

  /**
   * Get an interator for all the transactions in the chain.
   *
   * @return an iterator for all the blocks in the chain.
   */
  public Iterator<Transaction> iterator() {
    return new Iterator<Transaction>() {
      public boolean hasNext() {
        return false;   // STUB
      } // hasNext()

      public Transaction next() {
        throw new NoSuchElementException();     // STUB
      } // next()
    };
  } // iterator()

} // class BlockChain
