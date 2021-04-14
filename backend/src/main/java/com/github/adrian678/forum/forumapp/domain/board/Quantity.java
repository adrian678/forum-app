package com.github.adrian678.forum.forumapp.domain.board;


import java.io.Serializable;

public class Quantity implements Serializable {
    private final int quantity;

    private Quantity(int quantity){
        this.quantity = quantity;
    }
    public static Quantity of(int n){
        if(n < 0){
            throw new IllegalArgumentException();
        }
        return new Quantity(n);
    }

    public Quantity add(int num){
        if(num < 0){
            throw new IllegalArgumentException("Only ints >= 0 are acceptable input");
        }
        return  new Quantity(quantity + num);
    }

    public Quantity subtract(int num){
        if(num < 0){
            throw new IllegalArgumentException("Only ints >= 0 are acceptable input");
        }
        if(num > quantity){
            throw new IllegalArgumentException("Cannot subtract a number greater than itself");
        }
        return  new Quantity(quantity - num);
    }

    public int getQuantity() {
        return quantity;
    }
}
