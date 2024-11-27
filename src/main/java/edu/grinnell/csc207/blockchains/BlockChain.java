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
    
    private Node firstBlock;
    private Node lastBlock;
    private int size;
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
    blk.computeHash();

    if(!validator.isValid(blk.getHash())) {
      throw new IllegalArgumentException();
    }
   
    if (!blk.getPrevHash().equals(lastBlock.getBlock().getHash())) {
      throw new IllegalArgumentException();
    }

    Node node = new Node(blk);
    this.lastBlock.addBlock(node);
    lastBlock = node;
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
    while (curr.getNextNode() != lastBlock && curr.getNextNode() != null) {
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
      Transaction transaction = block.getTransaction();
      Hash newHash = block.getHash();
      block.computeHash();

      if(transaction.getAmount() < 0) {
        throw new Exception("Invalid negative transaction");
      }

      if(!validator.isValid(block.getHash())) {
        throw new Exception("isValid");
      } // if


      if(!block.getPrevHash().equals(prevHash)) {
        throw new Exception("getPrev");
      } // if


      if(!block.getHash().equals(newHash)) {
        throw new Exception("getHash");
      }

      if(!transaction.getSource().isEmpty()) {
        int balance = 0;
        Node temp = firstBlock;

        while (temp != curr) {
          Transaction blockTrans = temp.getBlock().getTransaction();

          if (transaction.getSource().equals(blockTrans.getTarget())) {
            balance += blockTrans.getAmount();
          } else if (transaction.getSource().equals(blockTrans.getSource())) {
            balance -= blockTrans.getAmount();
          }

          temp = temp.getNextNode();
        }

        if(balance < transaction.getAmount()) {
          throw new Exception("Invalid amount");
        }
      }
      
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

      private Node curr = firstBlock.getNextNode();

      public boolean hasNext() {
        return curr != null;
      } // hasNext()

      public String next() {
        if(curr == null) {
          throw new NoSuchElementException();
        }
        Transaction transaction = curr.getBlock().getTransaction();
        String user = "";
        if (!transaction.getTarget().isEmpty()) {
          user = transaction.getTarget();
        } // if/else
        
        curr = curr.getNextNode();
        return user;
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
    int balance = 0;
    Node curr = firstBlock;

    while (curr != null) {
      Transaction transaction = curr.getBlock().getTransaction();
      int amount = transaction.getAmount();
      if(user.equals(transaction.getTarget())) {
        balance += amount;
      }
      else if (user.equals(transaction.getSource())) {
        balance -= amount;
      } // if/else

      curr = curr.getNextNode();
    } // while-loop

    return balance;
  } // balance()

  /**
   * Get an interator for all the blocks in the chain.
   *
   * @return an iterator for all the blocks in the chain.
   */
  public Iterator<Block> blocks() {
    return new Iterator<Block>() {

      private Node curr = firstBlock;

      public boolean hasNext() {
        return curr != null;
      } // hasNext()

      public Block next() {
        if(curr == null) {
          throw new NoSuchElementException();
        }
        Block block = curr.getBlock();
        curr = curr.getNextNode();
        return block;
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

      private Node curr = firstBlock;

      public boolean hasNext() {
        return curr != null;
      } // hasNext()

      public Transaction next() {
        if(curr == null) {
          throw new NoSuchElementException();
        }
        Transaction transaction = curr.getBlock().getTransaction();
        curr = curr.getNextNode();
        return transaction;
      } // next()
    };
  } // iterator()

} // class BlockChain
