/*
 * Copyright 2015-2016 Erlu Shang (sel8616@gmail.com/philshang@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.sel.shc.core;

import cn.sel.shc.constant.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Singleton/Less configuration->To be Simple!
 *
 * @see ResponseHandler
 */
public final class HttpClient
{
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClient.class);
    private static final String PROTOCOL_HTTP = "HTTP";
    private static final String PROTOCOL_HTTPS = "HTTPS";
    private static final int DEFAULT_TIMEOUT_CONN = 5000;
    private static final int DEFAULT_TIMEOUT_READ = 10000;
    private static final boolean DEFAULT_USE_CACHES = false;
    private static final String DEFAULT_REQUEST_ENCODING = Charset.defaultCharset().name();
    private final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();

    private HttpClient()
    {
    }

    /**
     * Get the singleton instance.
     *
     * @return {@link SingletonHolder#INSTANCE}
     */
    public static HttpClient getInstance()
    {
        return SingletonHolder.INSTANCE;
    }

    /**
     * Create a holder to cache custom headers and connection attributes.
     *
     * @return {@link RequestHolder}
     */
    public RequestHolder prepare()
    {
        return new RequestHolder();
    }

    /**
     * @see #sendHttpRequest(int, String, RequestMethod, Map, ResponseHandler, String, Map, Map, int, int, boolean)
     */
    public void get(int requestId, String url, Map<String, Object> parameters, ResponseHandler handler)
    {
        sendHttpRequest(requestId, url, RequestMethod.GET, parameters, handler, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    /**
     * @see #sendHttpRequest(int, String, RequestMethod, Map, ResponseHandler, String, Map, Map, int, int, boolean)
     */
    public void get(int requestId, String url, ResponseHandler handler)
    {
        sendHttpRequest(requestId, url, RequestMethod.GET, null, handler, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    /**
     * @see #sendHttpRequest(int, String, RequestMethod, Map, ResponseHandler, String, Map, Map, int, int, boolean)
     */
    public void post(int requestId, String url, Map<String, Object> parameters, ResponseHandler handler)
    {
        sendHttpRequest(requestId, url, RequestMethod.POST, parameters, handler, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    /**
     * @see #sendHttpRequest(int, String, RequestMethod, Map, ResponseHandler, String, Map, Map, int, int, boolean)
     */
    public void post(int requestId, String url, ResponseHandler handler)
    {
        sendHttpRequest(requestId, url, RequestMethod.POST, null, handler, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    /**
     * @see #sendHttpRequest(int, String, RequestMethod, Map, ResponseHandler, String, Map, Map, int, int, boolean)
     */
    public void put(int requestId, String url, Map<String, Object> parameters, ResponseHandler handler)
    {
        sendHttpRequest(requestId, url, RequestMethod.PUT, parameters, handler, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    /**
     * @see #sendHttpRequest(int, String, RequestMethod, Map, ResponseHandler, String, Map, Map, int, int, boolean)
     */
    public void put(int requestId, String url, ResponseHandler handler)
    {
        sendHttpRequest(requestId, url, RequestMethod.PUT, null, handler, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    /**
     * @see #sendHttpRequest(int, String, RequestMethod, Map, ResponseHandler, String, Map, Map, int, int, boolean)
     */
    public void delete(int requestId, String url, Map<String, Object> parameters, ResponseHandler handler)
    {
        sendHttpRequest(requestId, url, RequestMethod.DELETE, parameters, handler, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    /**
     * @see #sendHttpRequest(int, String, RequestMethod, Map, ResponseHandler, String, Map, Map, int, int, boolean)
     */
    public void delete(int requestId, String url, ResponseHandler handler)
    {
        sendHttpRequest(requestId, url, RequestMethod.DELETE, null, handler, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    /**
     * Send the HTTP request
     *
     * @param requestId       An integer value to identify the request.
     * @param urlString       [NonNull] The string of the host urlString.
     * @param requestMethod   [NonNull] {@link RequestMethod}
     * @param parameters      [Nullable] A Map that contains some parameters.
     * @param responseHandler [NonNull] Implementation of {@link ResponseHandler}
     * @param requestEncoding [Nullable] Encoding for the request's parameters.
     * @param setHeaders      [Nullable] A Map that contains some http headers which should be set to the request.
     * @param addHeaders      [Nullable] A Map that contains some http headers which should be added to the request.
     * @param timeoutConn     Connection timeout(ms).
     * @param timeoutRead     Data timeout(ms).
     * @param ifUseCaches     Use caches or not.
     */
    void sendHttpRequest(int requestId, String urlString, RequestMethod requestMethod, Map<String, Object> parameters, ResponseHandler responseHandler, String requestEncoding, Map<String, String> setHeaders, Map<String, List<String>> addHeaders, int timeoutConn, int timeoutRead, boolean ifUseCaches)
    {
        Objects.requireNonNull(urlString);
        Objects.requireNonNull(requestMethod);
        Objects.requireNonNull(responseHandler);
        THREAD_POOL.submit(()->
        {
            HttpURLConnection connection = null;
            HttpResponse httpResponse = new HttpResponse();
            try
            {
                boolean isBodyNeeded = requestMethod == RequestMethod.POST || requestMethod == RequestMethod.PUT;
                String requestData = createRequestData(parameters, requestEncoding);
                URL url = createUrl(urlString, requestMethod, requestData);
                connection = createConnection(url);
                setConnectionAttributes(connection, requestMethod.name(), timeoutConn, timeoutRead, isBodyNeeded, !isBodyNeeded && ifUseCaches);
                fillRequestHeaders(connection, setHeaders, addHeaders);
                fillRequestBody(connection, isBodyNeeded, requestData, requestEncoding);
                connection.connect();
                readResponse(connection, httpResponse, url);
            } catch(Exception e)
            {
                httpResponse.setContentBytes(e.getMessage().getBytes());
                LOGGER.error(String.format("Request(%s) to (%s) failed!", requestMethod, urlString), e);
            } finally
            {
                responseHandler.handleResponse(requestId, httpResponse);
                if(connection != null)
                {
                    connection.disconnect();
                }
            }
        });
    }

    /**
     * Get encoded string for the specified object using the specified encoding.
     *
     * @param object   The object.
     * @param encoding The encoding name.
     *
     * @return Encoded string.
     *
     * @throws UnsupportedEncodingException
     */
    private String encode(Object object, String encoding)
            throws UnsupportedEncodingException
    {
        return URLEncoder.encode(object == null ? "" : object.toString(), encoding);
    }

    /**
     * Prepare the request data which looks like 'name1=value1&name2=value2' with the given args and encoding.
     */
    private String createRequestData(Map<String, Object> args, String encoding)
            throws UnsupportedEncodingException
    {
        if(args != null)
        {
            int size = args.size();
            if(size > 0)
            {
                StringBuilder stringBuilder = new StringBuilder(size * 10);
                if(encoding == null || encoding.isEmpty())
                {
                    encoding = Charset.defaultCharset().name();
                }
                Iterator<Map.Entry<String, Object>> iterator = args.entrySet().iterator();
                Map.Entry<String, Object> arg = iterator.next();
                String key = arg.getKey();
                Object value = arg.getValue();
                if(key != null && !key.isEmpty())
                {
                    stringBuilder.append(key).append('=').append(encode(value, encoding));
                }
                while(iterator.hasNext())
                {
                    arg = iterator.next();
                    key = arg.getKey();
                    value = arg.getValue();
                    if(key != null && !key.isEmpty())
                    {
                        stringBuilder.append('&').append(key).append('=').append(encode(value, encoding));
                    }
                }
                return stringBuilder.toString();
            }
        }
        return null;
    }

    /**
     * Create an {@link URL} object depending on the given args.
     */
    private URL createUrl(String urlString, RequestMethod requestMethod, String requestData)
            throws MalformedURLException
    {
        URL url = null;
        switch(requestMethod)
        {
            case GET:
            case DELETE:
                url = new URL(requestData != null && !requestData.isEmpty() ? urlString + '?' + requestData : urlString);
                break;
            case POST:
            case PUT:
                url = new URL(urlString);
                break;
        }
        return url;
    }

    /**
     * Open and return the connection referred to the given url.
     */
    private HttpURLConnection createConnection(URL url)
            throws IOException
    {
        HttpURLConnection connection;
        String protocol = url.getProtocol();
        if(PROTOCOL_HTTP.equalsIgnoreCase(protocol))
        {
            connection = (HttpURLConnection)url.openConnection();
        } else if(PROTOCOL_HTTPS.equalsIgnoreCase(protocol))
        {
            connection = (HttpsURLConnection)url.openConnection();
        } else
        {
            throw new ProtocolException(String.format("Unsupported protocol '%s'!", protocol));
        }
        return connection;
    }

    /**
     * Set the attributes of the prepared connection.
     */
    private void setConnectionAttributes(HttpURLConnection connection, String requestMethod, int timeoutConn, int timeoutRead, boolean ifDoOutput, boolean ifUseCaches)
            throws ProtocolException
    {
        connection.setRequestMethod(requestMethod);
        connection.setConnectTimeout(timeoutConn > 0 ? timeoutConn : DEFAULT_TIMEOUT_CONN);
        connection.setReadTimeout(timeoutRead > 0 ? timeoutRead : DEFAULT_TIMEOUT_READ);
        connection.setDoInput(true);
        connection.setDoOutput(ifDoOutput);
        connection.setUseCaches(ifUseCaches);
    }

    /**
     * Fill the specified request headers of the prepared connection.
     */
    private void fillRequestHeaders(HttpURLConnection connection, Map<String, String> setHeaders, Map<String, List<String>> addHeaders)
    {
        if(addHeaders != null)
        {
            for(Map.Entry<String, List<String>> entry : addHeaders.entrySet())
            {
                String key = entry.getKey();
                List<String> values = entry.getValue();
                for(String value : values)
                {
                    connection.addRequestProperty(key, value);
                }
            }
        }
        if(setHeaders != null)
        {
            for(Map.Entry<String, String> entry : setHeaders.entrySet())
            {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Fill the request body with the given data.
     */
    private void fillRequestBody(HttpURLConnection connection, boolean bodyNeeded, String requestData, String requestEncoding)
            throws IOException
    {
        if(bodyNeeded)
        {
            if(requestData != null && !requestData.isEmpty())
            {
                OutputStream out = connection.getOutputStream();
                out.write(requestData.getBytes(requestEncoding));
                out.flush();
                out.close();
            }
        }
    }

    /**
     * Read all info and data from the httpResponse.
     */
    private void readResponse(HttpURLConnection connection, HttpResponse httpResponse, URL url)
            throws IOException
    {
        httpResponse.setUrl(url);
        httpResponse.setStatusCode(connection.getResponseCode());
        httpResponse.setResponseMessage(connection.getResponseMessage());
        httpResponse.setContentType(connection.getContentType());
        httpResponse.setContentEncoding(connection.getContentEncoding());
        httpResponse.setContentLength(connection.getContentLength());
        httpResponse.setIfModifiedSince(connection.getIfModifiedSince());
        httpResponse.setLastModified(connection.getLastModified());
        httpResponse.setIfNoneMatch(connection.getHeaderField("If-None-Match"));
        httpResponse.setETag(connection.getHeaderField("ETag"));
        httpResponse.setDate(connection.getDate());
        httpResponse.setExpires(connection.getExpiration());
        httpResponse.setHeaders(connection.getHeaderFields());
        InputStream inputStream = httpResponse.getStatusCode() == HttpURLConnection.HTTP_OK ? connection.getInputStream() : connection.getErrorStream();
        if(inputStream != null)
        {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while((len = inputStream.read(buffer)) != -1)
            {
                outputStream.write(buffer, 0, len);
            }
            httpResponse.setContentBytes(outputStream.toByteArray());
            if(httpResponse.getContentLength() < 0)
            {
                httpResponse.setContentLength(httpResponse.getContentBytes().length);
            }
            outputStream.close();
            inputStream.close();
        }
    }

    /**
     * Singleton instance holder
     */
    private static class SingletonHolder
    {
        private static final HttpClient INSTANCE = new HttpClient();
    }
}