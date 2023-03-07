package backend.fabric;

import org.hyperledger.fabric.gateway.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Objects;
import java.util.Properties;

/**
 * @author charles
 * @date 2023/3/3 23:42
 */

@Component
public class fabricConnector {
    public Gateway gateway;
    public String channelName;
    public String contractName;

    public fabricConnector() throws IOException {
        try {
            Properties properties = new Properties();
            InputStream inputStream = fabricConnector.class.getResourceAsStream("/config.properties");
            properties.load(inputStream);

            String networkConfigPath = properties.getProperty("networkConfigPath");
            channelName = properties.getProperty("channelName");
            contractName = properties.getProperty("contractName");
            // use org1.user1 initialize wallet to connect network
            String certificatePath = properties.getProperty("certificatePath");
            X509Certificate certificate = readX509Certificate(Paths.get(certificatePath));

            String privateKeyPath = properties.getProperty("privateKeyPath");
            PrivateKey privateKey = getPrivateKey(Paths.get(privateKeyPath));

            Wallet wallet = Wallets.newInMemoryWallet();
            wallet.put("user1", Identities.newX509Identity("Org1MSP",certificate,privateKey));

            Gateway.Builder builder = Gateway.createBuilder()
                    .identity(wallet, "user1")
                    .networkConfig(Paths.get(networkConfigPath));
            gateway = builder.connect();
        } catch (InvalidKeyException | CertificateException e) {
            throw new RuntimeException(e);
        }
    }

    public Network initContract(String channelName, String contractName){
        if (!Objects.equals(channelName, ""))
            this.channelName = channelName;
        if (!Objects.equals(contractName, ""))
            this.contractName = contractName;
        return gateway.getNetwork(channelName);
    }

    private static X509Certificate readX509Certificate(final Path certificatePath) throws IOException, CertificateException {
        try (Reader certificateReader = Files.newBufferedReader(certificatePath, StandardCharsets.UTF_8)) {
            return Identities.readX509Certificate(certificateReader);
        }
    }

    private static PrivateKey getPrivateKey(final Path privateKeyPath) throws IOException, InvalidKeyException {
        try (Reader privateKeyReader = Files.newBufferedReader(privateKeyPath, StandardCharsets.UTF_8)) {
            return Identities.readPrivateKey(privateKeyReader);
        }
    }
}
