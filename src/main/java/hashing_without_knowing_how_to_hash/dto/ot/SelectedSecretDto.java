package main.java.hashing_without_knowing_how_to_hash.dto.ot;

import lombok.Getter;

import java.math.BigInteger;

/**
 * When all the exchange of information has taken place, the receiver can finally put it all together
 * and decrypt the selected encrypted secret
 */
public class SelectedSecretDto {

    @Getter
    private BigInteger m;

    public SelectedSecretDto(SelectionDto selectionDto, EncryptedSecretPairDto encryptedSecretPairDto, PublicKeyRSADto publicKeyRSADto){
        m=decrypt(selectionDto, encryptedSecretPairDto, publicKeyRSADto);
    }

    public BigInteger decrypt(SelectionDto selectionDto, EncryptedSecretPairDto encryptedSecretPairDto, PublicKeyRSADto publicKeyRSADto){
        BigInteger encm_x=getSelectedEncryptedMessage(selectionDto, encryptedSecretPairDto);
        BigInteger decrypted=encm_x.subtract(selectionDto.getK()).mod(publicKeyRSADto.getN());
        return decrypted;
    }

    public BigInteger getSelectedEncryptedMessage(SelectionDto selectionDto, EncryptedSecretPairDto encryptedSecretPairDto){
        if(selectionDto.isB()){
            return encryptedSecretPairDto.getEncM1();
        }
        else{
            return encryptedSecretPairDto.getEncM0();
        }
    }
}
