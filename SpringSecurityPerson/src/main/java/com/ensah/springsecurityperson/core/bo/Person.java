package com.ensah.springsecurityperson.core.bo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPersonne;

    @NotBlank(message = "This field is required")
    @Pattern(regexp = "^[A-Z]{2}[0-9]{8}", message = "The National ID must be 2 upper letters followed by  8 digits")
    private String nationalIdNumber;

    @NotBlank(message = "This field is required")
    private String firstName;

    @NotBlank(message = "This field is required")
    private String lastName;

    @Min(value = 20, message = "Age must be > 19")
    @Max(value = 90, message = "Age must be < 91")
    @NotNull(message = "This field is required")
    private int age;

    @Email(message = "Enter a valid email")
    @NotBlank(message = "This field is required")
    private String email;

    @NotBlank(message = "This field is required")
    private String address;

    @NotBlank(message = "This field is required")
    private String state;

    @NotEmpty(message = "Choose at least one community")
    @ElementCollection
    @OrderColumn(name = "pos")
    private String[] community;

    @NotBlank(message = "This field is required")
    private String gender;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<UserAccount> accounts;

    public Long getIdPersonne() {
        return idPersonne;
    }

    @Override
    public String toString() {
        return "Person [idPersonne=" + idPersonne + ", nationalIdNumber=" + nationalIdNumber + ", firstName="
                + firstName + ", lastName=" + lastName + ", age=" + age + ", email=" + email + ", address=" + address
                + ", state=" + state + ", community=" + community + ", gender=" + gender + "]";
    }

    public void setIdPersonne(Long idPersonne) {
        this.idPersonne = idPersonne;
    }

    public String getNationalIdNumber() {
        return nationalIdNumber;
    }

    public void setNationalIdNumber(String nationalIdNumber) {
        this.nationalIdNumber = nationalIdNumber;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String[] getCommunity() {
        return community;
    }

    public void setCommunity(String[] community) {
        this.community = community;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}
