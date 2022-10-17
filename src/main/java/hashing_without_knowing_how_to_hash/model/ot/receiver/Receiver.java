package main.java.hashing_without_knowing_how_to_hash.model.ot.receiver;

import localhost.hashing_without_knowing_how_to_hash.dto.ot.*;

/**
 * Implements math of the receiver side for a 1-out-of-2 RSA oblivious transfer
 */
public class Receiver {

    private PublicKeyRSADto publicKeyRSADto;
    private RandomMessagePairDto randomMessagePairDto;
    private SelectionDto selectionDto;
    private EncryptedSecretPairDto encryptedSecretPairDto;

    public Receiver(boolean b){
        publicKeyRSADto =null;
        randomMessagePairDto =null;
        selectionDto =new SelectionDto(b);
        encryptedSecretPairDto =null;
    }

    /**
     * Sets the public key requested form sender
     * @param publicKeyRSADto to be set
     */
    public void setPublicKeyRsa(PublicKeyRSADto publicKeyRSADto){
        this.publicKeyRSADto = publicKeyRSADto;
    }

    /**
     * Sets the two random messages requested from sender
     * @param randomMessagePairDto instance containing both random messages
     */
    public void setRandomMessagePair(RandomMessagePairDto randomMessagePairDto){
        this.randomMessagePairDto = randomMessagePairDto;
    }

    /**
     * The sender request the vValue from the receiver
     * @return the computed vValue
     */
    public VValueDto getVValue(){
        return new VValueDto(selectionDto, randomMessagePairDto, publicKeyRSADto);
    }

    /**
     * Sets the two encrypted secrets from sender
     * @param encryptedSecretPairDto encrypted secrets from sender
     */
    public void setEncryptedSecretPair(EncryptedSecretPairDto encryptedSecretPairDto){
        this.encryptedSecretPairDto = encryptedSecretPairDto;
    }

    /**
     * The receiver can finally encrypt just the selected secret
     * @return the plaintext secret wrapped in a dto
     */
    public SelectedSecretDto getSelectedSecret(){
        return new SelectedSecretDto(selectionDto, encryptedSecretPairDto, publicKeyRSADto);
    }
}
