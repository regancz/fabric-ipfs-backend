package main

import (
	"encoding/json"
	"fmt"
	"strconv"
	"time"

	"github.com/hyperledger/fabric-contract-api-go/contractapi"
)

// SmartContract provides functions for managing a gearbox
type SmartContract struct {
	contractapi.Contract
}

// Gearbox describes basic details of what makes up a gearbox
type Gearbox struct {
	Timestamp string `json:"timestamp"`
	CID       string `json:"cid"`
	Keyword   string `json:"keyword"`
	Owner     string `json:"owner"`
}

// QueryResult structure used for handling result of query
type QueryResult struct {
	Key    string `json:"Key"`
	Record *Gearbox
}

// InitLedger adds a base set of gearboxs to the ledger
func (s *SmartContract) InitLedger(ctx contractapi.TransactionContextInterface) error {
	gearboxs := []Gearbox{
		Gearbox{Timestamp: "2006-01-02", CID: "Prius", Keyword: "blue", Owner: "Tomoko"},
		Gearbox{Timestamp: "2007-01-02", CID: "Mustang", Keyword: "red", Owner: "Brad"},
		Gearbox{Timestamp: "2008-01-02", CID: "Tucson", Keyword: "green", Owner: "Jin Soo"},
		Gearbox{Timestamp: "2009-01-02", CID: "Passat", Keyword: "yellow", Owner: "Max"},
		Gearbox{Timestamp: "2010-01-02", CID: "S", Keyword: "black", Owner: "Adriana"},
		Gearbox{Timestamp: "2011-01-02", CID: "205", Keyword: "purple", Owner: "Michel"},
		Gearbox{Timestamp: "2012-01-02", CID: "S22L", Keyword: "white", Owner: "Aarav"},
		Gearbox{Timestamp: "2013-01-02", CID: "Punto", Keyword: "violet", Owner: "Pari"},
		Gearbox{Timestamp: "2014-01-02", CID: "Nano", Keyword: "indigo", Owner: "Valeria"},
		Gearbox{Timestamp: "2015-01-02", CID: "Barina", Keyword: "brown", Owner: "Shotaro"},
	}

	for i, gearbox := range gearboxs {
		gearboxAsBytes, _ := json.Marshal(gearbox)
		err := ctx.GetStub().PutState("gearbox"+strconv.Itoa(i), gearboxAsBytes)

		if err != nil {
			return fmt.Errorf("Failed to put to world state. %s", err.Error())
		}
	}

	return nil
}

// CreateGearbox adds a new gearbox to the world state with given details
func (s *SmartContract) CreateGearbox(ctx contractapi.TransactionContextInterface, gearboxNumber string, cid string, keyword string, owner string) error {
	gearbox := Gearbox{
		Timestamp: time.Now().Format("2006-01-02"),
		CID:       cid,
		Keyword:   keyword,
		Owner:     owner,
	}
	gearboxAsBytes, _ := json.Marshal(gearbox)
	return ctx.GetStub().PutState(gearboxNumber, gearboxAsBytes)
}

// QueryGearbox returns the gearbox stored in the world state with given id
func (s *SmartContract) QueryGearbox(ctx contractapi.TransactionContextInterface, gearboxNumber string) (*Gearbox, error) {
	gearboxAsBytes, err := ctx.GetStub().GetState(gearboxNumber)
	if err != nil {
		return nil, fmt.Errorf("Failed to read from world state. %s", err.Error())
	}

	if gearboxAsBytes == nil {
		return nil, fmt.Errorf("%s does not exist", gearboxNumber)
	}

	gearbox := new(Gearbox)
	_ = json.Unmarshal(gearboxAsBytes, gearbox)
	return gearbox, nil
}

// QueryAllGearboxes returns all gearboxes found in world state
func (s *SmartContract) QueryAllGearboxes(ctx contractapi.TransactionContextInterface) ([]QueryResult, error) {
	startKey := ""
	endKey := ""
	resultsIterator, err := ctx.GetStub().GetStateByRange(startKey, endKey)
	if err != nil {
		return nil, err
	}
	defer resultsIterator.Close()

	results := []QueryResult{}
	for resultsIterator.HasNext() {
		queryResponse, err := resultsIterator.Next()
		if err != nil {
			return nil, err
		}
		gearbox := new(Gearbox)
		_ = json.Unmarshal(queryResponse.Value, gearbox)
		queryResult := QueryResult{Key: queryResponse.Key, Record: gearbox}
		results = append(results, queryResult)
	}
	return results, nil
}

// ChangeGearboxOwner updates the owner field of gearbox with given id in world state
func (s *SmartContract) ChangeGearboxOwner(ctx contractapi.TransactionContextInterface, gearboxNumber string, newOwner string) error {
	gearbox, err := s.QueryGearbox(ctx, gearboxNumber)
	if err != nil {
		return err
	}

	gearbox.Owner = newOwner
	gearboxAsBytes, _ := json.Marshal(gearbox)
	return ctx.GetStub().PutState(gearboxNumber, gearboxAsBytes)
}

func main() {
	chaincode, err := contractapi.NewChaincode(new(SmartContract))
	if err != nil {
		fmt.Printf("Error create gearbox chaincode: %s", err.Error())
		return
	}

	if err := chaincode.Start(); err != nil {
		fmt.Printf("Error starting gearbox chaincode: %s", err.Error())
	}
}
