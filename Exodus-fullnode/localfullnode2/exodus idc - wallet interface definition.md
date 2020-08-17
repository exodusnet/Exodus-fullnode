# exodus idc - wallet interface definition

## Revision history

| date      | version | description | modifier |
| --------- | ------- | ----------- | -------- |
| 8/15/2020 | 0.1     |             | Francis  |



## Knowledge

The whole exodus blockchain is composed of three different types nodes: seed node,full node and localfullnode node.

The seed node play roles like localfullnode node admission,key information distribution.

The full node collaborates with seed node to manage the whole blockchain.

The localfullnode node takes the majority of responsibility to receive messages,sync messages and persist messages.



## Interface definition

### get NRG price

POST http://$seedNodeAddress:$seedNodeHttpPort/v1/price/nrg



#### parameter description

None

example:

```
{}
```
Note: In the exodus blockchain,brackets("{}") represents empty object.

### result

#### http response

| name     | type        | parameter description                                        | value example |
| -------- | ----------- | ------------------------------------------------------------ | ------------- |
| code     | integer     | 200 indicates successful submission while others indicating error arises. | 200           |
| data     | json object | contain nrg price information                                |               |
| nrgPrice | big integer | nrg price which is valued in atom unit(one exodus = 10^18atom) |               |

http response example

```
{
  "code":200,
  "data":"{\"nrgPrice\":\"1000000000\"}" 
}
```



### pull segmented transaction list in a loop

POST http://$localFullNodeAddress:$localFullNodeHttpPort/v1/gettransactionlist



#### parameter description

| name       | type   | mandatory | parameter description      |
| ---------- | ------ | --------- | -------------------------- |
| address    | string | true      | belong to which address    |
| tableIndex | long   | false     | table index(start with 0)  |
| offset     | long   | false     | offset value(start with 0) |

example:

```
{
	"address": "IT3GOEMWU3M47PSRGXKC7WCULKOTSWFP",
	"tableIndex": 0,
	"offset": 100
}
```

### result

#### http response

| name       | type        | parameter description                                        | value example |
| ---------- | ----------- | ------------------------------------------------------------ | ------------- |
| code       | int         | 200 indicates successful submission while others indicating error arises. |               |
| data       | json object | contain the following information                            |               |
| tableIndex | long        | table index                                                  |               |
| offset     | long        | offset value                                                 |               |
| list       | json array  | the following fields container                               |               |
| eHash      | string      | event hash                                                   |               |
| hash       | string      | message hash                                                 |               |
| id         | string      | consensus id                                                 |               |
| isStable   | boolean     | consensus status: true,consensus completion; false,in consensus process |               |
| isValid    | boolean     | consensus result: true,valid transaction;false,invalid transaction |               |
| lastIdx    | boolean     | whether it is contained in the last event                    |               |
| updateTime | long        | modified time,in milliseconds since 1970                     |               |
| message    | json object | raw message                                                  |               |

response example

