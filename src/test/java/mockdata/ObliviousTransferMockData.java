package mockdata;

import localhost.hashing_without_knowing_how_to_hash.dto.ot.EncryptedSecretPairDto;
import localhost.hashing_without_knowing_how_to_hash.dto.ot.PublicKeyRSADto;
import localhost.hashing_without_knowing_how_to_hash.dto.ot.RandomMessagePairDto;
import localhost.hashing_without_knowing_how_to_hash.model.ot.sender.Sender;
import lombok.experimental.UtilityClass;
import org.mockito.Mockito;

import java.math.BigInteger;


@UtilityClass
public class ObliviousTransferMockData {

    public RandomMessagePairDto mockRandomMessagePair(){
        RandomMessagePairDto randomMessages=Mockito.mock(RandomMessagePairDto.class);
        Mockito.when(randomMessages.getX0()).thenReturn(getFirstRandomMessage());
        Mockito.when(randomMessages.getX1()).thenReturn(getSecondRandomMessage());
        return randomMessages;
    }

    public BigInteger getFirstRandomMessage(){
        return new BigInteger("97875698253456686453645637634456983248");
    }

    public BigInteger getSecondRandomMessage(){
        return new BigInteger("984357632832795328724553245376341098798");
    }

    public PublicKeyRSADto mockPublicKey(){
        PublicKeyRSADto publicKey=Mockito.mock(PublicKeyRSADto.class);
        Mockito.when(publicKey.getE()).thenReturn(getE());
        Mockito.when(publicKey.getN()).thenReturn(getN());
        return publicKey;
    }

    public BigInteger getN(){
        return new BigInteger("7");
    }

    public BigInteger getE(){
        return new BigInteger("453243498872693259824678397584326578324658237220925872346920843587326456");
    }

    public EncryptedSecretPairDto mockEncryptedSecrets(){
        EncryptedSecretPairDto mockedEncryptedSecrets=Mockito.mock(EncryptedSecretPairDto.class);
        Mockito.when(mockedEncryptedSecrets.getEncM0()).thenReturn(getFirstEncryptedSecret());
        Mockito.when(mockedEncryptedSecrets.getEncM1()).thenReturn(getSecondEncryptedSecret());
        return mockedEncryptedSecrets;
    }

    public BigInteger getFirstEncryptedSecret(){
        return new BigInteger("987384276234534235325343214344556546432");
    }

    public BigInteger getSecondEncryptedSecret(){
        return new BigInteger("4539843437274320932765432984326098743298");
    }

    public Sender mockSender(){
        Sender mockedSender=Mockito.mock(Sender.class);
        RandomMessagePairDto randomMessages=mockRandomMessagePair();
        Mockito.when(mockedSender.getRandomMessagePair()).thenReturn(randomMessages);
        PublicKeyRSADto publicKey=mockPublicKey();
        Mockito.when(mockedSender.getPublicKeyRsa()).thenReturn(publicKey);
        EncryptedSecretPairDto encryptedSecrets=mockEncryptedSecrets();
        Mockito.when(mockedSender.getEncryptedSecretPair()).thenReturn(encryptedSecrets);
        return mockedSender;
    }
}
