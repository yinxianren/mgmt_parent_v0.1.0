package com.rxh.mapper.square;

import com.rxh.square.pojo.Customer;

import java.util.List;

public interface CustomerMapper {

    List<Customer> getMerChants();
    List<Customer> getAgents();


}