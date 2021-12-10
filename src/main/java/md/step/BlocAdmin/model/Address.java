package md.step.BlocAdmin.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(	name = "address",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"city","raion","street","houseNumber","entranceNo"})
        })
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
    private Integer entranceNo;

    public Address() {
    }

    public Address(Integer addressid, String city, String raion, String street, String houseNumber, Integer entranceNo) {
        this.addressid = addressid;
        this.city = city;
        this.raion = raion;
        this.street = street;
        this.houseNumber = houseNumber;
        this.entranceNo = entranceNo;
    }

    public Integer getEntranceNo() {
        return entranceNo;
    }

    public void setEntranceNo(Integer entranceNo) {
        this.entranceNo = entranceNo;
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
                ", entranceNo=" + entranceNo +
                '}';
    }

    //    public String toString() {
//        return "Tutorial [id=" + id + ", title=" + title + ", desc=" + description + ", published=" + published + "]";
//    }

}
