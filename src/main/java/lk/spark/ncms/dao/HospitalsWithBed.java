package lk.spark.ncms.dao;

import lk.spark.ncms.repository.HospitalBedRepository;

public class HospitalsWithBed {

    private String hospitalId ;
    private String patientId ;
    private int xCoordinate;
    private int yCoordinate;
//    private int bedNo;

//    public HospitalsWithBed() {
//
//    }

    public String getHospitalId() {
        return hospitalId;
    }

    public int getxCoordinate() {
        return xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public void setxCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public void setyCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public int assignBedToNearestHospital(String hospitalId, String patientId){
        setHospitalId(hospitalId);
        setPatientId(patientId);
        HospitalBedRepository hospitalBedRepository = new HospitalBedRepository();
        int bedId = hospitalBedRepository.getBedId(hospitalId, patientId);
        if(bedId != 0){
            String result = hospitalBedRepository.admitPatient(hospitalId, patientId, bedId);
            return bedId;
        }else {
            return bedId;
        }
    }

    public String assignBedToNextHospital(String hospitalId, String patientId, String bedNo){
        setHospitalId(hospitalId);
        int bedId = Integer.parseInt(bedNo);
        HospitalBedRepository hospitalBedRepository = new HospitalBedRepository();
        String result = hospitalBedRepository.admitPatient(hospitalId, patientId, bedId);

        return result;
    }
}
