package backend.ipfs;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author charles
 * @date 2023/3/3 23:18
 */

@Component
public class IpfsConnector {
    public static IPFS ipfs;

    public IpfsConnector() {
        ipfs = new IPFS("/ip4/127.0.0.1/tcp/5001");
    }

    public IpfsConnector(IPFS ipfs) {
        IpfsConnector.ipfs = ipfs;
    }

    public String upload(String pathname) throws IOException {
        NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(new File(pathname));
        MerkleNode addResult = ipfs.add(file).get(0);
        return addResult.hash.toBase58();
    }

    public String upload(byte[] data) throws IOException {
        NamedStreamable.ByteArrayWrapper file = new NamedStreamable.ByteArrayWrapper(data);
        MerkleNode addResult = ipfs.add(file).get(0);
        return addResult.hash.toString();
    }

    public String getContentByCid(String Cid) {
        byte[] result = null;
        try {
            Multihash multihash = Multihash.fromBase58(Cid);
            result = ipfs.cat(multihash);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (result == null)
            return "";
        return new String(result);
    }

    public String download(String hash, String destFile) {
        byte[] data = null;
        try {
            data = ipfs.cat(Multihash.fromBase58(hash));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (data != null && data.length > 0) {
            File file = new File(destFile);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                fos.write(data);
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return new String(data);
    }
}
