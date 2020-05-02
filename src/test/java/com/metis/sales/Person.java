package com.metis.sales;

import java.util.List;

public class Person {
    private String given;
    private String family;
    private List<Address> address;

    public Person() {}

    public Person(String given, String family, List<Address> address) {
        this.given = given;
        this.family = family;
        this.address = address;
    }

    public String getGiven() {
        return given;
    }

    public void setGiven(String given) {
        this.given = given;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Person{" +
            "given='" + given + '\'' +
            ", family='" + family + '\'' +
            ", address=" + address +
            '}';
    }
}
