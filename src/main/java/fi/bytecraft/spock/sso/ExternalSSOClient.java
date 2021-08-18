package fi.bytecraft.spock.sso;

import fi.bytecraft.spock.sso.domain.SsoLogin;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@Service
public class ExternalSSOClient {

    private static final String NOT_IMPLEMENTED = "Not implmented yet, hopefully the sub-contractor will provide these operations soon";

    private RestTemplate restTemplate;

    public ExternalSSOClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        restTemplate = getSSLRestTemplate();
    }

    public SsoLogin login(String email, String password) {
        throw new RuntimeException(NOT_IMPLEMENTED);
    }

    public boolean logout(String email) {
        throw new RuntimeException(NOT_IMPLEMENTED);
    }

    public boolean isLoggedIn(String email) {
        throw new RuntimeException(NOT_IMPLEMENTED);
    }

    private RestTemplate getSSLRestTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(csf)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);

        return new RestTemplate(requestFactory);
    }

}
