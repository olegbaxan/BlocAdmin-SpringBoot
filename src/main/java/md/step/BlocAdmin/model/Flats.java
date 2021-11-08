package md.step.BlocAdmin.model;

import com.sun.istack.Nullable;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(	name = "flats")
public class Flats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer flatid;

    private Integer floor;

    private Integer flatNumber;

    private byte numberOfPerson;
    private Integer ladder;
    private double wallet;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "flats_persons",
            joinColumns = @JoinColumn(name = "flat_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id"))
    private Set<Person> person = new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(	name = "flats_building",
            joinColumns = @JoinColumn(name = "flat_id"),
            inverseJoinColumns = @JoinColumn(name = "building_id"))
    private Buildings building;

    @Nullable
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "flats_meters",
            joinColumns = @JoinColumn(name = "flat_id"),
            inverseJoinColumns = @JoinColumn(name = "meter_id"))
    private Set<Meters> meters;

    public Flats() {
    }

    public Flats(Integer flatid, Integer floor, Integer flatNumber, byte numberOfPerson, Integer ladder, double wallet, Set<Person> person, Buildings building, Set<Meters> meters) {
        this.flatid = flatid;
        this.floor = floor;
        this.flatNumber = flatNumber;
        this.numberOfPerson = numberOfPerson;
        this.ladder = ladder;
        this.wallet = wallet;
        this.person = person;
        this.building = building;
        this.meters = meters;
    }

    public Integer getFlatid() {
        return flatid;
    }

    public void setFlatid(Integer flatid) {
        this.flatid = flatid;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(Integer flatNumber) {
        this.flatNumber = flatNumber;
    }

    public byte getNumberOfPerson() {
        return numberOfPerson;
    }

    public void setNumberOfPerson(byte numberOfPerson) {
        this.numberOfPerson = numberOfPerson;
    }

    public Integer getLadder() {
        return ladder;
    }

    public void setLadder(Integer ladder) {
        this.ladder = ladder;
    }

    public double getWallet() {
        return wallet;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }

    public Set<Person> getPerson() {
        return person;
    }

    public void setPerson(Set<Person> person) {
        this.person = person;
    }

    public Buildings getBuilding() {
        return building;
    }

    public void setBuilding(Buildings building) {
        this.building = building;
    }

    public Set<Meters> getMeters() {
        return meters;
    }
    public void setMeters(Set<Meters> meters) {
        this.meters = meters;
    }

    @Override
    public String toString() {
        return "Flats{" +
                "flatid=" + flatid +
                ", floors=" + floor +
                ", flatNumbers=" + flatNumber +
                ", numberOfPerson=" + numberOfPerson +
                ", ladders=" + ladder +
                ", wallet=" + wallet +
                ", person=" + person +
                ", building=" + building +
                ", meters=" + meters +
                '}';
    }
}
