package md.step.BlocAdmin.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "meters",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "serial"),
                @UniqueConstraint(columnNames = "initialValue"),
        })
public class Meters {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer meterid;

        @OneToOne(fetch = FetchType.LAZY)
        @JoinTable(	name = "meter_metertype",
                joinColumns = @JoinColumn(name = "meter_id"),
                inverseJoinColumns = @JoinColumn(name = "meter_type_id"))
        private MeterType meterType = new MeterType();

        @OneToOne(fetch = FetchType.LAZY)
        @JoinTable(	name = "meter_typeofmeterinvoice",
                joinColumns = @JoinColumn(name = "meter_id"),
                inverseJoinColumns = @JoinColumn(name = "type_of_meter_invoice_id"))
        private TypeOfMeterAndInvoice typeOfMeterAndInvoice = new TypeOfMeterAndInvoice();

        @Size(max = 30)
        private String serial;

        private Double initialValue;

        @NotBlank
        @OneToOne(fetch = FetchType.LAZY)
        @JoinTable(	name = "meter_supplier",
                joinColumns = @JoinColumn(name = "meter_id"),
                inverseJoinColumns = @JoinColumn(name = "supplier_id"))
        private Suppliers supplier;



        @OneToOne(fetch = FetchType.LAZY)
        @JoinTable(	name = "meter_flat",
                joinColumns = @JoinColumn(name = "meter_id"),
                inverseJoinColumns = @JoinColumn(name = "flat_id"))
        private Flats flat;

        @NotBlank
        @OneToOne(fetch = FetchType.LAZY)
        @JoinTable(	name = "meter_contractperson",
                joinColumns = @JoinColumn(name = "meter_id"),
                inverseJoinColumns = @JoinColumn(name = "contractperson_id"))
        private Person person;

        public Meters() {
        }

        public Meters(Integer meterid, MeterType meterType, TypeOfMeterAndInvoice typeOfMeterAndInvoice, String serial, Double initialValue, Suppliers supplier, Flats flat, Person person) {
                this.meterid = meterid;
                this.meterType = meterType;
                this.typeOfMeterAndInvoice = typeOfMeterAndInvoice;
                this.serial = serial;
                this.initialValue = initialValue;
                this.supplier = supplier;
                this.flat = flat;
                this.person = person;
        }

        public Integer getMeterid() {
                return meterid;
        }

        public void setMeterid(Integer meterid) {
                this.meterid = meterid;
        }

        public TypeOfMeterAndInvoice getTypeOfMeterAndInvoice() {
                return typeOfMeterAndInvoice;
        }

        public void setTypeOfMeterAndInvoice(TypeOfMeterAndInvoice typeOfMeterAndInvoice) {
                this.typeOfMeterAndInvoice = typeOfMeterAndInvoice;
        }

        public Integer getMeterId() {
                return meterid;
        }

        public void setMeterId(Integer meterid) {
                this.meterid = meterid;
        }

        public MeterType getMeterType() {
                return meterType;
        }

        public void setMeterType(MeterType meterType) {
                this.meterType = meterType;
        }

        public String getSerial() {
                return serial;
        }

        public void setSerial(String serial) {
                this.serial = serial;
        }

        public Double getInitialValue() {
                return initialValue;
        }

        public void setInitialValue(Double initialValue) {
                this.initialValue = initialValue;
        }

        public Suppliers getSupplier() {
                return supplier;
        }

        public void setSupplier(Suppliers supplier) {
                this.supplier = supplier;
        }

        public Flats getFlat() {
                return flat;
        }

        public void setFlat(Flats flat) {
                this.flat = flat;
        }

        public Person getPerson() {
                return person;
        }

        public void setPerson(Person person) {
                this.person = person;
        }

        @Override
        public String toString() {
                return "Meters{" +
                        "meterid=" + meterid +
                        ", meterType=" + meterType +
                        ", typeOfMeterAndInvoice=" + typeOfMeterAndInvoice +
                        ", serial='" + serial + '\'' +
                        ", initialValue=" + initialValue +
                        ", supplier=" + supplier +
                        ", flat=" + flat +
                        ", person=" + person +
                        '}';
        }
}