```
{
"code":200,
"data":"{\"list\":[{\"eHash\":\"MRYOHe+u6JkIr6EmvnfGPH3pXEs5ssUc6Y4eEtD4yjVAZvEvLsVZ3B+phtRI5Ug2\",\"hash\":\"32qLqv5s09ud1C912mp5b3AqgnYvY4xqWDHROnTkrT3fcwRizrltrxe0sfnvm/7lAn2T5X3NqRmf59EoKPpBJnHw==\",\"id\":\"614\",\"isStable\":true,\"isValid\":true,\"lastIdx\":true,\"message\":\"{\\\"fromAddress\\\":\\\"EVPBGL2SN4Z44AIRZEIKBWUGQG5BXWQQ\\\",\\\"toAddress\\\":\\\"IT3GOEMWU3M47PSRGXKC7WCULKOTSWFP\\\",\\\"amount\\\":\\\"2088444000000000000000000\\\",\\\"timestamp\\\":1551691259000,\\\"remark\\\":\\\"\\\",\\\"vers\\\":\\\"2.0\\\",\\\"pubkey\\\":\\\"AqMYjLmyxRkQ6Ff1VTcKXDuaZ2XlsG947Y3MoidbCDjU\\\",\\\"type\\\":1,\\\"fee\\\":\\\"500000\\\",\\\"nrgPrice\\\":\\\"1000000000\\\",\\\"signature\\\":\\\"32qLqv5s09ud1C912mp5b3AqgnYvY4xqWDHROnTkrT3fcwRizrltrxe0sfnvm/7lAn2T5X3NqRmf59EoKPpBJnHw==\\\"}\",\"updateTime\":1551691184097},{\"eHash\":\"sSUJxBpVcOwHzwT3ImdxUSM1QOSt/PtC7u5SgJRa2YwdX514YaFDGwyo7BfNqHJU\",\"hash\":\"32La74zuhAbwCitN3UGAKjy8JImmyrFLEUdVQasfXo7Thriql9wX64ZqQkoY/anMocmNLefUfivXlqbHPK+HW70Q==\",\"id\":\"621\",\"isStable\":true,\"isValid\":true,\"lastIdx\":true,\"message\":\"{\\\"nrgPrice\\\":\\\"1000000000\\\",\\\"amount\\\":\\\"20962503690581460000000\\\",\\\"signature\\\":\\\"32La74zuhAbwCitN3UGAKjy8JImmyrFLEUdVQasfXo7Thriql9wX64ZqQkoY/anMocmNLefUfivXlqbHPK+HW70Q==\\\",\\\"fee\\\":\\\"500000\\\",\\\"vers\\\":\\\"2.0\\\",\\\"fromAddress\\\":\\\"64QGQQUGRHM5WWEI5TUBCMJECSQXHUB3\\\",\\\"remark\\\":\\\"\\\",\\\"type\\\":1,\\\"toAddress\\\":\\\"IT3GOEMWU3M47PSRGXKC7WCULKOTSWFP\\\",\\\"timestamp\\\":1551711453632,\\\"pubkey\\\":\\\"AjCnnJ56HL8Oh740C3iu+lVyw7RiuHG8oYCGxlyz9hoF\\\"}\",\"updateTime\":1551711459210},{\"eHash\":\"L3UN/Qu9868W415dGWmuWijZ7ImD+gfasl9WBjQVf509rBrH3Vm+jkj36ejJYMa2\",\"hash\":\"327EmGegunGME46o0XRlTNNzW33U/mtpZnX/guBvwc13EqL4itwjfoouH0AsO7naxxzbfBfMzTkSo1evubOsXYqw==\",\"id\":\"655\",\"isStable\":true,\"isValid\":true,\"lastIdx\":true,\"message\":\"{\\\"fromAddress\\\":\\\"IT3GOEMWU3M47PSRGXKC7WCULKOTSWFP\\\",\\\"toAddress\\\":\\\"KWNQ6T6RDBITDMEITSFM7R6MCOQALVEV\\\",\\\"amount\\\":\\\"580210000000000000000000\\\",\\\"timestamp\\\":1551772792365,\\\"remark\\\":\\\"\\\",\\\"vers\\\":\\\"2.0\\\",\\\"pubkey\\\":\\\"AnQabHLKsnQM7q7Wa5Em0HBZ9O8cDIjTfmp5mQYBgW9W\\\",\\\"type\\\":1,\\\"fee\\\":\\\"500000\\\",\\\"nrgPrice\\\":\\\"1000000000\\\",\\\"signature\\\":\\\"327EmGegunGME46o0XRlTNNzW33U/mtpZnX/guBvwc13EqL4itwjfoouH0AsO7naxxzbfBfMzTkSo1evubOsXYqw==\\\"}\",\"updateTime\":1551772797090},{\"eHash\":\"3qLz/NdoWOXeZAB2j5X9b8NG+8lndqQSJCtoDwVaxnPfruYwblSy0/t6ao0PlT/I\",\"hash\":\"32mKuVZ+N4ez/14C96/vm3Awe2ll4rru9RjTVl/7UE080+xOa9oL8TPq2Iw1azlHZ1PiOvRVCo3kZPjRfeS+CXHw==\",\"id\":\"656\",\"isStable\":true,\"isValid\":true,\"lastIdx\":true,\"message\":\"{\\\"fromAddress\\\":\\\"IT3GOEMWU3M47PSRGXKC7WCULKOTSWFP\\\",\\\"toAddress\\\":\\\"KWNQ6T6RDBITDMEITSFM7R6MCOQALVEV\\\",\\\"amount\\\":\\\"377800000000000000000000\\\",\\\"timestamp\\\":1551772839084,\\\"remark\\\":\\\"\\\",\\\"vers\\\":\\\"2.0\\\",\\\"pubkey\\\":\\\"AnQabHLKsnQM7q7Wa5Em0HBZ9O8cDIjTfmp5mQYBgW9W\\\",\\\"type\\\":1,\\\"fee\\\":\\\"500000\\\",\\\"nrgPrice\\\":\\\"1000000000\\\",\\\"signature\\\":\\\"32mKuVZ+N4ez/14C96/vm3Awe2ll4rru9RjTVl/7UE080+xOa9oL8TPq2Iw1azlHZ1PiOvRVCo3kZPjRfeS+CXHw==\\\"}\",\"updateTime\":1551772844748},{\"eHash\":\"qpq3nLSRgYyv9cMnOwj7tg9nYxj1eRDB/tN4rjfu1uYHBz1UrIS2Fmnv3sSdvDgz\",\"hash\":\"32yC/xq0M6lcH5/uT0IQV7Eu+WlvVsxgUBtHkDLXUM/Dc0qHuCCzcWBxQeUjW+bHtyjaIEr/0gO0090a3FkWfO6g==\",\"id\":\"657\",\"isStable\":true,\"isValid\":true,\"lastIdx\":true,\"message\":\"{\\\"fromAddress\\\":\\\"IT3GOEMWU3M47PSRGXKC7WCULKOTSWFP\\\",\\\"toAddress\\\":\\\"KWNQ6T6RDBITDMEITSFM7R6MCOQALVEV\\\",\\\"amount\\\":\\\"51300000000000000000\\\",\\\"timestamp\\\":1551772883267,\\\"remark\\\":\\\"\\\",\\\"vers\\\":\\\"2.0\\\",\\\"pubkey\\\":\\\"AnQabHLKsnQM7q7Wa5Em0HBZ9O8cDIjTfmp5mQYBgW9W\\\",\\\"type\\\":1,\\\"fee\\\":\\\"500000\\\",\\\"nrgPrice\\\":\\\"1000000000\\\",\\\"signature\\\":\\\"32yC/xq0M6lcH5/uT0IQV7Eu+WlvVsxgUBtHkDLXUM/Dc0qHuCCzcWBxQeUjW+bHtyjaIEr/0gO0090a3FkWfO6g==\\\"}\",\"updateTime\":1551772887531},{\"eHash\":\"JfDpgnweWkCB8w2fHK94SE+p3v0TXZdTOCu7eLJ6Wnfoyx2U1lhf6Jx0UdHsdJi1\",\"hash\":\"323DXohZtvRzee6N5rgFLqCB1jTyj/PYpqxMGWRpfKbBRaxFJH6/KCzau1cLr4s4OFvgNdwQoLtO5Bcf9o4bWtuw==\",\"id\":\"658\",\"isStable\":true,\"isValid\":true,\"lastIdx\":true,\"message\":\"{\\\"fromAddress\\\":\\\"6WV6RFE4ZDWQNLICXHW7QBDPS5GN5EWO\\\",\\\"toAddress\\\":\\\"IT3GOEMWU3M47PSRGXKC7WCULKOTSWFP\\\",\\\"amount\\\":\\\"25274140000000000000000\\\",\\\"timestamp\\\":1551772983083,\\\"remark\\\":\\\"\\\",\\\"vers\\\":\\\"2.0\\\",\\\"pubkey\\\":\\\"A1R324uomNddjHr6LgUUgZQfQ0cSc5QwtmwE6LTFiy+m\\\",\\\"type\\\":1,\\\"fee\\\":\\\"500000\\\",\\\"nrgPrice\\\":\\\"1000000000\\\",\\\"signature\\\":\\\"323DXohZtvRzee6N5rgFLqCB1jTyj/PYpqxMGWRpfKbBRaxFJH6/KCzau1cLr4s4OFvgNdwQoLtO5Bcf9o4bWtuw==\\\"}\",\"updateTime\":1551772987599}],\"offset\":6,\"tableIndex\":0}"}
```



