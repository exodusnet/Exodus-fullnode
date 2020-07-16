package org.exodus.bean.wallet;

/**
 * @Author: zz
 * @Description: 钱包地址
 * @Date: 下午 5:32 2018/9/17 0017
 * @Modified By
 */
public class Address {

    private String address;

    public Address(String address) {
        this.address = address;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
