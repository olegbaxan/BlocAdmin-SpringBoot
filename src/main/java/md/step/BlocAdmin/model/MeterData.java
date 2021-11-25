package md.step.BlocAdmin.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(	name = "meterdata")
public class MeterData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer meterdataid;

    @NotBlank
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(	name = "meterdata_meter",
            joinColumns = @JoinColumn(name = "meterdata_id"),
            inverseJoinColumns = @JoinColumn(name = "meter_id"))
    private Meters meter;

    private Double previousValue;

    private Double currentValue;

    private Double meterValue;

    private Double meterSum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(	name = "meterdata_status",
            joinColumns = @JoinColumn(name = "meterdata_id"),
            inverseJoinColumns = @JoinColumn(name = "status_id"))
    private Status status;

    public MeterData() {
    }

    public MeterData(Integer meterdataid, Meters meter, Double previousValue, Double currentValue, Double meterValue, Double meterSum, Status status) {
        this.meterdataid = meterdataid;
        this.meter = meter;
        this.previousValue = previousValue;
        this.currentValue = currentValue;
        this.meterValue = meterValue;
        this.meterSum = meterSum;
        this.status = status;
    }

    public Double getMeterSum() {
        return meterSum;
    }

    public void setMeterSum(Double meterSum) {
        this.meterSum = meterSum;
    }

    public Integer getMeterdataid() {
        return meterdataid;
    }

    public void setMeterdataid(Integer meterdataid) {
        this.meterdataid = meterdataid;
    }

    public Meters getMeter() {
        return meter;
    }

    public void setMeter(Meters meter) {
        this.meter = meter;
    }

    public Double getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(Double previousValue) {
        this.previousValue = previousValue;
    }

    public Double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Double currentValue) {
        this.currentValue = currentValue;
    }

    public Double getMeterValue() {
        return meterValue;
    }

    public void setMeterValue(Double meterValue) {
        this.meterValue = meterValue;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MeterData{" +
                "meterdataid=" + meterdataid +
                ", meter=" + meter +
                ", previousValue=" + previousValue +
                ", currentValue=" + currentValue +
                ", meterValue=" + meterValue +
                ", meterSum=" + meterSum +
                ", status=" + status +
                '}';
    }
}
