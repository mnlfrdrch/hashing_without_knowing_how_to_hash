package localhost.hashing_without_knowing_how_to_hash.model.ot.receiver;

import localhost.hashing_without_knowing_how_to_hash.dto.WireAddressDto;
import localhost.hashing_without_knowing_how_to_hash.dto.ot.*;
import localhost.hashing_without_knowing_how_to_hash.dto.party.HostDto;
import localhost.hashing_without_knowing_how_to_hash.model.ot.WireLabelSecretDecoder;
import localhost.hashing_without_knowing_how_to_hash.util.HttpUtil;

import java.math.BigInteger;

/**
 * Implements the concrete transfer on receiver side for a 1-out-of-2 RSA oblivious transfer
 * The receiver can receive exactly one of the two wire labels
 */
public class WireLabelReceiver {

    private Receiver receiver;
    private WireAddressDto wireAddress;
    private HostDto hostDto;

    public WireLabelReceiver(HostDto hostDto, WireAddressDto wireAddress, boolean b){
        this.hostDto=hostDto;
        this.wireAddress=wireAddress;
        receiver=new Receiver(b);
    }

    /**
     * Runs the oblivious transfer protocol on selected wire address, host and selection b.
     * @return the selected wire label
     */
    public byte[] receive(){
        PublicKeyRSADto publicKey=receivePublicKeyRSA();
        receiver.setPublicKeyRsa(publicKey);
        RandomMessagePairDto randomMessagePairDto =receiveRandomMessagePair();
        receiver.setRandomMessagePair(randomMessagePairDto);
        VValueDto vValueDto =receiver.getVValue();
        sendVValue(vValueDto);
        EncryptedSecretPairDto encryptedSecretPairDto =receiveEncryptedSecretPair();
        receiver.setEncryptedSecretPair(encryptedSecretPairDto);

        SelectedSecretDto selectedSecretDto =receiver.getSelectedSecret();
        BigInteger receivedSecret= selectedSecretDto.getM();
        WireLabelSecretDecoder decoder=new WireLabelSecretDecoder(receivedSecret);
        return decoder.decode();
    }

    private PublicKeyRSADto receivePublicKeyRSA(){
        PublicKeyRSADto publicKey=HttpUtil.httpGetPublicKeyRSADto(hostDto, wireAddress);
        return publicKey;
    }


    private RandomMessagePairDto receiveRandomMessagePair(){
        RandomMessagePairDto randomMessagePairDto=HttpUtil.httpGetRandomMessagePairDto(hostDto, wireAddress);
        return randomMessagePairDto;
    }

    private void sendVValue(VValueDto vValueDto){
        HttpUtil.httpPostVValueDto(hostDto, vValueDto,wireAddress);
    }


    private EncryptedSecretPairDto receiveEncryptedSecretPair(){
        EncryptedSecretPairDto encryptedSecretPairDto=HttpUtil.httpGetEncryptedSecretPairDto(hostDto,wireAddress);
        return encryptedSecretPairDto;
    }

}
