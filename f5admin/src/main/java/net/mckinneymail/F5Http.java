package net.mckinneymail;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.TrustSelfSignedStrategy;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.ssl.SSLContextBuilder;

public class F5Http {

	private final String hostName;
	private final String userName;
	private final char[] password;
	private boolean verbose;

	public F5Http(String hostName, String userName, char[] password, boolean verbose) {
		this.hostName = hostName;
		this.userName = userName;
		this.password = password;
		this.verbose = verbose;
	}

	public String httpPools()
			throws KeyManagementException, ParseException, NoSuchAlgorithmException, KeyStoreException, IOException {
		String uri = absPath( "/mgmt/tm/ltm/pool");
		return httpGet(uri);
	}

	public String httpPoolMembers(String poolName, String memberName) {

		String uri = absPath("/mgmt/tm/ltm/pool/" + poolName + "/members/~Common~" + memberName
				+ "/stats");
		return httpGet(uri);
	}
	
	private String absPath(String url) {
		return "https://" + hostName + url;
	}

	public String httpGet(String relativeUrl) {
		String url = absPath(relativeUrl);
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(new TrustSelfSignedStrategy()).build();
			HostnameVerifier allowAllHosts = new NoopHostnameVerifier();
			SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, allowAllHosts);

			final HttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create()
					.setSSLSocketFactory(sslSocketFactory).build();

			final BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(new AuthScope(null, -1), new UsernamePasswordCredentials(userName, password));
			try (final CloseableHttpClient httpclient = HttpClients.custom()
					.setDefaultCredentialsProvider(credsProvider).setConnectionManager(cm).build()) {
				final HttpGet httpget = new HttpGet(url);

				if(verbose) 
					System.out.println("Request: " + httpget.getMethod() + " " + httpget.getUri());
				try (final CloseableHttpResponse response = httpclient.execute(httpget)) {
					String body = EntityUtils.toString(response.getEntity());
					if(verbose || response.getCode() != 200 ) {
						System.out.println("Response: " + response.getCode() + " " + response.getReasonPhrase());
						System.out.println(body);
					}
					return body;
				}
			}
		} catch (Exception err) {
			throw new RuntimeException(err);
		}
	}
	
	public Response httpPatch(String relativeUrl, String json) {
		String url = absPath(relativeUrl);
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(new TrustSelfSignedStrategy()).build();
			HostnameVerifier allowAllHosts = new NoopHostnameVerifier();
			SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, allowAllHosts);

			final HttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create()
					.setSSLSocketFactory(sslSocketFactory).build();

			final BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(new AuthScope(null, -1), new UsernamePasswordCredentials(userName, password));
			try (final CloseableHttpClient httpclient = HttpClients.custom()
					.setDefaultCredentialsProvider(credsProvider).setConnectionManager(cm).build()) {
				final HttpPatch httpPatch = new HttpPatch(url);
				System.out.println("json: " + json);
				httpPatch.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
				httpPatch.setHeader("Accept", "application/json");
				httpPatch.setHeader("Content-type", "application/json");

				if(verbose) 
					System.out.println("Request: " + httpPatch.getMethod() + " " + httpPatch.getUri());
				try (final CloseableHttpResponse response = httpclient.execute(httpPatch)) {
					
					String responseBody = EntityUtils.toString(response.getEntity());
					if(verbose || response.getCode() != 200 ) {
						System.out.println("Response: " + response.getCode() + " " + response.getReasonPhrase());
						System.out.println(responseBody);
					}
					return new Response(response.getCode(), responseBody);
				}
			}
		} catch (Exception err) {
			throw new RuntimeException(err);
		}
	}

}
