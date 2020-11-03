package lk.spark.ncms.service;

import lk.spark.ncms.repository.HospitalBedRepository;

public class DoctorServicelmpl implements DoctorService {
    public String[] getHospitalStatistic(String hospitalId){

        String [] result = new String[2];
        int totalBed = 10;
        HospitalBedRepository hospitalBedRepository = new HospitalBedRepository();
        result[0] = hospitalBedRepository.getBedCount(hospitalId);
        result[1] = String.valueOf((totalBed - Integer.parseInt(result[0])));
        return result;
    }
}
