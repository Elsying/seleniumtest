package com.test.util.blockchain;

import com.google.gson.GsonBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

public class BlockList {
    public static ArrayList<Block>blockchain =new ArrayList<Block>();
    public static int difficulty = 3;
    @Test
    public void b(){
        //w挖矿
        long beginTime1 = new Date().getTime();
        blockchain.add(new Block("Hi i am the first block", "0"));
        System.out.println("Trying to  mine block 1...");
        blockchain.get(0).mineBlock(difficulty);
        long endTime1 = new Date().getTime();
        System.out.println("Mining block 1 cost " + (endTime1 - beginTime1));


    }
    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        for(int i=1;i<blockchain.size();i++){
            currentBlock=blockchain.get(i);
            previousBlock=blockchain.get(i-1);
            //如果Block的哈市属性值变了
            if(currentBlock.hash.equals(currentBlock.calculateHash())){
                System.out.println("Current Hashes not equal!");
                return false;
            }
            if (!previousBlock.hash.equals(currentBlock.preHash)) {
                System.out.println("Previous Hashes not equal!");
                return false;
            }
        }
        return true;

    }
}
