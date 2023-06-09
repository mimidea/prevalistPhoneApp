package com.example.prevailist.logIn_SignIn;

public class CreateAccounts{
        private String username;
        private String firstName;
        private String lastName;
        private int age;
        private String email;
        private String password;

        public CreateAccounts(String username, String firstName, String lastName, int age, String email, String password) {
            this.username = username;
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
            this.email = email;
            this.password = password;
        }


        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public static boolean validatePassword(String password, String confirmPassword) {
            return password.equals(confirmPassword);
        }
    }


