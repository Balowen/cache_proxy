package proxy;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "customers")
public class customer {
    @DatabaseField(generatedId = true)
    private int CustomerId;

    @DatabaseField(columnName = "FirstName")
    private String FirstName;

    @DatabaseField(columnName = "LastName")
    private String LastName;

    @DatabaseField(columnName = "Company")
    private String Company;

    @DatabaseField(columnName = "Address")
    private String Address;

    @DatabaseField(columnName = "City")
    private String City;

    @DatabaseField(columnName = "State")
    private String State;

    @DatabaseField(columnName = "Country")
    private String Country;

    @DatabaseField(columnName = "PostalCode")
    private String PostalCode;

    @DatabaseField(columnName = "Phone")
    private String Phone;

    @DatabaseField(columnName = "Fax")
    private String Fax;

    @DatabaseField(columnName = "Email")
    private String Email;

    @DatabaseField(columnName = "SupportRepId")
    private int SupportRepId;


    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int customerId) {
        CustomerId = customerId;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getFax() {
        return Fax;
    }

    public void setFax(String fax) {
        Fax = fax;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getSupportRepId() {
        return SupportRepId;
    }

    public void setSupportRepId(int supportRepId) {
        SupportRepId = supportRepId;
    }
}
