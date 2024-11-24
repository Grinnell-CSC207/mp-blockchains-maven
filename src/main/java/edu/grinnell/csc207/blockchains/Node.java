package edu.grinnell.csc207.blockchains;

public class Node {
  Node prev;

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
  public Node(Block val, Node next) {
    this.block = val;
    this.next = next;
  } // Node2(Block, Node)


  /**
   * Create a new node with no next link (e.g., if it's at the end of
   * the list). Included primarily for symmetry.
   *
   *   The previous node in the list (or null, if it's the front of the list).
   * @param val
   *   The value to be stored in the node.
   */
  public Node(Block val) {
    this(val, null);
  } // Node2(Node)

}
