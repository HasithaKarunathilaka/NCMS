package lk.spark.ncms.dao;

public class Patient {

    private String firstName;
    private String lastName;
    private String district;
    private int xCoordinate;
    private int yCoordinate;
    private String gender;
    private String contact;
    private String email;
    private int age;

    public Patient(String firstName, String lastName, String district, int locationX, int locationY, String gender, String contact, String email, int age) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.district = district;
        this.xCoordinate = locationX;
        this.yCoordinate = locationY;
        this.gender = gender;
        this.contact = contact;
        this.email = email;
        this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDistrict() {
        return district;
    }

    public int getxCoordinate() {
        return xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    public String getGender() {
        return gender;
    }

    public String getContact() {
        return contact;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }
}
