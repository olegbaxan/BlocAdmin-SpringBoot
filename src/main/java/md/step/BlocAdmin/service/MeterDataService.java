package md.step.BlocAdmin.service;

import md.step.BlocAdmin.exception.MeterDataNotFoundException;
import md.step.BlocAdmin.model.MeterData;
import md.step.BlocAdmin.model.Meters;
import md.step.BlocAdmin.model.Status;
import md.step.BlocAdmin.repository.MeterDataRepository;
import md.step.BlocAdmin.repository.MetersRepository;
import md.step.BlocAdmin.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MeterDataService {
    private final MeterDataRepository meterDataRepository;
    private final StatusRepository statusRepository;
    private MetersRepository metersRepository;

    @Autowired
    public MeterDataService(MeterDataRepository meterDataRepository, MetersRepository metersRepository,StatusRepository statusRepository) {
        this.metersRepository = metersRepository;
        this.meterDataRepository = meterDataRepository;
        this.statusRepository=statusRepository;
    }
    public MeterData addMeterData(MeterData meterData) {
        return meterDataRepository.save(meterData);
    }

    public List<MeterData> findAll() {
        return meterDataRepository.findAll();
    }

    public List<Meters> findAllMeters() {
        return metersRepository.findAll();
    }
    public List<Status> findAllStatus() {
        return statusRepository.findAll();
    }


    public Meters getMetersByMeterDataId(Integer id) {
        Optional<MeterData> meterData = meterDataRepository.findById(id);
        return meterData.get().getMeter();
    }
    public Status getStatusByMeterDataId(Integer id) {
        Optional<MeterData> meterData= meterDataRepository.findById(id);
        return meterData.get().getStatus();
    }

    public MeterData updateMeterData(MeterData meterData) {
        return meterDataRepository.save(meterData);
    }


    public MeterData findMeterDataById(Integer id) throws MeterDataNotFoundException {
        return meterDataRepository.findById(id)
                .orElseThrow(() -> new MeterDataNotFoundException(id));
    }

    public void deleteMeterData(Integer id) throws MeterDataNotFoundException {
        final MeterData meterData = this.meterDataRepository.findById(id).orElseThrow(() -> new MeterDataNotFoundException(id));
        meterDataRepository.delete(meterData);
    }

    public Page<MeterData> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.meterDataRepository.findAll(pageable);
    }
}
