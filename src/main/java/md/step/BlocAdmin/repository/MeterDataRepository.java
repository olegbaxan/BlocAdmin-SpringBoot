package md.step.BlocAdmin.repository;

import md.step.BlocAdmin.model.EStatus;
import md.step.BlocAdmin.model.MeterData;
import md.step.BlocAdmin.model.TypeOfMeterInvoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface MeterDataRepository extends JpaRepository<MeterData, Integer> {
    MeterData findAllByMeterdataid(Integer id);

    @Query(value = "select max(m.current_value) FROM application.meterdata m inner join application.meterdata_meter mm on m.meterdataid=mm.meterdata_id inner join application.meterdata_status ms on ms.meterdata_id=m.meterdataid where mm.meter_id=?1",nativeQuery = true)
    Double findMaxCurrentValueByMeterId(Integer meter_id);

    @Query(value = "select * FROM application.meterdata m \n" +
            "inner join application.meterdata_meter mm on m.meterdataid=mm.meterdata_id \n" +
            "inner join application.meterdata_status ms on ms.meterdata_id=m.meterdataid \n" +
            "where mm.meter_id=?1 order by m.previous_value DESC limit 1",nativeQuery = true)
    MeterData findAllByMaxCurrentValueAndyMeterId(Integer meter_id);

//    Page findAllByStatusNameStatusNew(Pageable page);
    Page findMeterDataByStatus_Name(EStatus status, Pageable page);
//    Page findMeterDataByStatusNameAndMeterTypeOfMeterInvoice(EStatus status, ETypeOfMeterInvoice type, Pageable page);
    List<MeterData> findMeterDataByStatus_NameAndMeter_TypeOfMeterInvoice(EStatus status, TypeOfMeterInvoice type);
    List<MeterData> findMeterDataByStatus_NameAndMeter_TypeOfMeterInvoiceAndMeter_Supplier_SupplierName(EStatus status, TypeOfMeterInvoice type,String supplier);
    List<MeterData> findMeterDataByStatus_NameAndMeter_TypeOfMeterInvoiceAndMeter_Flat_Building_Buildingid(EStatus status, TypeOfMeterInvoice type, Integer buildingid);
    List<MeterData> findMeterDataByStatus_NameAndMeter_TypeOfMeterInvoiceAndMeter_Supplier_SupplierNameAndMeter_Flat_Building_Buildingid(EStatus status, TypeOfMeterInvoice type,String supplier, Integer buildingid);


    @Query(value = "SELECT md.meterdataid,md.current_value,md.meter_value,md.previous_value\n" +
            "\tFROM application.meterdata md\n" +
            "\tinner join application.meterdata_meter mm\n" +
            "\ton md.meterdataid=mm.meterdata_id\n" +
            "\tinner join application.meter_typeofmeterinvoice tmd \n" +
            "\ton mm.meter_id=tmd.meter_id\n" +
            "\tinner join application.meterdata_status ms \n" +
            "\ton md.meterdataid=ms.meterdata_id\n" +
            "\twhere ms.status_id=?1 and tmd.type_of_meter_invoice_id=?2",nativeQuery = true)
    Page findMeterDataByStatusAndTypeOfMeter(Integer status,Integer type,Pageable page);

    @Query(value = "SELECT md.meterdataid as meterdat1_20_,md.current_value as current_2_20_,md.meter_value as meter_va3_20_,md.previous_value as previous4_20_\n" +
            "\tFROM application.meterdata md\n" +
            "\tinner join application.meterdata_meter mm\n" +
            "\ton md.meterdataid=mm.meterdata_id\n" +
            "\tinner join application.meterdata_status ms \n" +
            "\ton md.meterdataid=ms.meterdata_id\n" +
            "\twhere ms.status_id=?1",nativeQuery = true)
    Page findMeterDataByStatus(Integer status,Pageable page);
}
