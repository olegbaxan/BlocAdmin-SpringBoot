//package md.step.BlocAdmin.model;
//
//import javax.persistence.*;
//
//@Entity
//public class Supplier {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer supplierId;
//    @Column(length = 50)
//    private String supplierName;
//    @Column(length = 25)
//    private String IBAN;
//    @Column(length = 20)
//    private String fiscalCode;
//    private String bankCode;
//    private String details;
//
//    @OneToOne
//    private Address address;
//
//
//    public Integer getSupplierId() {
//        return supplierId;
//    }
//
//    public void setSupplierId(Integer supplierId) {
//        this.supplierId = supplierId;
//    }
//
//    public String getSupplierName() {
//        return supplierName;
//    }
//
//    public void setSupplierName(String supplierName) {
//        this.supplierName = supplierName;
//    }
//
//    public String getIBAN() {
//        return IBAN;
//    }
//
//    public void setIBAN(String IBAN) {
//        this.IBAN = IBAN;
//    }
//
//    public String getFiscalCode() {
//        return fiscalCode;
//    }
//
//    public void setFiscalCode(String fiscalCode) {
//        this.fiscalCode = fiscalCode;
//    }
//
//    public String getBankCode() {
//        return bankCode;
//    }
//
//    public void setBankCode(String bankCode) {
//        this.bankCode = bankCode;
//    }
//
//    public String getDetails() {
//        return details;
//    }
//
//    public void setDetails(String details) {
//        this.details = details;
//    }
//
//    public Address getAddress() {
//        return address;
//    }
//
//    public void setAddress(Address address) {
//        this.address = address;
//    }
//}
