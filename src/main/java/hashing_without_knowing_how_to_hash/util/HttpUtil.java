package main.java.hashing_without_knowing_how_to_hash.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import localhost.hashing_without_knowing_how_to_hash.constants.ControllerParameters;
import localhost.hashing_without_knowing_how_to_hash.constants.ControllerPaths;
import localhost.hashing_without_knowing_how_to_hash.dto.WireAddressDto;
import localhost.hashing_without_knowing_how_to_hash.dto.circuit.CircuitsContainerDto;
import localhost.hashing_without_knowing_how_to_hash.dto.ot.*;
import localhost.hashing_without_knowing_how_to_hash.dto.party.HostDto;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Handles the http request for transfer of the garbled circuits and the oblivious transfer
 * Is required to access the REST services
 */
@UtilityClass
public class HttpUtil {

    private final String PROTOCOL="http";

    /**
     * Builds the required HTTP URL for communication
     * @param hostDto the host to communicate with
     * @param path details of the service
     * @return the url to run a HTTP GET on
     */
    public String buildBasicUrl(HostDto hostDto, String path){
        UriComponentsBuilder builder = UriComponentsBuilder
                .newInstance()
                .scheme(PROTOCOL)
                .host(hostDto.getHost())
                .port(hostDto.getPort())
                .path(path);

        String basicUrl=builder.build().toUriString();
        return basicUrl;
    }

    /**
     * Builds the required url specifically for HTTP GET on the oblivious transfer service
     * @param host wrapper of host name and port of ObliviousTransferService
     * @param obliviousTransferStep e.g. ControllerPaths.PUBLIC_KEY
     * @param wireAddress the address of the sender communicating with
     * @return the url to run the chosen step of oblivious transfer
     */
    public String buildWireLabelReceiverUrl(HostDto host, String obliviousTransferStep, WireAddressDto wireAddress){
        UriComponentsBuilder builder = UriComponentsBuilder
                .newInstance()
                .scheme(PROTOCOL)
                .host(host.getHost())
                .port(host.getPort())
                .path(ControllerPaths.OBLIVIOUS_TRANSFER)
                .path("/")
                .path(obliviousTransferStep)
                .queryParam(ControllerParameters.CIRCUIT_CONTAINER_HASH, wireAddress.getCircuitsContainerHash())
                .queryParam(ControllerParameters.CIRCUIT_HASH, wireAddress.getCircuitHash())
                .queryParam(ControllerParameters.WIRE_ID, wireAddress.getWireId());

        String wireLabelReceiverUrl=builder.toUriString();
        return wireLabelReceiverUrl;
    }

    /**
     * The general scheme to for a HTTP GET command
     * @param url to run the HTTP GET on
     * @return the response from the server
     */
    private HttpEntity<String> httpGet(String url){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> entity = new HttpEntity<>(httpHeaders);

        HttpEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response;
    }

    /**
     * The general scheme to for a HTTP POST command
     * @param url to POST to using HTTP
     */
    private void httpPost(String url, String jsonObject){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<String>(jsonObject, headers);
        restTemplate.postForObject(url, request, String.class);
    }

