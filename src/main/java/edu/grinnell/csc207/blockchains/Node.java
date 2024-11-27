package edu.grinnell.csc207.blockchains;


/**
 * Class implements a single linked list which
 * expects blocks to be held in the nodes.
 */
public class Node {

  /**
   * The next node in the list.
   */
  Node next;

  /**
   * The block being stored in the node.
   */
  Block block;


   /**
   * Create a new node.
   *
   * @param val
   *   The value to be stored in the node.
   * @param next
   *   The next node in the list (or null, if it's the end of the list).
   */
  public Node(Block val) {
    this.block = val;
    this.next = null;
  } // Node2(Block, Node)


  /**
   * Returns the block at the current node.
   *
   * @return
   *  The block at current node.
   */
  public Block getBlock() {
    return this.block;
  } // getBlock()


  /**
   * Gets the next node in the list.
   *
   * @return
   *   The next node in the list.
   */
  public Node getNextNode() {
    return next;
  } // getNextNode()

  /**
   * Adds a block node to the next node in the
   * list.
   *
   * @param val
   *   Expects a node with a block value.
   */
  public void addBlock(Node val) {
    this.next = val;
  } // addBlock(node)
} // class Node
