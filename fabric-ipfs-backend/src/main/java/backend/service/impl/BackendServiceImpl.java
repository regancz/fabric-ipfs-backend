package backend.service.impl;

import backend.ipfs.IpfsConnector;
import backend.service.BackendService;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.sdk.Peer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.EnumSet;

/**
 * @author charles
 * @date 2023/3/4 0:04
 */

@Service
public class BackendServiceImpl implements BackendService {
    @Autowired
    private backend.fabric.fabricConnector fabricConnector;

    @Autowired
    private IpfsConnector ipfsConnector;

    @Override
    public byte[] createTrx(String cName, String ccName, String cid, String keyword) throws IOException {
        try {
            Network network = fabricConnector.initContract(cName, ccName);
            Contract contract = network.getContract(ccName);
//            String methodName = "create" + ccName;
            return contract.createTransaction("createMixture")
                    .setEndorsingPeers(network.getChannel().getPeers(EnumSet.of(Peer.PeerRole.ENDORSING_PEER)))
                    // first arg is keyword, second is cid
                    .submit(keyword, cid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String uploadFile(String filePath) throws IOException {
        return ipfsConnector.upload(filePath);
    }

    @Override
    public String queryByCid(String Cid){
        return ipfsConnector.getContentByCid(Cid);
    }

    @Override
    public String queryMixtureByTimestampAndKeyword(String timestamp, String keyword) {
        try {
            Network network = fabricConnector.initContract("mychannel", "mixture");
            Contract contract = network.getContract("mixture");
            byte[] invokeResult = contract.evaluateTransaction("queryMixture", keyword, timestamp);
            return new String(invokeResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] createGearbox(String cName, String ccName, String cid, String keyword, String owner) throws IOException {
        try {
            Network network = fabricConnector.initContract(cName, ccName);
            Contract contract = network.getContract(ccName);
//            String methodName = "create" + ccName;
            return contract.createTransaction("CreateGearbox")
                    .setEndorsingPeers(network.getChannel().getPeers(EnumSet.of(Peer.PeerRole.ENDORSING_PEER)))
                    // first arg is keyword, second is cid
                    .submit("gearbox" + (int) (Math.random()*1000), cid, keyword, owner);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String QueryGearboxById(String gearboxNumber) {
        try {
            Network network = fabricConnector.initContract("mychannel", "gearbox");
            Contract contract = network.getContract("gearbox");
            byte[] invokeResult = contract.evaluateTransaction("QueryGearbox", gearboxNumber);
            return new String(invokeResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String queryAllGearboxes() {
        try {
            Network network = fabricConnector.initContract("mychannel", "gearbox");
            Contract contract = network.getContract("gearbox");
            byte[] invokeResult = contract.evaluateTransaction("QueryAllGearboxes");
            return new String(invokeResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
