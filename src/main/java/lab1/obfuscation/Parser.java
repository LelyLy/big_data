package lab1.obfuscation;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.io.File;

import static lab1.obfuscation.Obfuscator.obfuscate;
import static lab1.obfuscation.Obfuscator.unobfuscate;

@XmlSeeAlso({Employee.class})
class Parser {

    private static void marshalingXMLData(Employees employees, String filename) throws JAXBException
    {
        JAXBContext jaxbContext = JAXBContext.newInstance(Employees.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(employees, new File(filename));
    }

    private static Employees unMarshalingXMLData(String filename) throws JAXBException
    {
        JAXBContext jaxbContext = JAXBContext.newInstance(Employees.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        return ((Employees) jaxbUnmarshaller.unmarshal( new File(filename) ));

    }
    private static void obfuscateEmployees(Employees emps) {
        for(Employee emp : emps.getEmployees())
        {
            emp.setId(obfuscate(emp.getId()));
            emp.setFirstName(obfuscate(emp.getFirstName()));
            emp.setLastName(obfuscate(emp.getLastName()));
            emp.setLocation(obfuscate(emp.getLocation()));
        }
    }

    private static void unobfuscateEmployees(Employees emps) {
        for(Employee emp : emps.getEmployees())
        {
            emp.setId(unobfuscate(emp.getId()));
            emp.setFirstName(unobfuscate(emp.getFirstName()));
            emp.setLastName(unobfuscate(emp.getLastName()));
            emp.setLocation(unobfuscate(emp.getLocation()));
        }
    }

    void unobfuscateEmployesFromFile(String inputFilename, String outputFilename) throws JAXBException {
        Employees emp = unMarshalingXMLData(inputFilename);
        unobfuscateEmployees(emp);
        marshalingXMLData(emp, outputFilename);
    }

    void obfuscateEmployeesFromFile(String inputFilename, String outputFilename) throws JAXBException {
        Employees emp = unMarshalingXMLData(inputFilename);
        obfuscateEmployees(emp);
        marshalingXMLData(emp, outputFilename);
    }
}