    private CircuitsContainerDto instantiateCircuitsContainerDtoFromResponse(HttpEntity<String> response){
        ObjectMapper objectMapper = new ObjectMapper();
        CircuitsContainerDto circuitsContainerDto=null;
        try {
             circuitsContainerDto= objectMapper.readValue(response.getBody(), CircuitsContainerDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            circuitsContainerDto=new CircuitsContainerDto();
        }
        return circuitsContainerDto;
    }

    /**
     * Gets the garbled circuits wrapped in a CircuitsContainerDto from the CircuitService of the specified host
     * @param hostDto the host to receive the CircuitsContainerDto from
     * @return the requested instance of CircuitsContainerDto
     */
    public CircuitsContainerDto httpGetCircuitsContainerDto(HostDto hostDto) {
        String url=buildBasicUrl(hostDto, ControllerPaths.CIRCUIT);
        HttpEntity<String> response = httpGet(url);
        CircuitsContainerDto circuitsContainerDto=instantiateCircuitsContainerDtoFromResponse(response);
        return circuitsContainerDto;
    }

    private boolean instantiateStatusFromResponse(HttpEntity<String> response){
        ObjectMapper objectMapper = new ObjectMapper();
        Boolean status=null;
        try {
            status= objectMapper.readValue(response.getBody(), Boolean.class);
        } catch (Exception e) {
            e.printStackTrace();
            status=Boolean.FALSE;
        }
        return status.booleanValue();
    }

    /**
     * Gets the status, whether the garbled circuits can already be requested or not
     * @param hostDto the host to communicate with
     * @return *true* if the host is ready to transfer the garbled circuits, else *false*
     */
    public boolean httpGetStatus(HostDto hostDto){
        String url=buildBasicUrl(hostDto, ControllerPaths.CIRCUIT+"/"+ControllerPaths.STATUS);
        HttpEntity<String> response=httpGet(url);
        boolean status=instantiateStatusFromResponse(response);
        return status;
    }

    private PublicKeyRSADto instantiatePublicKeyRSADtoFromResponse(HttpEntity<String> response){
        ObjectMapper objectMapper = new ObjectMapper();
        PublicKeyRSADto publicKey=null;
        try {
            publicKey= objectMapper.readValue(response.getBody(), PublicKeyRSADto.class);
        } catch (Exception e) {
            e.printStackTrace();
            publicKey=new PublicKeyRSADto();
        }
        return publicKey;
    }

    /**
     * The receiver of the oblivious transfer calls this method to receive the public key from sender
     * @param host wrapper of host name and port of the ObliviousTransferService
     * @param wireAddress the address of the specific sender
     * @return wrapper of the public key
     */
    public PublicKeyRSADto httpGetPublicKeyRSADto(HostDto host, WireAddressDto wireAddress){
        String url=buildWireLabelReceiverUrl(host, ControllerPaths.PUBLIC_KEY, wireAddress);
        HttpEntity<String> response=httpGet(url);
        PublicKeyRSADto publicKey=instantiatePublicKeyRSADtoFromResponse(response);
        return publicKey;
    }

    private RandomMessagePairDto instantiateRandomMessagePairDtoFromResponse(HttpEntity<String> response){
        ObjectMapper objectMapper = new ObjectMapper();
        RandomMessagePairDto messagePair=null;
        try {
            messagePair= objectMapper.readValue(response.getBody(), RandomMessagePairDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            messagePair=new RandomMessagePairDto();
        }
        return messagePair;
    }

    /**
     * The receiver of the oblivious transfer calls this method to receive the two random messages from sender
     * @param host wrapper of host name and port of the ObliviousTransferService
     * @param wireAddress the address of the specific sender
     * @return wrapper of the two random messages
     */
    public RandomMessagePairDto httpGetRandomMessagePairDto(HostDto host, WireAddressDto wireAddress){
        String url=buildWireLabelReceiverUrl(host, ControllerPaths.RANDOM_MESSAGE_PAIR, wireAddress);
        HttpEntity<String> response=httpGet(url);
        RandomMessagePairDto randomMessagePairDto=instantiateRandomMessagePairDtoFromResponse(response);
        return randomMessagePairDto;
    }

    private EncryptedSecretPairDto instantiateEncryptedSecretPairDtoFromResponse(HttpEntity<String> response){
        ObjectMapper objectMapper = new ObjectMapper();
        EncryptedSecretPairDto encryptedSecrets=null;
        try {
            encryptedSecrets= objectMapper.readValue(response.getBody(), EncryptedSecretPairDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            encryptedSecrets=new EncryptedSecretPairDto();
        }
        return encryptedSecrets;
    }

    /**
     * The receiver of the oblivious transfer calls this method to receive the encrypted secrets from sender
     * @param host wapper of host name and port of the ObliviousTransferService
     * @param wireAddress the address of the specific sender
     * @return wrapper of the two encrypted secrets
     */
    public EncryptedSecretPairDto httpGetEncryptedSecretPairDto(HostDto host, WireAddressDto wireAddress){
        String url=buildWireLabelReceiverUrl(host, ControllerPaths.ENCRYPTED_SECRET_PAIR, wireAddress);
        HttpEntity<String> response=httpGet(url);
        EncryptedSecretPairDto encryptedSecrets=instantiateEncryptedSecretPairDtoFromResponse(response);
        return encryptedSecrets;
    }

    private String createJSONFrom(VValueDto vValueDto, WireAddressDto wireAddress){
        VValueContainerDto vValueContainerDto =new VValueContainerDto(wireAddress, vValueDto);
        ObjectMapper objectMapper=new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String json = null;
        try{
            json=objectWriter.writeValueAsString(vValueContainerDto);
        }
        catch (Exception e){
            e.printStackTrace();
            json="";
        }
        return json;
    }

    /**
     * The receiver transmits the calculated vValue to the oblivious transfer sender
     * This is done using HTTP POST
     * @param host wrapper of host name and port of the ObliviousTransferService
     * @param vValueDto wrapper of the vValue
     * @param wireAddress to address the actual sender instance
     */
    public void httpPostVValueDto(HostDto host, VValueDto vValueDto,WireAddressDto wireAddress){
        String url=buildWireLabelReceiverUrl(host, ControllerPaths.VVALUE, wireAddress);
        String jsonOfVValueDto=createJSONFrom(vValueDto, wireAddress);
        httpPost(url, jsonOfVValueDto);
    }


}