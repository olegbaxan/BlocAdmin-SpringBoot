package md.step.BlocAdmin.model;

import javax.persistence.*;

@Entity
@Table(name = "meter_dest")

public class MeterType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer metertypeid;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EMeterType name;

    public MeterType() {

    }

    public MeterType(EMeterType name) {
        this.name = name;
    }

    public Integer getId() {
        return metertypeid;
    }

    public void setId(Integer id) {
        this.metertypeid = id;
    }

    public EMeterType getName() {
        return name;
    }

    public void setName(EMeterType name) {
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
