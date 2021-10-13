import java.util.*;

public class BlockchainMain {
    public static ArrayList<Block> blockchain = new ArrayList<>();
    public static ArrayList<Transaction> transactions = new ArrayList<>();
    public static int difficulty = 5;

    public static void main(String args[]){
        Wallet A = new Wallet(blockchain);
        Wallet B = new Wallet(blockchain);
        System.out.println("Wallet A Balance: " + A.getBalance());
        System.out.println("Wallet B Balance: " + B.getBalance());

        System.out.println("Add two transactions");
        Transaction tran1 = A.send(B.publicKey, 10);
        if(tran1 != null){
            transactions.add(tran1);
        }

        Transaction tran2 = A.send(B.publicKey, 20); // change 20 to 200 to test transaction rejection
        if(tran2 != null) {
            transactions.add(tran2);
        }

        Block b = new Block(0, null, transactions);
        b.mineBlock(difficulty);
        blockchain.add(b);
        System.out.println("Current block valid: " + validateBlock(b, null));

        System.out.println("Wallet A Balance: " + A.getBalance());
        System.out.println("Wallet B Balance: " + B.getBalance());
        System.out.println("Blockchain valid: " + validateChain(blockchain));
    }

    public static boolean validateBlock(Block newBlock, Block prevBlock){
        if (prevBlock == null) { // first block

            // validate last index
            if(newBlock.index != 0) {
                return false;
            }

            // validate value of prevHash
            if (newBlock.prevHash != null) {
                return false;
            }

            // validate hash of previous block
            if (newBlock.curHash == null || !newBlock.calculateHash().equals(newBlock.curHash)) {
                return false;
            }

            return true;

        } else {
            if (newBlock != null) { // rest of the blocks

                // validate next index is one greater than previous
                if (prevBlock.index + 1 != newBlock.index) {
                    return false;
                }

                // validate prevHash is the same for last block and new block
                if (newBlock.prevHash == null || !newBlock.prevHash.equals(prevBlock.curHash)){
                    return false;
                }

                // validate the current hash was calculated correctly
                if (newBlock.curHash == null || !newBlock.calculateHash().equals(newBlock.curHash)){
                    return false;
                }

                return true;
            }

            return false;
        }

        
    }

    public static boolean validateChain(ArrayList<Block> blockchain){
        if (!validateBlock((blockchain.get(0)), null)){ // check if genesis block is valid
            return false;
        }

        for(int i = 1; i < blockchain.size(); i++){
            Block currentBlock = blockchain.get(i);
            Block previousBlock = blockchain.get(i - 1);

            if (!validateBlock(currentBlock, previousBlock)){
                return false;
            }
        }

        return true;
    }
}
