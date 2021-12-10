package md.step.BlocAdmin.repository;

import md.step.BlocAdmin.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvoicesRepository extends JpaRepository<Invoices, Integer> {
    Invoices findInvoiceByInvoiceId(Integer id);

    List<Invoices> findAllBySupplier_SupplierNameAndStatusNameAndTypeOfMeterInvoiceName(String supplierName, EStatus status, ETypeOfMeterInvoice type);
    List<Invoices> findInvoicesByMeter_PersonOrMeter_Flat_Person(Person contactPerson, Person flatPerson);
    List<Invoices> findFirst10InvoicesByMeter_PersonOrMeter_Flat_PersonOrderByEmittedDateDesc(Person contactPerson, Person flatPerson);
    List<Invoices> findFirst10InvoicesByTypeOfMeterInvoiceOrderByEmittedDateDesc(TypeOfMeterInvoice type);
    List<Invoices> findInvoicesByMeter_Flat_FlatidAndStatus(Integer flatId, Status status);
    List<Invoices> findInvoicesByTypeOfMeterInvoiceAndStatusOrStatus(TypeOfMeterInvoice type, Status stNew,Status stSend);
    Page<Invoices> findAllDistinctInvoicesByInvoiceNumberStartingWithIgnoreCaseOrSupplier_SupplierNameContainingIgnoreCaseOrBuildings_Address_CityStartingWithIgnoreCaseOrBuildings_Address_RaionStartingWithIgnoreCaseOrBuildings_Address_StreetStartingWithIgnoreCase ( String invoice,String supplier,String city,String raion, String street,Pageable pageable);
    List<Invoices> findInvoicesByInvoiceNumberStartingWith(String invoiceNumber);

    List<Invoices> findInvoicesByTypeOfMeterInvoiceName(ETypeOfMeterInvoice type);
    // String supplier,

//    List<Invoices> findInvoicesByMeter_Building_ AndTypeOfMeterInvoiceOrderByEmittedDateDesc(Person person, TypeOfMeterInvoice type);

//    @Query(value = "select i.invoice_id ,i.date_of_pay ,i.emitted_date ,i.invoice_file_id ,i.invoice_number ,i.invoice_sum ,i.meter_data_current ,i.meter_data_previous ,i.pay_till ,i.unit_price from application.Invoices i "+
    @Query(value = "select i.* from application.Invoices i "+
    " inner join application.invoice_meter im on i.invoice_id =im.invoice_id "+
            " inner join application.meters m on im.meter_id =m.meterid "+
            " inner join application.meter_typeofmeterinvoice mt on mt.meter_id =m.meterid "+
            " inner join application.typeofmeterinvoice t on t.id = mt.type_of_meter_invoice_id "+
            " inner join application.invoice_building ib on i.invoice_id = ib.invoice_id "+
            " inner join application.buildings b on ib.buildings_id =b.buildingid "+
            " inner join application.flats_building fb on fb.building_id =b.buildingid "+
            " inner join application.flats f on f.flatid =fb.flat_id "+
            " inner join application.flats_persons fp on fp.flat_id =f.flatid "+
            " inner join application.persons p on p.personid =fp.person_id "
            +" where fp.person_id =?1 and t.id = ?2 order by i.emitted_date desc limit 10"
            ,nativeQuery = true)
    List<Invoices> findSupplierInvoiceForBuildings(Integer personId, Integer type);
        List<Invoices> findInvoicesByMeter_Flat_Person(Person person);


    Boolean existsInvoicesByInvoiceNumber(String invoice);
}
