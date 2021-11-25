package md.step.BlocAdmin.model;

import javax.persistence.*;

@Entity
@Table(name = "typeofmeterinvoice")
public class TypeOfMeterInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ETypeOfMeterInvoice name;

    public TypeOfMeterInvoice() {
    }

    public TypeOfMeterInvoice(Integer id, ETypeOfMeterInvoice name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ETypeOfMeterInvoice getName() {
        return name;
    }

    public void setName(ETypeOfMeterInvoice name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TypeOfMeterAndInvoice{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }}

