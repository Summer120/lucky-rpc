package rpc.test.order.impl;

import org.springframework.beans.factory.annotation.Autowired;
import rpc.test.order.iface.OrderInfoInterface;

/**
 * @Author:chaoqiang.zhou
 * @Description:
 * @Date:Create in 8:57 2017/6/20
 */
public class OrderInfoImpl implements OrderInfoInterface {

    @Autowired
    private OrderInfoInterface infoInterface;

    @Override
    public String getOrderInfo(String orderId) {
        System.out.println(orderId);
        return orderId;
    }
}
