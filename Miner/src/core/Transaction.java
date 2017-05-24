package core;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;

/**
 *
 * @author Samuel Heath
 */
public class Transaction implements Serializable {
    
    private final String sender_key;
    private final String receiver_key;
    private final double coin_amount;
    private String signature;
    
    public Transaction(String senderKey, String recieverKey, double chrisCoins) {
        this.sender_key = senderKey;
        this.receiver_key = recieverKey;
        this.coin_amount = chrisCoins;
    }
    
    public Transaction(String senderKey, String recieverKey, double chrisCoins, String signature) {
        this.sender_key = senderKey;
        this.receiver_key = recieverKey;
        this.coin_amount = chrisCoins;
        this.signature = signature;
    }
    
    public String getSenderKey() { return this.sender_key; }
    
    public String getReceiverKey() { return this.receiver_key; }
    
    public double getTransactionAmount() {return this.coin_amount; }
    
    public String getSignature() { return this.signature; }
    
    /**
     * @return                      Whether or not the transaction signature is valid.
     */
    public boolean verifySignature() {
        try {
            Signature s = Signature.getInstance("SHA256withRSA");
            s.initVerify(KeyPairGen.getPublicKey());
            s.verify(signature.getBytes(StandardCharsets.US_ASCII));
        } catch (NoSuchAlgorithmException NSAE) {
        } catch (SignatureException SE) {
        } catch (InvalidKeyException IKE) {} 
        return true;
    }
    
    
    
    private static byte[] transaction2Bytes(byte[] receiver_key, byte[] amount) {
        return concatByteArr(receiver_key,amount);
    }
    
    /**
     * @param a                     The first byte array.
     * @param b                     The second byte array to be appended.
     * @return                      The resulting byte array after concatination.
     */
    private static byte[] concatByteArr(byte[] a, byte[] b) {
        byte[] concatArr = new byte[a.length+b.length];
        System.arraycopy(a, 0, concatArr, 0, b.length);
        System.arraycopy(b, 0, concatArr, a.length, b.length);
        return concatArr;
    }
    
    public void signCoinBaseTransaction() {
        try {
            Signature s = Signature.getInstance("SHA256withRSA");
            s.initSign(KeyPairGen.getPrivateKey());
            s.update(transaction2Bytes(receiver_key.getBytes(),ByteBuffer.allocate(8).putDouble(coin_amount).array()));
            byte[] sig = s.sign();
            this.signature = Base58Check.encode(sig,false);
        } catch (NoSuchAlgorithmException NSAE) {
                NSAE.printStackTrace();

        } catch (InvalidKeyException IKE) { IKE.printStackTrace(); 
        } catch (SignatureException SE) { SE.printStackTrace(); }
    }
    
    public String transactionToString() {
        StringBuilder sb = new StringBuilder("'");
        sb.append(this.sender_key);
        sb.append("-");
        sb.append(this.receiver_key);
        sb.append("-");
        sb.append(this.coin_amount);
        sb.append("-");
        sb.append(this.signature);
        sb.append("'");
        return sb.toString();
    }
    
}