### get transaction message by hash

POST http://$localFullNodeAddress:$localFullNodeHttpPort/v1/gettransaction



#### parameter description

| name | type   | mandatory | parameter description                                  |
| ---- | ------ | --------- | ------------------------------------------------------ |
| hash | string | true      | transaction hash value,namely signature in the message |

example:

```
{
"hash":"32yC/xq0M6lcH5/uT0IQV7Eu+WlvVsxgUBtHkDLXUM/Dc0qHuCCzcWBxQeUjW+bHtyjaIEr/0gO0090a3FkWfO6g=="
}
```

### result

#### http response

| name       | type        | parameter description                                        | value example |
| ---------- | ----------- | ------------------------------------------------------------ | ------------- |
| code       | int         | 200 indicates successful submission while others indicating error arises. | 200           |
| data       | json object | the following items container                                |               |
| eHash      | string      | event hash                                                   |               |
| hash       | string      | message hash                                                 |               |
| id         | string      | consensus id                                                 |               |
| isStable   | boolean     | consensus status: true,consensus completion; false,in consensus process |               |
| isValid    | boolean     | consensus result: true,valid transaction;false,invalid transaction |               |
| lastIdx    | boolean     | whether it is contained in the last event                    |               |
| updateTime | long        | modified time,in milliseconds since 1970                     |               |
| message    | json object | raw message                                                  |               |

