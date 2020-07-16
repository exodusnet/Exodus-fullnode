package org.exodus.bean.message;

/**
 * 消息类型枚举类型
 * @author Clare
 * @date   2018/8/31 0031.
 */
public enum MessageType {
    TRANSACTIONS(1),
    CONTRACT(2),
    SNAPSHOT(3),
    TEXT(4);

    private int index ;

    MessageType(int index){
        this.index = index ;
    }

    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "" + index;
    }


}
