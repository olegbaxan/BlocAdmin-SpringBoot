package md.step.BlocAdmin.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
@Table(	name = "withdrawn")
public class Withdrawn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer withdrawnid;

    @NotBlank
    private LocalDate withdrawnDate;

    @NotBlank
    private double withdrawnSum;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(	name = "withdrawn_invoice",
            joinColumns = @JoinColumn(name = "withdrawn_id"),
            inverseJoinColumns = @JoinColumn(name = "invoice_id"))
    private Invoices invoice = new Invoices();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(	name = "withdrawns_flat",
            joinColumns = @JoinColumn(name = "withdrawn_id"),
            inverseJoinColumns = @JoinColumn(name = "flat_id"))
    private Flats flat = new Flats();

    public Withdrawn() {
    }

    public Withdrawn(Integer withdrawnid, LocalDate withdrawnDate, double withdrawnSum, Invoices invoice, Flats flat) {
        this.withdrawnid = withdrawnid;
        this.withdrawnDate = withdrawnDate;
        this.withdrawnSum = withdrawnSum;
        this.invoice = invoice;
        this.flat = flat;
    }

    public Flats getFlat() {
        return flat;
    }

    public void setFlat(Flats flat) {
        this.flat = flat;
    }

    public Integer getWithdrawnid() {
        return withdrawnid;
    }

    public void setWithdrawnid(Integer withdrawnid) {
        this.withdrawnid = withdrawnid;
    }

    public LocalDate getWithdrawnDate() {
        return withdrawnDate;
    }

    public void setWithdrawnDate(LocalDate withdrawnDate) {
        this.withdrawnDate = withdrawnDate;
    }

    public double getWithdrawnSum() {
        return withdrawnSum;
    }

    public void setWithdrawnSum(double withdrawnSum) {
        this.withdrawnSum = withdrawnSum;
    }

    public Invoices getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoices invoice) {
        this.invoice = invoice;
    }

    @Override
    public String toString() {
        return "Withdrawn{" +
                "withdrawnid=" + withdrawnid +
                ", withdrawnDate=" + withdrawnDate +
                ", withdrawnSum=" + withdrawnSum +
                ", invoice=" + invoice +
                ", flat=" + flat +
                '}';
    }
}
