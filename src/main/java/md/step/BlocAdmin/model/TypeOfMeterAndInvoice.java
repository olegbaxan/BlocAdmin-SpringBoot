package md.step.BlocAdmin.model;

import javax.persistence.*;

@Entity
@Table(name = "typeofmeterandinvoice")
public class TypeOfMeterAndInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ETypeOfMeterAndInvoice name;

    public TypeOfMeterAndInvoice() {
    }

    public TypeOfMeterAndInvoice(Integer id, ETypeOfMeterAndInvoice name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ETypeOfMeterAndInvoice getName() {
        return name;
    }

    public void setName(ETypeOfMeterAndInvoice name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TypeOfMeterAndInvoice{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }}

