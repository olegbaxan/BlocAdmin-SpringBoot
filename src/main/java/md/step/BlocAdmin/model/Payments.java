package md.step.BlocAdmin.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
@Table(	name = "payments")
public class Payments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(	name = "payments_person",
            joinColumns = @JoinColumn(name = "payment_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id"))
    private Person person = new Person();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(	name = "payments_flat",
            joinColumns = @JoinColumn(name = "payment_id"),
            inverseJoinColumns = @JoinColumn(name = "flat_id"))
    private Flats flat = new Flats();

    @NotBlank
    private LocalDate payDate;

    @NotBlank
    private double sum;

    public Payments() {
    }

    public Payments(Integer paymentid, Person person, Flats flat, LocalDate payDate, double sum) {
        this.paymentid = paymentid;
        this.person = person;
        this.flat = flat;
        this.payDate = payDate;
        this.sum = sum;
    }

    public Integer getPaymentid() {
        return paymentid;
    }

    public void setPaymentid(Integer paymentid) {
        this.paymentid = paymentid;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Flats getFlat() {
        return flat;
    }

    public void setFlat(Flats flat) {
        this.flat = flat;
    }

    public LocalDate getPayDate() {
        return payDate;
    }

    public void setPayDate(LocalDate payDate) {
        this.payDate = payDate;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "Payments{" +
                "paymentid=" + paymentid +
                ", person=" + person +
                ", flat=" + flat +
                ", payDate=" + payDate +
                ", sum=" + sum +
                '}';
    }
}
