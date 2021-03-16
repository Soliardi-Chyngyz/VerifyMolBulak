package com.chyngyz.verifymolbulak.data;

public class UserFullContact {
    private String name;
    private String password;
    private String phoneNumber;
    private String secretAnswer;

    public UserFullContact(String name, String password, String phoneNumber, String secretAnswer) {
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.secretAnswer = secretAnswer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSecretAnswer() {
        return secretAnswer;
    }

    public void setSecretAnswer(String secretAnswer) {
        this.secretAnswer = secretAnswer;
    }
}
