import java.security.*;
import java.util.*;

// Create a single block
public class Block {
    public int index;
    public long timestamp;
    public String curHash;
    public String prevHash;
    public String data;
    public ArrayList<Transaction> transactions = new ArrayList<>();
    public int nonce;

    public Block(int index, String prevHash, ArrayList<Transaction> transactions){
        this.index = index;
        this.timestamp = System.currentTimeMillis();
        this.prevHash = prevHash;
        this.transactions = transactions;
        nonce = 0;
        curHash = calculateHash();
    }

    public String calculateHash(){
        try{
            data = "";
            for(int i = 0; i < transactions.size(); i++){
                Transaction tr = transactions.get(i);
                data = data + tr.sender + tr.recipient + tr.value;
            }

            String input = index + timestamp + prevHash + data + nonce;

            MessageDigest msgDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = msgDigest.digest(input.getBytes("UTF-8"));

            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++){
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void mineBlock(int difficulty){
        nonce = 0;
        String target = new String(new char[difficulty]).replace('\0', '0');

        while(!curHash.substring(0, difficulty).equals(target)){
            nonce++;
            curHash = calculateHash();
        }
    }

    public String toString(){
        String s = "Block # : " + index + "\r\n";
        s += "Previous Hash: " + prevHash + "\r\n";
        s += "Timestamp: " + timestamp + "\r\n";
        s += "Transactions: " + data + "\r\n";
        s += "Nonce: " + nonce + "\r\n";
        s += "Current Hash: " + curHash + "\r\n";

        return s;
    }
    
}
