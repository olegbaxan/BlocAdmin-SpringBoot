package md.step.BlocAdmin.model;


import com.fasterxml.jackson.annotation.JsonAutoDetect;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
//https://www.baeldung.com/jackson-jsonmappingexception
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Table(	name = "suppliers",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "supplierName"),
                @UniqueConstraint(columnNames = "IBAN"),
                @UniqueConstraint(columnNames = "fiscalCode"),
                @UniqueConstraint(columnNames = "bankCode"),
        })
public class Suppliers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer supplierid;

    @NotBlank
    @Size(max = 50,min = 3)
    private String supplierName;

    @NotBlank
    @Size(max = 24,min = 24)
    private String IBAN;

    @NotBlank
    @Size(max = 30)
    private String fiscalCode;

    @NotBlank
    @Size(max = 30)
    private String bankCode;

    @Size(max = 100)
    private String details;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(	name = "supplier_address",
            joinColumns = @JoinColumn(name = "supplier_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id"))
    private Address address;



    private EMeterDest meterDestination;

    public Suppliers() {
    }

    public Suppliers(Integer supplierid, String supplierName, String IBAN, String fiscalCode, String bankCode, String details, Address address) {
        this.supplierid = supplierid;
        this.supplierName = supplierName;
        this.IBAN = IBAN;
        this.fiscalCode = fiscalCode;
        this.bankCode = bankCode;
        this.details = details;
        this.address = address;
    }

    public Integer getSupplierId() {
        return supplierid;
    }

    public void setSupplierId(Integer supplierid) {
        this.supplierid = supplierid;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public EMeterDest getMeterDestination() {
        return meterDestination;
    }

    public void setMeterDestination(EMeterDest meterDestination) {
        this.meterDestination = meterDestination;
    }

    @Override
    public String toString() {
        return "Suppliers{" +
                "supplierId=" + supplierid +
                ", supplierName='" + supplierName + '\'' +
                ", IBAN='" + IBAN + '\'' +
                ", fiscalCode='" + fiscalCode + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", details='" + details + '\'' +
                ", address=" + address +
                ", meterDestination=" + meterDestination +
                '}';
    }
}
