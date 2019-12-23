package lab1.obfuscation;

import javax.xml.bind.JAXBException;


public class Main {

    public static void main(String[] args) throws JAXBException {
        Parser parser = new Parser();
        String originalFileName = "./src/main/java/lab1/test_data/test_data_original.xml";
        String obfucatedFileName = "./src/main/java/lab1/test_data/test_data_obfuscated.xml";
        String unobfuscatedFileName = "./src/main/java/lab1/test_data/test_data_unobfuscated.xml";
        parser.obfuscateEmployeesFromFile(originalFileName, obfucatedFileName);
        parser.unobfuscateEmployesFromFile(obfucatedFileName, unobfuscatedFileName);
    }
}
