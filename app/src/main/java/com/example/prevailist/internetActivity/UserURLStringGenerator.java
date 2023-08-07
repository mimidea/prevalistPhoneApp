package com.example.prevailist.internetActivity;

public interface UserURLStringGenerator {

    final String startPath = "http://192.168.1.97:0603/api/v1/";

    default String getUser(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(startPath);
        stringBuilder.append("accountRetrieval");
        return stringBuilder.toString();
    }

    default String loginUrlGenerator(String email, String password) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(startPath);
        stringBuilder.append("login?EM=");
        stringBuilder.append(email);
        stringBuilder.append("&PW=");
        stringBuilder.append(password);
        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }

    static String refreshAccessUrlGenerator(String email, String password) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(startPath);
        stringBuilder.append("refreshAccess/?EM=");
        stringBuilder.append(email);
        stringBuilder.append("&PW=");
        stringBuilder.append(password);
        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }

    default String uniqueUserAndEmailCheckerUrlGenerator(String email, String username) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(startPath);
        stringBuilder.append("uniqueParams/?EM=");
        stringBuilder.append(email);
        stringBuilder.append("&UN=");
        stringBuilder.append(username);
        return stringBuilder.toString();
    }

    default String registerDetailsUrlGenerator(String firstName, String lastName, String email, String password, String username, String age) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(startPath);
        stringBuilder.append("register?FN=");
        stringBuilder.append(firstName);
        stringBuilder.append("&LN=");
        stringBuilder.append(lastName);
        stringBuilder.append("&EM=");
        stringBuilder.append(email);
        stringBuilder.append("&PW=");
        stringBuilder.append(password);
        stringBuilder.append("&UN=");
        stringBuilder.append(username);
        stringBuilder.append("&AG=");
        stringBuilder.append(age);
        return stringBuilder.toString();
    }

    default String accountUpdateGenerator(String paramName, String paramValue) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(startPath);
        stringBuilder.append("accountUpdate?PN=");
        stringBuilder.append(paramName);
        stringBuilder.append("&PV=");
        stringBuilder.append(paramValue);
        return stringBuilder.toString();
    }

    default String deleteAccountUrlGenerator() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(startPath);
        stringBuilder.append("accountDeletion");
        return stringBuilder.toString();
    }

    default String getAccountDetailsUrlGenerator() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(startPath);
        stringBuilder.append("accountRetrieval");
        return stringBuilder.toString();
    }



    default String getFriendUrlGenerator(String searchQuery) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(startPath);
        stringBuilder.append("retrieveFriend");
        stringBuilder.append("?SQ=");
        stringBuilder.append(searchQuery);
        return stringBuilder.toString();
    }

    default String getSearchForFriendUrlGenerator(String searchQuery) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(startPath);
        stringBuilder.append("searchForFriend");
        stringBuilder.append("?EM=");
        stringBuilder.append(searchQuery);
        return stringBuilder.toString();
    }

    default String addFriendGroupByEmailUrlGenerator(String email) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(startPath);
        stringBuilder.append("addToFriendGroupByEmail");
        stringBuilder.append("?EM=");
        stringBuilder.append(email);
        return stringBuilder.toString();
    }


}
