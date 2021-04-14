package com.github.adrian678.forum.forumapp.domain;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static PageRequest createPageRequestFromParams(int page, int limit, String sort){
        if(null == sort || "" == sort){
            return PageRequest.of(page, limit);
        }
        String[] sortOrders = sort.split("&");
        List<Sort.Order> orders = new ArrayList<>();
        for(String s : sortOrders){
            String[] pieces = s.split(",");
            String[] test = "test".split(",");
            System.out.println("test split has " + test.length + " length");
            Sort.Direction direction;
            //case where user did not provide a sort direction
            direction = (pieces.length == 1) ? Sort.Direction.ASC : Sort.Direction.fromString(pieces[pieces.length - 1]);
            String property = pieces[0];
            orders.add(new Sort.Order(direction, property));
        }
        if(orders.isEmpty()){
            return null;
        }
        return PageRequest.of(page, limit, Sort.by(orders));

    }


}
