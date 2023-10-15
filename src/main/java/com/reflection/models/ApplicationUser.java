package com.reflection.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user")
public class ApplicationUser {
    @Id
    private String _id ;

    private String name;
    private String email;
    private int age;
    private static String gender;
    private List<String> contactNumber;
    private static String password;
    private Date sqlDate = new Date() ;
    private String accountStatus = "active";
    private boolean isEmailVerified;
    private boolean isPhoneVerified;

    private String getName(String userName) {
        this.name = userName;
        System.out.println("I am private method" + name);
        return name;
    }

    private static String getPassword() {
        System.out.println("I am private and static method");
        return password;
    }

    public String getEmail() {
        System.out.println("I am public method");
        return email;
    }

    public static String getGender() {
        System.out.println("I am public and static method");
        return gender;
    }
}
