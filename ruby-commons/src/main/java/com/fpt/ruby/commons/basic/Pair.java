package com.fpt.ruby.commons.basic;

public class Pair<F,S> {
    public F first;
    public S second;

    public Pair() {
        first = null;
        second = null;
    }

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public boolean equals(Pair<F,S> other) {
        return first.equals(other.first) && second.equals(other.second);
    }

}
