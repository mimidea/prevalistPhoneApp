package com.example.prevailist;

public class Friend {

    private final String username;
    private final String firstName;
    private final String lastName;
    int age;
    String email;

    public Friend(String username, String firstName, String lastName){
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Friend(String username, String firstName, String lastName, int age, String email){
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }
}
