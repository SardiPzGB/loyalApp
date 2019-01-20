package ru.vlad.loyalty.util;

import lombok.Data;

@Data
class Box {
    private double num = 0;
    private long den = 0;

    void combine (Box that){
        this.num += that.num;
        this.den += that.den;
    }
}
