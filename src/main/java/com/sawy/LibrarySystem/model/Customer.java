package com.sawy.LibrarySystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Address is mandatory")
    private String address;

    @Pattern(regexp = "^01[0125]\\d{8}$", message = "Phone number is invalid")
    private String phoneNumber;

    // @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) ensures that the field is only written during deserialization,
    // meaning it will not be included in the JSON response when retrieving data but can be set when sending data to the server.
    @NotBlank(message = "Password is mandatory")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"customer", "book"})
    private List<BorrowingRecord> borrowingRecords;

    public Customer(String name, String email, String address, String phoneNumber, String password) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }
}
