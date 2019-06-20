package com.swire.etsww.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

public abstract class HttpUtil {
	private static final String USER_AGENT = "HttpClient/4.5 PaasAPIUtil";
	private static final Log logger = LogFactory.getLog(HttpUtil.class);

	public static enum DataType {
		JSON, XML, FORM, FILE
	}

	public static class HttpFormFile {
		private String filePath;
		private String fileName;

		public HttpFormFile() {

		}

		public HttpFormFile(String path, String name) {
			this.filePath = path;
			this.fileName = name;
		}

		public String getFilePath() {
			return filePath;
		}

		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	private static HttpEntity bulidHttpEntiry(HttpRequestBase request, DataType dataType, Object data) {
		HttpEntity entiry = null;
		if (dataType == DataType.FORM) {
			request.setHeader("Content-Type", "application/x-www-form-urlencoded");
			if (data != null) {
				if (data instanceof Map<?, ?>) {
					List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
					Map<String, String> bodys = (Map<String, String>) data;
					for (Map.Entry<String, String> body : bodys.entrySet()) {
						NameValuePair nameValuePair = new BasicNameValuePair(body.getKey(),
								String.valueOf(body.getValue()));
						valuePairs.add(nameValuePair);
					}
					entiry = new UrlEncodedFormEntity(valuePairs, Charset.forName("UTF-8"));
				} else {
					throw new RuntimeException("数据类型不正确");
				}
			}
		} else if (dataType == DataType.JSON) {
			request.setHeader("Content-Type", "application/json");
			if (data != null) {
				ContentType type = ContentType.create("application/json", Charset.forName("UTF-8"));
				entiry = new StringEntity(data.toString(), type);
			}
		} else if (dataType == DataType.XML) {
			request.setHeader("Content-Type", "application/xml");
			if (data != null) {
				ContentType type = ContentType.create("application/xml", Charset.forName("UTF-8"));
				entiry = new StringEntity(data.toString(), type);
			}
		} else if (dataType == DataType.FILE) {
			if (data != null) {
				MultipartEntityBuilder multiPart = MultipartEntityBuilder.create();
				if (data instanceof Map<?, ?>) {
					Map<String, ?> files = (Map<String, ?>) data;
					Iterator<?> iterator = files.entrySet().iterator();
					while (iterator.hasNext()) {
						Entry<String, ?> obj = (Entry<String, ?>) iterator.next();
						String fieldName = obj.getKey();
						Object val = obj.getValue();
						if (val instanceof HttpFormFile) {
							HttpFormFile file = (HttpFormFile) val;
							InputStreamBody fileStream = null;
							try {
								fileStream = new InputStreamBody(new FileInputStream(file.getFilePath()),
										file.getFileName());
							} catch (FileNotFoundException e) {
								throw new RuntimeException(e);
							}
							multiPart.addPart(new FormBodyPart(fieldName, fileStream));
						} else {
							StringBody body = null;
							try {
								body = new StringBody(val.toString(), Charset.forName("UTF-8"));
							} catch (Exception e) {
								throw new RuntimeException(e);
							}
							multiPart.addPart(new FormBodyPart(fieldName, body));
						}
					}
					entiry = multiPart.build();
				} else {
					throw new RuntimeException("数据类型不正确");
				}
			}
		}
		return entiry;
	}

	public static String delete(String user, String url) {
		return delete(user, url, null);
	}

	public static String delete(String user, String url, Map<String, String> headers) {
		HttpDelete delete = new HttpDelete(url);
		if (headers != null && headers.size() > 0) {
			for (Map.Entry<String, String> head : headers.entrySet()) {
				delete.addHeader(head.getKey(), head.getValue());
			}
		}
		return http(user, delete, null, null);
	}

	public static String put(String user, String url, DataType dataType, Object data) {
		return put(user, url, null, dataType, data);
	}

	public static String put(String user, String url, Map<String, String> headers, DataType dataType, Object data) {
		HttpPut put = new HttpPut(url);
		if (headers != null && headers.size() > 0) {
			for (Map.Entry<String, String> head : headers.entrySet()) {
				put.addHeader(head.getKey(), head.getValue());
			}
		}
		return http(user, put, dataType, data);
	}

	public static String get(String user, String url) {
		return get(user, url, null);
	}

	public static String get(String user, String url, Map<String, String> headers) {
		HttpGet get = new HttpGet(url);
		if (headers != null && headers.size() > 0) {
			for (Map.Entry<String, String> head : headers.entrySet()) {
				get.addHeader(head.getKey(), head.getValue());
			}
		}
		return http(user, get, null, null);
	}

	public static String post(String user, String url, DataType dataType, Object data) {
		return post(user, url, null, dataType, data);
	}

	public static String post(String user, String url, Map<String, String> headers, DataType dataType, Object data) {
		HttpPost post = new HttpPost(url);
		if (headers != null && headers.size() > 0) {
			for (Map.Entry<String, String> head : headers.entrySet()) {
				post.addHeader(head.getKey(), head.getValue());
			}
		}
		return http(user, post, dataType, data);
	}

	public static String getHttpResponseString(HttpResponse response) {
		try {
			return EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String getHttpResponseString(HttpResponse response, String charset) {
		try {
			return EntityUtils.toString(response.getEntity(), charset);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("serial")
	public static class HttpException extends RuntimeException {
		private String url;
		private int code;
		private String response;

		public HttpException(String url, int code, String response) {
			super(String.format("Error %d on %s", code, url));
			this.url = url;
			this.code = code;
			this.response = response;
		}

		public String getUrl() {
			return url;
		}

		public int getCode() {
			return code;
		}

		public String getResponse() {
			return response;
		}
	}

	private static String http(String user, HttpRequestBase request, DataType dataType, Object data)
			throws HttpException {
		String response = null;
		SSLContext sslContext = null;
		try {
			sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				// 信任所有
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
		HttpClient client =  HttpClients.custom().setSSLSocketFactory(sslsf).build();
		try {
//			request.addHeader(YiheTokenGenerator.getTokenHeaderName(), getSignature(user));
			HttpEntity entiry = bulidHttpEntiry(request, dataType, data);
			if (request instanceof HttpEntityEnclosingRequestBase) {
				((HttpEntityEnclosingRequestBase) request).setEntity(entiry);
			}
			HttpResponse httpResponse = client.execute(request);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
				Header locationHeader = httpResponse.getFirstHeader("location");
				String location = null;
				if (locationHeader != null) {
					location = locationHeader.getValue();
					try {
						request.setURI(new URI(location));
					} catch (URISyntaxException e) {
						throw new RuntimeException(e);
					}
					response = http(user, request, dataType, data);
				}
			} else if (statusCode == HttpStatus.SC_OK) {
				response = getHttpResponseString(httpResponse);
			} else {
				try {
					logger.debug(httpResponse.getAllHeaders());
					response = getHttpResponseString(httpResponse);
					logger.debug(response);
				} catch (Exception e) {

				}
				throw new HttpException(request.getURI().toString(), statusCode, response);
			}
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		} finally {
			request.releaseConnection();
		}
		return response;
	}
}
