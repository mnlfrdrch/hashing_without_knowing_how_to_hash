package test.java.localhost.hashing_without_knowing_how_to_hash.model.circuit;

import mockdata.LiteralTestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.logicng.formulas.Literal;
import yao.gate.Wire;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WireEncodingTest {

    private WireEncoding wireEncoding;

    @BeforeEach
    public void setUp(){
        Set<Literal> literals= LiteralTestData.getLiteralsAtoC();
        wireEncoding=new WireEncoding(literals);
    }

    @Test
    public void testGetWireShouldReturnInstanceForIdC(){
        //given
        String wireId="C";

        //when
        Wire instance=wireEncoding.getWire(wireId);

        //then
        assertNotNull(instance);
    }

    @Test
    public void testGetWireShouldReturnNullForIdD(){
        //given
        String illegitimateWireId="D";

        //when
        Wire shouldBeNull=wireEncoding.getWire(illegitimateWireId);

        //then
        assertNull(shouldBeNull);
    }

    @Test
    public void testGetIdShouldReturnEncodingOfWireInstance(){
        //given
        Wire wireA=wireEncoding.getInputWires().values().iterator().next();

        //when
        String idShouldBeA=wireEncoding.getId(wireA);

        //then
        assertEquals("A", idShouldBeA);
    }

}