package edu.grinnell.csc207.blockchains;

public class Node {

  Node next;

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


  public Block getBlock() {
    return this.block;
  }

  public Node getNextNode() {
    return next;
  }

  public void addBlock(Node val) {
    this.next = val;
  }
}
