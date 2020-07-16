module one{
    module inve{
        module rpc {
            module fullnode {
                sequence<string> PublicKey1DList;
                sequence<PublicKey1DList> PublicKey2DList;
                interface Register {
                    string registerFullNode(string ip, int rpcPort, int httpPort, string pubkey);    // for full node applicator
                    string addNewFullNode(string fullnodeStr);                      // for full node
                    string logoutFullNode(string pubkey);                           // for full node
                    string getFullNodeList(string pubkey);                          // for full node and local full node

                    string getNeighborLocalFullNodeList(string pubkey);             // for light node
                    string getLocalFullNodeList(string pubkey);                     // for full node

                    string getNodeShardInfo(string pubkey);                         // for local full node
                    string getShardInfoList();                                      // for new full node

                    PublicKey1DList getFullNodePublicKeyList();                     // for full node
                    PublicKey2DList getLocalFullNodePublicKeyList();                // for local full node

                    string registerRelayNode(string pubkey, string ip, int port);   // for relay node
                    string aliveHeartRelayNode(string pubkey);                      // for relay node heart
                    string registerLocalFullNode(string pubkey, string address);    // for local full node applicator
                    bool sendPbftMessage(string message);                           // for full node pbft new shard info and new local full node
                    string queryBlock(string index);                                // for all
                    string getNrgPrice();                                           // for all
                }
            };
        };
    };
};