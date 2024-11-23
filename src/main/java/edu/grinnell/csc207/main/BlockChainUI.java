package edu.grinnell.csc207.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import edu.grinnell.csc207.blockchains.Block;
import edu.grinnell.csc207.blockchains.BlockChain;
import edu.grinnell.csc207.blockchains.HashValidator;
import edu.grinnell.csc207.blockchains.Transaction;
import edu.grinnell.csc207.util.IOUtils;

/**
 * A simple UI for our BlockChain class.
 *
 * @author Khanh Do
 * @author Nicole Gorrell
 * @author Samuel A. Rebelsky
 */
public class BlockChainUI {
  // +-----------+---------------------------------------------------
  // | Constants |
  // +-----------+

  /**
   * The number of bytes we validate. Should be set to 3 before submitting.
   */
  static final int VALIDATOR_BYTES = 0;

  // +---------+-----------------------------------------------------
  // | Helpers |
  // +---------+

  /**
   * Print out the instructions.
   *
   * @param pen The pen used for printing instructions.
   */
  public static void instructions(PrintWriter pen) {
    pen.println("""
        Valid commands:
          mine: discovers the nonce for a given transaction
          append: appends a new block onto the end of the chain
          remove: removes the last block from the end of the chain
          check: checks that the block chain is valid
          users: prints a list of users
          balance: finds a user's balance
          transactions: prints out the chain of transactions
          blocks: prints out the chain of blocks (for debugging only)
          help: prints this list of commands
          quit: quits the program""");
  } // instructions(PrintWriter)

  // +------+--------------------------------------------------------
  // | Main |
  // +------+

  /**
   * Run the UI.
   *
   * @param args Command-line arguments (currently ignored).
   */
  public static void main(String[] args) throws Exception {
    PrintWriter pen = new PrintWriter(System.out, true);
    BufferedReader eyes = new BufferedReader(new InputStreamReader(System.in));

    // Set up our blockchain.
    HashValidator validator = (h) -> {
      if (h.length() < VALIDATOR_BYTES) {
        return false;
      } // if
      for (int v = 0; v < VALIDATOR_BYTES; v++) {
        if (h.get(v) != 0) {
          return false;
        } // if
      } // for
      return true;
    };
    BlockChain chain = new BlockChain(validator);

    instructions(pen);

    boolean done = false;

    String source = "";
    String target = "";
    int amount = 0;

    while (!done) {
      pen.print("\nCommand: ");
      pen.flush();
      String command = eyes.readLine();
      if (command == null) {
        command = "quit";
      } // if

      switch (command.toLowerCase()) {
        case "append":
          pen.printf("Adding new block...\n", command);
          chain.append(new Block(chain.getSize(), new Transaction(source, target, amount),
              chain.getHash(), validator));
          pen.print("Block appended.");
          break;

        case "balance":
          pen.printf("" + source + "'s balance: " + chain.balance(source), command);
          break;

        case "blocks":
          pen.printf("Chain of blocks:\n" + chain.blocks().toString(), command);
          break;

        case "check":
          pen.printf("Checking validity of blockchain...\n", command);
          if (chain.isCorrect()) {
            pen.println("Blockchain is valid.");
          } else {
            pen.println("Blockchain is not valid.");
          } // if/else
          break;

        case "help":
          instructions(pen);
          break;

        case "mine":
          source = IOUtils.readLine(pen, eyes, "Source (return for deposit): ");
          target = IOUtils.readLine(pen, eyes, "Target: ");
          amount = IOUtils.readInt(pen, eyes, "Amount: ");
          Block b = chain.mine(new Transaction(source, target, amount));
          pen.println("Nonce: " + b.getNonce());
          break;

        case "quit":
          done = true;
          break;

        case "remove":
          pen.printf("Removing last block entry...\n", command);
          if (chain.removeLast()) {
            pen.println("Successfully removed block.");
          } else {
            pen.println("Failed to remove block.");
          } // if/else
          break;

        case "transactions":
          pen.printf("Chain of transactions:\n" + chain.iterator().toString(), command);
          break;

        case "users":
          pen.printf("List of users:\n" + chain.users().toString(), command);
          break;

        default:
          pen.printf("invalid command: '%s'. Try again.\n", command);
          break;
      } // switch
    } // while

    pen.printf("\nGoodbye\n");
    eyes.close();
    pen.close();
  } // main(String[])
} // class BlockChainUI
