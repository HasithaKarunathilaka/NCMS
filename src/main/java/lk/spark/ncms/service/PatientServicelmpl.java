package lk.spark.ncms.service;

import lk.spark.ncms.dao.HospitalsWithBed;
import lk.spark.ncms.dao.Patient;
import lk.spark.ncms.repository.HospitalRepository;
import lk.spark.ncms.repository.PatientQueueRepository;
import lk.spark.ncms.repository.PatientRepository;

public class PatientServicelmpl implements PatientService {

    @Override
    public String[] registerPatient(Patient patientInformation) {
        String [] result = new String[3];
        PatientRepository patientRepository = new PatientRepository();
        String patientId = patientRepository.addPatient(patientInformation);

        if (!patientId.equals("Error")) {
            result[2] = patientId;
            int patientXCoordinate = patientInformation.getxCoordinate();
            int patientYCoordinate = patientInformation.getyCoordinate();
            HospitalRepository hospitalRepository = new HospitalRepository();
//            ArrayList<HospitalsWithBed> hospitalsWithBedArrayList;
//            hospitalsWithBedArrayList = hospitalRepository.getHospitalsWithBeds();
            String nearestHospitalId = hospitalRepository.getNearestHospital(patientXCoordinate, patientYCoordinate);

            HospitalsWithBed hospitalsWithBed = new HospitalsWithBed();
            int bedID = hospitalsWithBed.assignBedToNearestHospital(nearestHospitalId, patientId);
            if(bedID == 0){
                result = hospitalRepository.assignedHospital(nearestHospitalId, patientId);
                String assignResult = hospitalsWithBed.assignBedToNextHospital(result[0], patientId, result[1]);
                if(assignResult == "success"){
                    return result;
                }else{
                    PatientQueueRepository patientQueueRepository = new PatientQueueRepository();
                    String queueResult = patientQueueRepository.addToQueue(patientId);
                    result[0] = "All Hospitals are Booked";
                    result[1] = queueResult;
                    return result;
                }
//                return result;
            }else{
                result[0] = nearestHospitalId;
                result[1] = String.valueOf(bedID);
                return result;
            }



//            if (hospitalsWithBedArrayList.isEmpty()) {
//                PatientQueueRepository patientQueueRepository = new PatientQueueRepository();
//                return patientQueueRepository.addToQueue(patientId);
////                return hospitalsWithBedArrayList.get();
//
//            } else {
//
//                double distance = 0.0;
//                String hospitalId = "Hospital ID Servlet";
//                String test = "testPatientServlet";
////                int i = 0;
//                for (HospitalsWithBed hospitalsWithBed : hospitalsWithBedArrayList) {
////                    i++;
//                    double xDistance = (double) Math.pow(patientInformation.getxCoordinate() - hospitalsWithBed.getxCoordinate(), 2);
//                    double yDistance = (double) Math.pow(patientInformation.getyCoordinate() - hospitalsWithBed.getyCoordinate(), 2);
//                    double calculatedDistence = (double) Math.sqrt(xDistance + yDistance);
//                    test = "For Loop ";
//                    if (distance == 0.0 || distance > calculatedDistence) {
//
//                        hospitalId = hospitalsWithBed.getHospitalId();
//                        distance = calculatedDistence;
//                        test = (hospitalId + " sfsafgrg fdvbdfg rgerg " + distance);
//                    }
//                }
//
//                HospitalBedRepository hospitalBedRepository = new HospitalBedRepository();
//                return hospitalBedRepository.admitPatient(hospitalId, patientId);
//                return String.valueOf(hospitalsWithBedArrayList);
//            }


//        } else {

//            return "Patient Registration Failed..!!";
        }else {
            result[0]= "Patient Registration Failed..!!";
            return result;
        }

    }
}
