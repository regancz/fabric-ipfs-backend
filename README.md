# fabric-ipfs-backend

fabric-ipfs-backend provides a secure channel to store and transfer records. It is based on the [Interplanetary File System (IPFS)](https://ipfs.io/) and [Hyperledger Fabric](https://www.hyperledger.org/). The conjunction of these technologies guarantees the immutability and availability of the data.

## Installation and Usage

**Hyperledger Fabric**

Project website: https://www.hyperledger.org/projects/fabric

we use test-network to initialize our network

```
git clone https://github.com/hyperledger/fabric-samples
cd fabric-samples/test-network
./network.sh up createChannel -s couchdb
```

 copy gearbox to fabric-samples/chaincode and test network condition

```
./network.sh deployCC -ccn gearbox -ccp ../chaincode/gearbox/go/ -ccl go 

peer chaincode invoke -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls --cafile ${PWD}/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem -C mychannel -n gearbox --peerAddresses localhost:7051 --tlsRootCertFiles ${PWD}/organizations/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt --peerAddresses localhost:9051 --tlsRootCertFiles ${PWD}/organizations/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt -c '{"function":"InitLedger","Args":[]}'

peer chaincode invoke -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls --cafile ${PWD}/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem -C mychannel -n gearbox --peerAddresses localhost:7051 --tlsRootCertFiles ${PWD}/organizations/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt --peerAddresses localhost:9051 --tlsRootCertFiles ${PWD}/organizations/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt -c '{"Args":["CreateGearbox","gearbox110","123321","Blue","Borui"]}'

peer chaincode query t -C mychannel -n gearbox -c '{"Args":["QueryAllGearboxes"]}'
```

**Interplanetary File System (IPFS)**

Project website: https://ipfs.io/

our furture work is supporting ipfs-cluster that already hava HTTP API, but official don't provide ipfs-cluster-api to develop

```
docker run -d --name ipfs_host -v $PWD/export:/export -v $PWD/data:/data/ipfs -p 4001:4001 -p 127.0.0.1:8181:8181 -p 127.0.0.1:5001:5001 ipfs/go-ipfs:latest
```

## Configuration of fabric-ipfs-backend

copy 2 folder including **ordererOrganizations** and **peerOrganizations**(fabric-samples/test-network/organizations) to fabric-ipfs-backend/src/main/resources/crypto-config

Edit fabric-ipfs-backend/src/main/java/backend/ipfs/IpfsConnector.java

```
ipfs = new IPFS("/ip4/127.0.0.1/tcp/5001");
```

## Test API

- CreateGearbox

  ![image-20230304190322290](https://gitee.com/sambird/fabric-ipfs-backend/raw/master/imgs/image-20230304190052809.png)

- queryGearboxById

![image-20230304190322290](https://gitee.com/sambird/fabric-ipfs-backend/raw/master/imgs/image-20230304190322290.png)

- queryAllGearboxes

![image-20230304190336279](https://gitee.com/sambird/fabric-ipfs-backend/raw/master/imgs/image-20230304190336279.png)

## Further Research

- we plan to use nacos to support more convenient experiment, it also support cluster
- develop chaincode support private data and CompositeKey, etc, and set up a better model to design data format like index in MySql
- support load balance, discovery, heart beat, validation for cid, consensus, security protocols, ipfs-cluster