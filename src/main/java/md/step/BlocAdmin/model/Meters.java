package md.step.BlocAdmin.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "meters",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "serial"),
        })
public class Meters {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer meterid;

        @OneToOne(fetch = FetchType.LAZY)
        @JoinTable(	name = "meter_meterdest",
                joinColumns = @JoinColumn(name = "meter_id"),
                inverseJoinColumns = @JoinColumn(name = "meter_dest_id"))
        private MeterDest meterDest ;

        @OneToOne(fetch = FetchType.LAZY)
        @JoinTable(	name = "meter_typeofmeterinvoice",
                joinColumns = @JoinColumn(name = "meter_id"),
                inverseJoinColumns = @JoinColumn(name = "type_of_meter_invoice_id"))
        private TypeOfMeterInvoice typeOfMeterInvoice ;
//        = new TypeOfMeterInvoice();

        @Size(max = 30)
        private String serial;

        private Double initialValue;

        @NotBlank
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinTable(	name = "meter_supplier",
                joinColumns = @JoinColumn(name = "meter_id"),
                inverseJoinColumns = @JoinColumn(name = "supplier_id"))
        private Suppliers supplier;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinTable(	name = "meter_flat",
                joinColumns = @JoinColumn(name = "meter_id"),
                inverseJoinColumns = @JoinColumn(name = "flat_id"))
        private Flats flat;

        @OneToOne(fetch = FetchType.LAZY)
        @JoinTable(	name = "meter_building",
                joinColumns = @JoinColumn(name = "meter_id"),
                inverseJoinColumns = @JoinColumn(name = "building_id"))
        private Buildings building;


        @NotBlank
        @OneToOne(fetch = FetchType.LAZY)
        @JoinTable(	name = "meter_contractperson",
                joinColumns = @JoinColumn(name = "meter_id"),
                inverseJoinColumns = @JoinColumn(name = "contractperson_id"))
        private Person person;

        public Meters() {
        }

        public Meters(Integer meterid, MeterDest meterDest, TypeOfMeterInvoice typeOfMeterInvoice, String serial, Double initialValue, Suppliers supplier, Flats flat, Buildings building, Person person) {
                this.meterid = meterid;
                this.meterDest = meterDest;
                this.typeOfMeterInvoice = typeOfMeterInvoice;
                this.serial = serial;
                this.initialValue = initialValue;
                this.supplier = supplier;
                this.flat = flat;
                this.building = building;
                this.person = person;
        }

        public Integer getMeterid() {
                return meterid;
        }

        public void setMeterid(Integer meterid) {
                this.meterid = meterid;
        }

        public Buildings getBuilding() {
                return building;
        }

        public void setBuilding(Buildings building) {
                this.building = building;
        }

        public Integer getMeterId() {
                return meterid;
        }

        public void setMeterId(Integer meterid) {
                this.meterid = meterid;
        }

        public MeterDest getMeterDest() {
                return meterDest;
        }

        public void setMeterDest(MeterDest meterDest) {
                this.meterDest = meterDest;
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

        public void setTypeOfMeterInvoice(TypeOfMeterInvoice typeOfMeterInvoice) {
                this.typeOfMeterInvoice = typeOfMeterInvoice;
        }
        public TypeOfMeterInvoice getTypeOfMeterInvoice() {
                return typeOfMeterInvoice;
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
                        ", meterDest=" + meterDest +
                        ", typeOfMeterInvoice=" + typeOfMeterInvoice +
                        ", serial='" + serial + '\'' +
                        ", initialValue=" + initialValue +
                        ", supplier=" + supplier +
                        ", flat=" + flat +
                        ", building=" + building +
                        ", person=" + person +
                        '}';
        }
}
