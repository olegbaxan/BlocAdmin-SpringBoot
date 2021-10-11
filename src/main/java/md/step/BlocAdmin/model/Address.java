package md.step.BlocAdmin.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Address implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer addressid;
    @Column(length = 20)
    private String city;
    @Column(length = 20)
    private String raion;
    @Column(length = 40)
    private String street;
    @Column(length = 10)
    private String houseNumber;

    public Address() {
    }

    public Address(Integer addressid, String city, String raion, String street, String houseNumber) {
        this.addressid = addressid;
        this.city = city;
        this.raion = raion;
        this.street = street;
        this.houseNumber = houseNumber;
    }

    public Integer getAddressid() {
        return addressid;
    }

    public void setAddressid(Integer addressid) {
        this.addressid = addressid;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRaion() {
        return raion;
    }

    public void setRaion(String raion) {
        this.raion = raion;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressid=" + addressid +
                ", city='" + city + '\'' +
                ", raion='" + raion + '\'' +
                ", street='" + street + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                '}';
    }
}
