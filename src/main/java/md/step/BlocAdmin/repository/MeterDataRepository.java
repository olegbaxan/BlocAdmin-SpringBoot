package md.step.BlocAdmin.repository;

import md.step.BlocAdmin.model.MeterData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MeterDataRepository extends JpaRepository<MeterData, Integer> {
    MeterData findAllByMeterdataid(Integer id);

    @Query(value = "select max(m.current_value) FROM application.meterdata m inner join application.meterdata_meter mm on m.meterdataid=mm.meterdata_id inner join application.meterdata_status ms on ms.meterdata_id=m.meterdataid where mm.meter_id=?1",nativeQuery = true)
    Double findMaxCurrentValueByMeterId(Integer meter_id);

}
