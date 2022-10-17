package test.java.localhost.hashing_without_knowing_how_to_hash.security.access_control_tree;

import mockdata.WireMockData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import yao.gate.Wire;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccessControlTreeTest {

    AccessControlTree<Wire> accessControlTree;

    @BeforeEach
    public void setUp(){
        accessControlTree=new AccessControlTree<>();
    }

    private List<String> getIdList(){

        String partA="abc";
        String partB="def";
        String partC="ghi";
        List<String> partsInList=List.of(partA, partB, partC);
        return partsInList;
    }

    private String getId(){
        String partA="abc";
        String partB="def";
        String partC="ghi";
        String concatenatedParts=partA+partB+partC;
        return concatenatedParts;
    }

    @Test
    public void testConcatStringsInList(){
        //given
        List<String> idList=getIdList();
        String expectedConcatenatedString=getId();

        //when
        String actualConcatenatedString=accessControlTree.concatStringsInList(idList);

        //then
        assertEquals(expectedConcatenatedString, actualConcatenatedString);
    }

    @Test
    public void testSetShouldAddWireAndGetShouldGetTheWireAgain(){
        //given
        List<String> idList=List.of(String.valueOf(WireMockData.CIRCUITS_CONTAINER_HASH), String.valueOf(WireMockData.CIRCUIT_HASH));
        AccessControlTreeFinalLayer<Wire> accessControlTreeFinalLayer=new AccessControlTreeFinalLayer<>(WireMockData.getWireEncodingOfMockedWires());
        List<String> idOfWireA=List.of(String.valueOf(WireMockData.CIRCUITS_CONTAINER_HASH), String.valueOf(WireMockData.CIRCUIT_HASH), WireMockData.WIRE_ID_A);
        Wire mockedWireA=WireMockData.mockWireA();

        //when
        accessControlTree.set(idList, accessControlTreeFinalLayer);
        Wire shouldBeWireA=accessControlTree.get(idOfWireA);

        //then
        assertArrayEquals(mockedWireA.getValue0(), shouldBeWireA.getValue0());
        assertArrayEquals(mockedWireA.getValue1(), shouldBeWireA.getValue1());
    }

    @Test
    public void testGetCalledTwiceShouldReturnNullTheSecondTime(){
        //given
        List<String> idList=List.of(String.valueOf(WireMockData.CIRCUITS_CONTAINER_HASH), String.valueOf(WireMockData.CIRCUIT_HASH));
        AccessControlTreeFinalLayer<Wire> accessControlTreeFinalLayer=new AccessControlTreeFinalLayer<>(WireMockData.getWireEncodingOfMockedWires());
        List<String> idOfWireA=List.of(String.valueOf(WireMockData.CIRCUITS_CONTAINER_HASH), String.valueOf(WireMockData.CIRCUIT_HASH), WireMockData.WIRE_ID_A);
        accessControlTree.set(idList, accessControlTreeFinalLayer);

        //when
        accessControlTree.get(idOfWireA);
        Wire shouldNotGetInstance=accessControlTree.get(idOfWireA);

        //then
        assertNull(shouldNotGetInstance);
    }
}