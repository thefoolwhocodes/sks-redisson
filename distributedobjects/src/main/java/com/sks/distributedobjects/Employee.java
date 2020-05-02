package com.sks.distributedobjects;

public class Employee {
    private static final long serialVersionUID = 1L;
    private String name;
    private int age;
    private String location;

    /*
     * Needed for JsonJacksonCodec
     * Caused by: com.fasterxml.jackson.databind.exc.InvalidDefinitionException
     */
    Employee () {}

    Employee (String name, int age, String location) {
        this.name = name;
        this.age = age;
        this.location = location;
    }

    public void Display() {
        System.out.println("Name: " + name + " Age: " + age + " Location: " + location);
    }

    public boolean equals(Object obj) {
        if (obj!= null && obj instanceof Employee) {
            Employee emp = (Employee) obj;
            return this.name.equals(emp.name) &&
                    this.age == emp.age &&
                    this.location.equals(emp.location);
        }
        return false;
    }
}
