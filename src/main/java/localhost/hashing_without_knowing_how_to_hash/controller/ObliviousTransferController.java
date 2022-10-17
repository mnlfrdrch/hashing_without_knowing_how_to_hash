package localhost.hashing_without_knowing_how_to_hash.controller;

import localhost.hashing_without_knowing_how_to_hash.constants.ControllerParameters;
import localhost.hashing_without_knowing_how_to_hash.constants.ControllerPaths;
import localhost.hashing_without_knowing_how_to_hash.dto.ot.*;
import localhost.hashing_without_knowing_how_to_hash.service.ObliviousTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Enables the oblivious transfer of wire labels to circuits, that have been transferred in advance
 * Implements an oblivious transfer variant using RSA
 * REST is used for the data transmitted
 * Class contains pairs of methods, one for sending and one for receiving
 */
@RestController
@RequestMapping(path= ControllerPaths.OBLIVIOUS_TRANSFER)
public class ObliviousTransferController {

    private ObliviousTransferService obliviousTransferService;

    @Autowired
    public ObliviousTransferController(ObliviousTransferService obliviousTransferService){
        this.obliviousTransferService=obliviousTransferService;
    }

    /**
     * A receiver requests the public key from corresponding receiver
     * 1. step of oblivious transfer
     * @param circuitContainerHash part of sender address
     * @param circuitHash part of sender address
     * @param wireId part of sender address
     * @return RSA public key
     */
    @RequestMapping(value = ControllerPaths.PUBLIC_KEY, method = RequestMethod.GET)
    public PublicKeyRSADto getPublicKey(@RequestParam(ControllerParameters.CIRCUIT_CONTAINER_HASH) int circuitContainerHash, @RequestParam(ControllerParameters.CIRCUIT_HASH) int circuitHash, @RequestParam(ControllerParameters.WIRE_ID) String wireId){
        return obliviousTransferService.getPublicKey(circuitContainerHash, circuitHash, wireId);
    }

    /**
     * A receiver request two random messages from corresponding sender
     * 2. step of oblivious transfer
     * @param circuitContainerHash part of sender address
     * @param circuitHash part of sender address
     * @param wireId part of sender address
     * @return random messages pair
     */
    @RequestMapping(value = ControllerPaths.RANDOM_MESSAGE_PAIR, method = RequestMethod.GET)
    public RandomMessagePairDto getRandomMessages(@RequestParam(ControllerParameters.CIRCUIT_CONTAINER_HASH) int circuitContainerHash, @RequestParam(ControllerParameters.CIRCUIT_HASH) int circuitHash, @RequestParam(ControllerParameters.WIRE_ID) String wireId){
        return obliviousTransferService.getRandomMessagePair(circuitContainerHash, circuitHash, wireId);
    }

    /**
     * Vvalue and corresponding sender address is sent by receiver
     * Then vvalue ist set at sender
     * 3. step of oblivious transfer
     * @param vValueContainerDto contains vvalue as well as sender address
     */
    @PostMapping(value = ControllerPaths.VVALUE)
    public void setVValue(@RequestBody VValueContainerDto vValueContainerDto){
        obliviousTransferService.setVValue(vValueContainerDto);
    }

    /**
     * Receiver requests the encrypted secrets from corresponding sender
     * 4. step of oblivious transfer
     * @param circuitContainerHash part of sender address
     * @param circuitHash part of sender address
     * @param wireId part of sender address
     * @return the encrypted secrets
     */
    @RequestMapping(value = ControllerPaths.ENCRYPTED_SECRET_PAIR, method = RequestMethod.GET)
    public @ResponseBody EncryptedSecretPairDto getEncryptedSecrets(@RequestParam(ControllerParameters.CIRCUIT_CONTAINER_HASH) int circuitContainerHash, @RequestParam(ControllerParameters.CIRCUIT_HASH) int circuitHash, @RequestParam(ControllerParameters.WIRE_ID) String wireId){
        return obliviousTransferService.getEncryptedSecretPair(circuitContainerHash,circuitHash,wireId);
    }

}
