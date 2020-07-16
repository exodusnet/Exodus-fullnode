package org.exodus.node;

import org.exodus.bean.node.RelayNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @date 2018/10/11 0011 下午 4:29
 **/
public class Test {
    public static void main(String[] args) {
        ConcurrentHashMap<String, RelayNode> nodes = new ConcurrentHashMap<>();
        for(int i=0; i<5; i++) {
            new ListParallelThread(nodes).start();
        }
    }
}

class ListParallelThread extends Thread {
    private static final Logger logger = LoggerFactory.getLogger("ListParallelThread.class");
    private ConcurrentHashMap<String, RelayNode> nodes;

    public ListParallelThread(ConcurrentHashMap<String, RelayNode> nodes) {
        this.nodes = nodes;
    }

    @Override
    public void run() {
        int i=200;
        Random r = new Random();
        while ((i--)>0) {
            String pubkey = "2222222222";
            nodes.put(pubkey, new RelayNode.Builder().pubkey(pubkey).build());
            logger.info("{}:{}", this.getName(), nodes.size());
//            try {
//                sleep(20);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }
}
