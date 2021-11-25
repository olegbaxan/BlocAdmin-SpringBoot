package md.step.BlocAdmin.model;

import javax.persistence.*;

@Entity
@Table(name = "meter_type")

public class MeterDest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer metertypeid;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EMeterDest name;

    public MeterDest() {

    }

    public MeterDest(EMeterDest name) {
        this.name = name;
    }

    public Integer getId() {
        return metertypeid;
    }

    public void setId(Integer id) {
        this.metertypeid = id;
    }

    public EMeterDest getName() {
        return name;
    }

    public void setName(EMeterDest name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MeterDestination{" +
                "metertypeid=" + metertypeid +
                ", name=" + name +
                '}';
    }
}
