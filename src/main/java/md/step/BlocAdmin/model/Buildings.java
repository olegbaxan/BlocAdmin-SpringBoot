package md.step.BlocAdmin.model;


import javax.persistence.*;

@Entity
@Table(	name = "buildings")
public class Buildings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer buildingid;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(	name = "building_address",
            joinColumns = @JoinColumn(name = "building_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id"))
    private Address address;


    private Integer floors;

    private Integer flats;

    public Buildings() {
    }

    public Buildings(Integer buildingid, Address address, Integer floors, Integer flats) {
        this.buildingid = buildingid;
        this.address = address;
        this.floors = floors;
        this.flats = flats;
    }

    public Integer getBuildingid() {
        return buildingid;
    }

    public void setBuildingid(Integer buildingid) {
        this.buildingid = buildingid;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Integer getFloors() {
        return floors;
    }

    public void setFloors(Integer floors) {
        this.floors = floors;
    }

    public Integer getFlats() {
        return flats;
    }

    public void setFlats(Integer flats) {
        this.flats = flats;
    }

    @Override
    public String toString() {
        return "Buildings{" +
                "buildingid=" + buildingid +
                ", address=" + address +
                ", floors=" + floors +
                ", flats=" + flats +
                '}';
    }
}
