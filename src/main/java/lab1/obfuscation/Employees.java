package lab1.obfuscation;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "employees")
@XmlAccessorType(XmlAccessType.FIELD)
class Employees
{
    @XmlElement(name = "employee")
    private List<Employee> employees = null;

    List<Employee> getEmployees() {
        return employees;
    }
}
