package edu.grinnell.csc207.main;

import edu.grinnell.csc207.blockchains.Block;
import edu.grinnell.csc207.blockchains.BlockChain;
import edu.grinnell.csc207.blockchains.HashValidator;
import edu.grinnell.csc207.blockchains.Transaction;
import edu.grinnell.csc207.blockchains.Hash;

import edu.grinnell.csc207.util.IOUtils;

import java.io.PrintWriter;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * A simple UI for our BlockChain class.
 *
 * @author Luis Lopez
 * @author Alex Cyphers
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
   * @param pen
   *   The pen used for printing instructions.
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
   * @param args
   *   Command-line arguments (currently ignored).
   */
  public static void main(String[] args) throws Exception {
    PrintWriter pen = new PrintWriter(System.out, true);
    BufferedReader eyes = new BufferedReader(new InputStreamReader(System.in));

    // Set up our blockchain.
    HashValidator validator =
        (h) -> {
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

    String source;
    String target;
    int amount;

    while (!done) {
      pen.print("\nCommand: ");
      pen.flush();
      String command = eyes.readLine();
      if (command == null) {
        command = "quit";
      } // if

      switch (command.toLowerCase()) {
        case "append":
          pen.printf("\nSource (return for deposit): ");
          String src = eyes.readLine();
          pen.printf("\nTarget: ");
          String tgt = eyes.readLine();
          pen.printf("\nAmount: ");
          String amnt = eyes.readLine();
          int amt = Integer.valueOf(amnt);
          pen.printf("\nNonce; ");
          String nnc = eyes.readLine();
          long nonce = Long.valueOf(nnc);
          Transaction trans = new Transaction(src, tgt, amt);
          Hash hash = chain.getHash();
          int blknum = chain.getSize();
          Block blk = new Block(blknum, trans, hash, nonce);
          chain.append(blk);
          pen.printf("\nAppended: Block %d (Transaction: %s, Nonce: %l", blknum, trans.toString(), nonce);
          break;

        case "balance":
          pen.printf("\nUser: ");
          String user = eyes.readLine();
          pen.printf("\n%s's balance is %d", user, chain.balance(user));
          break;

        case "blocks":
          Iterator<Block> blocks = chain.blocks();
          while (blocks.hasNext()) {
            pen.printf("\n%s", blocks.next().toString());
          }
          break;

        case "check":
          boolean right = chain.isCorrect();
          if (right) {
            pen.printf("\nThe Blockchain checks out.");
          } else {
            pen.printf("\nThe Blockchain does not check out.");
          }
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
          boolean remove = chain.removeLast();
          if (remove) {
            pen.printf("\nRemoved last element.");
          } else {
            pen.printf("\nNo last elmeent to remove.");
          }

          break;

        case "transactions":
          Iterator<Transaction> transacts = chain.iterator();
          while (transacts.hasNext()) {
            pen.printf("\n%s", transacts.next().toString());
          }
          break;

        case "users":
        Iterator<String> users = chain.users();
        while (users.hasNext()) {
          pen.printf("\n%s", users.next().toString());
        }
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
