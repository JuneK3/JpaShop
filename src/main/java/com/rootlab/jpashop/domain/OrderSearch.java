package com.rootlab.jpashop.domain;

import com.rootlab.jpashop.domain.status.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearch {
    private String memberName;
    private OrderStatus orderStatus;
}