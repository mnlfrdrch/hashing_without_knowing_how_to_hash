package localhost.hashing_without_knowing_how_to_hash.dto.ot;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
@NoArgsConstructor
public class VValueDto {

    @Getter
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigInteger v;

    private SelectionDto selectionDto;
    private RandomMessagePairDto randomMessagePairDto;
    private PublicKeyRSADto publicKeyRSADto;

    public VValueDto(BigInteger v){
        this.v=v;
        selectionDto =null;
        randomMessagePairDto =null;
        publicKeyRSADto =null;
    }

    public VValueDto(SelectionDto selectionDto, RandomMessagePairDto randomMessagePairDto, PublicKeyRSADto publicKeyRSADto){
        this.selectionDto = selectionDto;
        this.randomMessagePairDto = randomMessagePairDto;
        this.publicKeyRSADto = publicKeyRSADto;
        v=calculateV();
    }

    private BigInteger calculateV(){
        //v = (x+(k^e)) mod n  <=>  v = ((x mod n) + (k^e mod n)) mod n
        // (a + b) mod n = ((a mod n) + (b mod n)) mod n
        BigInteger xModN=getSelectedRandomMessage().mod(publicKeyRSADto.getN());
        BigInteger kModPowEN= selectionDto.getK().modPow(publicKeyRSADto.getE(), publicKeyRSADto.getN());
        BigInteger sum=xModN.add(kModPowEN);
        BigInteger moduloSum=sum.mod(publicKeyRSADto.getN());
        return moduloSum;
    }

    /*public String getV(){
        return String.valueOf(v);
    }*/

    private BigInteger getSelectedRandomMessage(){
        BigInteger selectedMessage=null;
        if(selectionDto.isB()){
            selectedMessage=randomMessagePairDto.getX1();
        }
        else {
            selectedMessage=randomMessagePairDto.getX0();
        }
        return selectedMessage;
    }
}