response example

```
{
"code": 200,
"data": "{\"eHash\":\"qpq3nLSRgYyv9cMnOwj7tg9nYxj1eRDB/tN4rjfu1uYHBz1UrIS2Fmnv3sSdvDgz\",\"hash\":\"32yC/xq0M6lcH5/uT0IQV7Eu+WlvVsxgUBtHkDLXUM/Dc0qHuCCzcWBxQeUjW+bHtyjaIEr/0gO0090a3FkWfO6g==\",\"id\":\"657\",\"isStable\":true,\"isValid\":true,\"lastIdx\":true,\"message\":\"{\\\"fromAddress\\\":\\\"IT3GOEMWU3M47PSRGXKC7WCULKOTSWFP\\\",\\\"toAddress\\\":\\\"KWNQ6T6RDBITDMEITSFM7R6MCOQALVEV\\\",\\\"amount\\\":\\\"51300000000000000000\\\",\\\"timestamp\\\":1551772883267,\\\"remark\\\":\\\"\\\",\\\"vers\\\":\\\"2.0\\\",\\\"pubkey\\\":\\\"AnQabHLKsnQM7q7Wa5Em0HBZ9O8cDIjTfmp5mQYBgW9W\\\",\\\"type\\\":1,\\\"fee\\\":\\\"500000\\\",\\\"nrgPrice\\\":\\\"1000000000\\\",\\\"signature\\\":\\\"32yC/xq0M6lcH5/uT0IQV7Eu+WlvVsxgUBtHkDLXUM/Dc0qHuCCzcWBxQeUjW+bHtyjaIEr/0gO0090a3FkWfO6g==\\\"}\",\"updateTime\":1551772887531}"
}
```



### submit transaction message to blockchain

POST http://$localFullNodeAddress:$localFullNodeHttpPort/v1/sendmsg


#### parameter description

| name        | type        | mandatory | parameter description                                   |
| ----------- | ----------- | --------- | ------------------------------------------------------- |
| message     | string      | true      | the following fields container                          |
| fromAddress | string      | true      | from which address                                      |
| toAddress   | string      | true      | to which address                                        |
| amount      | big integer | true      | transaction amount in atom unit                         |
| timestamp   | long        | true      | transaction creation timestamp                          |
| remark      | string      | true      | message note                                            |
| vers        | string      | true      | transaction protocol version,"2.0"                      |
| pubkey      | string      | true      | public key                                              |
| type        | integer     | true      | message type,1 means ordinary message                   |
| fee         | big integer | true      | commission charges,BASE_NRG+size(remark)* NRG_PER_KBYTE |
| nrgPrice    | big integer | true      | nrg price                                               |
| signature   | string      | true      | message signature                                       |

example:

```
{
  "message": "{\"fromAddress\":\"IT3GOEMWU3M47PSRGXKC7WCULKOTSWFP\",\"toAddress\":\"KWNQ6T6RDBITDMEITSFM7R6MCOQALVEV\",\"amount\":\"51300000000000000000\",\"timestamp\":1551772883267,\"remark\":\"\",\"vers\":\"2.0\",\"pubkey\":\"AnQabHLKsnQM7q7Wa5Em0HBZ9O8cDIjTfmp5mQYBgW9W\",\"type\":1,\"fee\":\"500000\",\"nrgPrice\":\"1000000000\",\"signature\":\"32yC/xq0M6lcH5/uT0IQV7Eu+WlvVsxgUBtHkDLXUM/Dc0qHuCCzcWBxQeUjW+bHtyjaIEr/0gO0090a3FkWfO6g==\"}"
}
```

### result

#### http response

| name | type    | parameter description                                        | value example |
| ---- | ------- | ------------------------------------------------------------ | ------------- |
| code | integer | 200 indicates successful submission while others indicating error arises. | 200           |
| data | string  | error cause                                                  |               |

response example

```
{
    "code": 200,
    "data": ""
}
```

