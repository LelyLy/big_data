package lab1.obfuscation;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "employee")
@XmlAccessorType(XmlAccessType.PROPERTY)
class Employee
{
    private String id;
    private String firstName;
    private String lastName;
    private String location;

    String getId() {
        return id;
    }

    @XmlAttribute
    void setId(String id) {
        this.id = id;
    }

    String getFirstName() {
        return firstName;
    }

    @XmlElement
    void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    String getLastName() {
        return lastName;
    }

    @XmlElement
    void setLastName(String lastName) {
        this.lastName = lastName;
    }

    String getLocation() {
        return location;
    }

    @XmlElement
    void setLocation(String location) {
        this.location = location;
    }
}