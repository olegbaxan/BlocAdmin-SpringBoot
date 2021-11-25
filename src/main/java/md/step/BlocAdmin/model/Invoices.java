package md.step.BlocAdmin.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(	name = "invoices",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "invoiceNumber"),

        })
public class Invoices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer invoiceId;

    @NotBlank
    private String invoiceNumber;

    @NotBlank
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(	name = "invoice_status",
            joinColumns = @JoinColumn(name = "invoice_id"),
            inverseJoinColumns = @JoinColumn(name = "status_id"))
    private Status status;


    private Double meterDataCurrent;

    private Double meterDataPrevious;

    @NotBlank
    private Double invoiceSum;

    @NotBlank
    private Double unitPrice;

    @NotBlank
    private LocalDate payTill;

    @NotBlank
    private LocalDate emittedDate;
    private LocalDate dateOfPay;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(	name = "invoice_typeofmeterinvoice",
            joinColumns = @JoinColumn(name = "invoice_id"),
            inverseJoinColumns = @JoinColumn(name = "type_of_meter_invoice_id"))
    private TypeOfMeterInvoice typeOfMeterInvoice = new TypeOfMeterInvoice();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(	name = "invoice_supplier",
            joinColumns = @JoinColumn(name = "invoice_id"),
            inverseJoinColumns = @JoinColumn(name = "supplier_id"))
    private Suppliers supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(	name = "invoice_meter",
            joinColumns = @JoinColumn(name = "invoice_id"),
            inverseJoinColumns = @JoinColumn(name = "meter_id"))
    private Meters meter;
    private String invoiceFileId ;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "invoice_building",
            joinColumns = @JoinColumn(name = "invoice_id"),
            inverseJoinColumns = @JoinColumn(name = "buildings_id"))
    private Set<Buildings> buildings = new HashSet<>();

    public Invoices() {
    }

    public Invoices(Integer invoiceId, String invoiceNumber, Status status, Double meterDataCurrent, Double meterDataPrevious, Double invoiceSum, Double unitPrice, LocalDate payTill, LocalDate emittedDate, LocalDate dateOfPay, TypeOfMeterInvoice typeOfMeterInvoice, Suppliers supplier, Meters meter, String invoiceFileId, Set<Buildings> buildings) {
        this.invoiceId = invoiceId;
        this.invoiceNumber = invoiceNumber;
        this.status = status;
        this.meterDataCurrent = meterDataCurrent;
        this.meterDataPrevious = meterDataPrevious;
        this.invoiceSum = invoiceSum;
        this.unitPrice = unitPrice;
        this.payTill = payTill;
        this.emittedDate = emittedDate;
        this.dateOfPay = dateOfPay;
        this.typeOfMeterInvoice = typeOfMeterInvoice;
        this.supplier = supplier;
        this.meter = meter;
        this.invoiceFileId = invoiceFileId;
        this.buildings = buildings;
    }

    public Set<Buildings> getBuildings() {
        return buildings;
    }

    public void setBuildings(Set<Buildings> buildings) {
        this.buildings = buildings;
    }

    public String getInvoiceFileId() {
        return invoiceFileId;
    }

    public void setInvoiceFileId(String invoiceFileId) {
        this.invoiceFileId = invoiceFileId;
    }

    public TypeOfMeterInvoice getTypeOfMeterInvoice() {
        return typeOfMeterInvoice;
    }

    public void setTypeOfMeterInvoice(TypeOfMeterInvoice typeOfMeterAndInvoice) {
        this.typeOfMeterInvoice = typeOfMeterAndInvoice;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Double getMeterDataCurrent() {
        return meterDataCurrent;
    }

    public void setMeterDataCurrent(Double meterDataCurrent) {
        this.meterDataCurrent = meterDataCurrent;
    }

    public Double getMeterDataPrevious() {
        return meterDataPrevious;
    }

    public void setMeterDataPrevious(Double meterDataPrevious) {
        this.meterDataPrevious = meterDataPrevious;
    }

    public Double getInvoiceSum() {
        return invoiceSum;
    }

    public void setInvoiceSum(Double invoiceSum) {
        this.invoiceSum = invoiceSum;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public LocalDate getPayTill() {
        return payTill;
    }

    public void setPayTill(LocalDate payTill) {
        this.payTill = payTill;
    }

    public LocalDate getEmittedDate() {
        return emittedDate;
    }

    public void setEmittedDate(LocalDate emittedDate) {
        this.emittedDate = emittedDate;
    }

    public LocalDate getDateOfPay() {
        return dateOfPay;
    }

    public void setDateOfPay(LocalDate dateOfPay) {
        this.dateOfPay = dateOfPay;
    }

    public Suppliers getSupplier() {
        return supplier;
    }

    public void setSupplier(Suppliers supplier) {
        this.supplier = supplier;
    }

    public Meters getMeter() {
        return meter;
    }

    public void setMeter(Meters meter) {
        this.meter = meter;
    }

    @Override
    public String toString() {
        return "Invoices{" +
                "invoiceId=" + invoiceId +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", status=" + status +
                ", meterDataCurrent=" + meterDataCurrent +
                ", meterDataPrevious=" + meterDataPrevious +
                ", invoiceSum=" + invoiceSum +
                ", unitPrice=" + unitPrice +
                ", payTill=" + payTill +
                ", emittedDate=" + emittedDate +
                ", dateOfPay=" + dateOfPay +
                ", typeOfMeterInvoice=" + typeOfMeterInvoice +
                ", supplier=" + supplier +
                ", meter=" + meter +
                ", invoiceFileId='" + invoiceFileId + '\'' +
                ", buildings=" + buildings +
                '}';
    }
}
