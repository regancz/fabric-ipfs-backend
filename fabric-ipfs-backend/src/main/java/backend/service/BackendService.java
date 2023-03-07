package backend.service;

import java.io.IOException;

/**
 * @author charles
 * @date 2023/3/4 0:04
 */
public interface BackendService {
    byte[] createTrx(String cName, String ccName, String cid, String keyword) throws IOException;

    String uploadFile(String filePath) throws IOException;

    String queryByCid(String Cid);

    String queryMixtureByTimestampAndKeyword(String timestamp, String keyword);

    byte[] createGearbox(String cName, String ccName, String cid, String keyword, String owner) throws IOException;

    String QueryGearboxById(String gearboxNumber);

    String queryAllGearboxes();
}
