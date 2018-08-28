package com.test.util.blockchain;

import com.sun.prism.impl.paint.PaintUtil;
import org.junit.Test;

/**
 * 每个块不仅仅包含它之前的块信息，同时也包含自身。如果前面一块内容改变了，
 * 其 hash 值也会改变，将会导致其后面所有的块发生变化。通过计算和比较所得的 hash 值，
 * 我们可以判断区块链是否合法。换句话说，改变区块链中的任意内容，将会改变整个区块链的签名。
 */
import java.util.Date;

public class Block {
    public String hash; //存放数字签名
    public String preHash; //前面块的签名
    private String data;
    private long timeStamp;
    private int nonce;

    public Block(String data, String preHash) {
        this.data = data;
        this.preHash = preHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    //计算数字签名
    public String calculateHash(){
        return StringUtil.applySha256(preHash + Long.toString(timeStamp)+Integer.toString(nonce) + data);
    }

    // difficulty 变量就是用来控制计算量，'\0表示空字符
    public void mineBlock(int difficulty){
            String target = new String(new char[difficulty]).replace('\0', '0');//就是difficulty个0
        //寻找配置
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
            System.out.println(hash.substring(0, difficulty));
        }
        System.out.println("Block Mined!!!" + hash);
    }


}
