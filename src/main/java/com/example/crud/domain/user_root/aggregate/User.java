package com.example.crud.domain.user_root.aggregate;

import com.example.crud.domain.common.DateTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Entity
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor
@Getter
@Builder
@Table(name = "users")
public class User extends DateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    public static User create(String name, String email, String password) {
        return User.builder()
                .name(name)
                .email(email)
                .password(password)
                .build();
    }

    public void update(String name, String password){
        if(name != null){
            this.name = name;
        }
        if(password != null){
            this.password = password;
        }
    }


}
