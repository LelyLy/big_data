package lab2;


import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OutputEncryptor;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {

        // The configuration works correctly:
        Security.setProperty("crypto.policy", "unlimited");
        int maxKeySize = Cipher.getMaxAllowedKeyLength("AES");
        System.out.println("Max Key Size for AES : " + maxKeySize);

        Security.addProvider(new BouncyCastleProvider());
        // The getInstance () method takes two arguments: the type of certificate is “X.509” and the security provider is “BC”.
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509", "BC");
        X509Certificate certificate = (X509Certificate) certFactory.generateCertificate(new FileInputStream("./src/main/java/lab2/public.cer"));
        // We need a global password to open the keystore and a special password to retrieve the private key
        char[] keystorePassword = "password".toCharArray();
        char[] keyPassword = "password".toCharArray();
        KeyStore keystore = KeyStore.getInstance("PKCS12");
        keystore.load(new FileInputStream("./src/main/java/lab2/private.p12"), keystorePassword);
        PrivateKey privateKey = (PrivateKey) keystore.getKey("baeldung", keyPassword);

        // Simple test for encrypt and decrypt data
        String secretMessage = "My password is 123qwerty";
        System.out.println("Original Message : " + secretMessage);
        byte[] stringToEncrypt = secretMessage.getBytes();
        byte[] encryptedData = encryptData(stringToEncrypt, certificate);
        System.out.println("Encrypted Message : ");
        System.out.println(new String(encryptedData));
        byte[] rawData = decryptData(encryptedData, privateKey);
        String decryptedMessage = new String(rawData);
        System.out.println("Decrypted Message : " + decryptedMessage);

        // Simple test for verify Signed Data
        byte[] signedData = signData(rawData, certificate, privateKey);
        Boolean check = verifySignedData(signedData);
        System.out.println(check);
    }

    private static byte[] encryptData(byte[] data, X509Certificate encryptionCertificate) throws CertificateEncodingException, CMSException, IOException {
        byte[] encryptedData = null;
        if (null != data && null != encryptionCertificate) {
            //
            CMSEnvelopedDataGenerator cmsEnvelopedDataGenerator = new CMSEnvelopedDataGenerator();
            // Using certificate the recipient.
            JceKeyTransRecipientInfoGenerator jceKey = new JceKeyTransRecipientInfoGenerator(encryptionCertificate);
            cmsEnvelopedDataGenerator.addRecipientInfoGenerator(jceKey);
            CMSTypedData msg = new CMSProcessableByteArray(data);
            OutputEncryptor encryptor = new JceCMSContentEncryptorBuilder(CMSAlgorithm.AES128_CBC)
                    .setProvider("BC")
                    .build();
            CMSEnvelopedData cmsEnvelopedData = cmsEnvelopedDataGenerator.generate(msg, encryptor);
            encryptedData = cmsEnvelopedData.getEncoded();
        }
        return encryptedData;
    }

    private static byte[] decryptData(byte[] encryptedData, PrivateKey decryptionKey) throws CMSException {
        byte[] decryptedData = null;
        if (null != encryptedData && null != decryptionKey) {
            CMSEnvelopedData envelopedData = new CMSEnvelopedData(encryptedData);
            // Retrieved all the intended recipients posts
            Collection<RecipientInformation> recipients = envelopedData.getRecipientInfos().getRecipients();
            KeyTransRecipientInformation recipientInfo  = (KeyTransRecipientInformation) recipients.iterator().next();
            JceKeyTransRecipient recipient = new JceKeyTransEnvelopedRecipient(decryptionKey);
            return recipientInfo.getContent(recipient);
        }
        return decryptedData;
    }

    private static byte[] signData(byte[] data, X509Certificate signingCertificate, PrivateKey signingKey) throws Exception {
        byte[] signedMessage = null;
        List<X509Certificate> certList = new ArrayList<>();
        CMSTypedData cmsData = new CMSProcessableByteArray(data);
        certList.add(signingCertificate);
        Store certs = new JcaCertStore(certList);
        CMSSignedDataGenerator cmsGenerator = new CMSSignedDataGenerator();
        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256withRSA").build(signingKey);
        cmsGenerator.addSignerInfoGenerator(
                new JcaSignerInfoGeneratorBuilder(
                        new JcaDigestCalculatorProviderBuilder()
                                .setProvider("BC")
                                .build())
                        .build(contentSigner, signingCertificate));
        cmsGenerator.addCertificates(certs);
        CMSSignedData cms = cmsGenerator.generate(cmsData, true);
        signedMessage = cms.getEncoded();
        return signedMessage;
    }

    private static boolean verifySignedData(byte[] signedData) throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(signedData);
        ASN1InputStream asnInputStream = new ASN1InputStream(inputStream);
        CMSSignedData cmsSignedData = new CMSSignedData(ContentInfo.getInstance(asnInputStream.readObject()));
        SignerInformationStore signers = cmsSignedData.getSignerInfos();
        SignerInformation signer = signers.getSigners().iterator().next();
        Collection<X509CertificateHolder> certCollection = cmsSignedData.getCertificates().getMatches(signer.getSID());
        X509CertificateHolder certHolder = certCollection.iterator().next();
        return signer.verify(new JcaSimpleSignerInfoVerifierBuilder().build(certHolder));
    }
}