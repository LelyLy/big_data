package lab1.obfuscation;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;

public class Parser {
    static Employees employees = new Employees();
    static
    {
        employees.setEmployees(new ArrayList<Employee>());
        Employee emp1 = new Employee();
        emp1.setId(1);
        emp1.setFirstName("Lokesh");
        emp1.setLastName("Gupta");
        emp1.setLocation("India");

        Employee emp2 = new Employee();
        emp2.setId(2);
        emp2.setFirstName("Alex");
        emp2.setLastName("Gussin");
        emp2.setLocation("Russia");

        employees.getEmployees().add(emp1);
        employees.getEmployees().add(emp2);
    }

    private static void marshalingXMLData() throws JAXBException
    {
        JAXBContext jaxbContext = JAXBContext.newInstance(Employees.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxbMarshaller.marshal(employees, System.out);

        jaxbMarshaller.marshal(employees, new File("/home/user/IdeaProjects/big_data/src/main/java/lab1/test_data/test_new_data.xml"));
    }
    private static void unMarshalingXMLData() throws JAXBException
    {
        JAXBContext jaxbContext = JAXBContext.newInstance(Employees.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        Employees emps = (Employees) jaxbUnmarshaller.unmarshal( new File("/home/user/IdeaProjects/big_data/src/main/java/lab1/test_data/test_data.xml") );

        for(Employee emp : emps.getEmployees())
        {
            System.out.println(emp.getId());
            System.out.println(emp.getFirstName());
            System.out.println(emp.getLastName());
            System.out.println(emp.getLocation());
        }
    }
    public static void main(String[] args) throws JAXBException
    {
        marshalingXMLData();
        unMarshalingXMLData();
    }

    }
