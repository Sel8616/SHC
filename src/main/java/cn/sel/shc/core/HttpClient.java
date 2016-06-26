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

import cn.sel.shc.constant.RequestError;
import cn.sel.shc.constant.RequestMethod;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Singleton/Less configuration->To be Simple!
 *
 * @see ResponseHandler
 */
public final class HttpClient
{
    /**
     * Singleton instance holder
     */
    private static class SingletonHolder
    {
        private static final HttpClient INSTANCE = new HttpClient();
    }

    private static final String PROTOCOL_HTTP = "HTTP";
    private static final String PROTOCOL_HTTPS = "HTTPS";
    private static final int DEFAULT_TIMEOUT_CONN = 5000;
    private static final int DEFAULT_TIMEOUT_READ = 10000;
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
     * @see #sendHttpRequest(int, String, RequestMethod, Map, ResponseHandler, String, Map, Map, int, int)
     */
    public void get(int requestId, String url, Map<String, Object> parameters, ResponseHandler handler)
    {
        sendHttpRequest(requestId, url, RequestMethod.GET, parameters, handler, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ);
    }

    /**
     * @see #sendHttpRequest(int, String, RequestMethod, Map, ResponseHandler, String, Map, Map, int, int)
     */
    public void get(int requestId, String url, ResponseHandler handler)
    {
        sendHttpRequest(requestId, url, RequestMethod.GET, null, handler, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ);
    }

    /**
     * @see #sendHttpRequest(int, String, RequestMethod, Map, ResponseHandler, String, Map, Map, int, int)
     */
    public void post(int requestId, String url, Map<String, Object> parameters, ResponseHandler handler)
    {
        sendHttpRequest(requestId, url, RequestMethod.POST, parameters, handler, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ);
    }

    /**
     * @see #sendHttpRequest(int, String, RequestMethod, Map, ResponseHandler, String, Map, Map, int, int)
     */
    public void post(int requestId, String url, ResponseHandler handler)
    {
        sendHttpRequest(requestId, url, RequestMethod.POST, null, handler, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ);
    }

    /**
     * @see #sendHttpRequest(int, String, RequestMethod, Map, ResponseHandler, String, Map, Map, int, int)
     */
    public void put(int requestId, String url, Map<String, Object> parameters, ResponseHandler handler)
    {
        sendHttpRequest(requestId, url, RequestMethod.PUT, parameters, handler, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ);
    }

    /**
     * @see #sendHttpRequest(int, String, RequestMethod, Map, ResponseHandler, String, Map, Map, int, int)
     */
    public void put(int requestId, String url, ResponseHandler handler)
    {
        sendHttpRequest(requestId, url, RequestMethod.PUT, null, handler, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ);
    }

    /**
     * @see #sendHttpRequest(int, String, RequestMethod, Map, ResponseHandler, String, Map, Map, int, int)
     */
    public void delete(int requestId, String url, Map<String, Object> parameters, ResponseHandler handler)
    {
        sendHttpRequest(requestId, url, RequestMethod.DELETE, parameters, handler, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ);
    }

    /**
     * @see #sendHttpRequest(int, String, RequestMethod, Map, ResponseHandler, String, Map, Map, int, int)
     */
    public void delete(int requestId, String url, ResponseHandler handler)
    {
        sendHttpRequest(requestId, url, RequestMethod.DELETE, null, handler, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ);
    }

    /**
     * Send the HTTP request
     *
     * @param requestId   An integer value to identify the request.
     * @param url         [NonNull] The string of the host url.
     * @param method      [NonNull] {@link RequestMethod}
     * @param parameters  [Nullable] A Map that contains some parameters.
     * @param handler     [NonNull] Implementation of {@link ResponseHandler}
     * @param encoding    [Nullable] Encoding for the request's parameters.
     * @param setHeaders  [Nullable] A Map that contains some http headers which should be set to the request.
     * @param addHeaders  [Nullable] A Map that contains some http headers which should be added to the request.
     * @param timeoutConn Connection timeout(ms)
     * @param timeoutRead Data timeout(ms)
     */
    void sendHttpRequest(int requestId, String url, RequestMethod method, Map<String, Object> parameters, ResponseHandler handler, String encoding, Map<String, String> setHeaders, Map<String, List<String>> addHeaders, int timeoutConn, int timeoutRead)
    {
        Objects.requireNonNull(handler);
        THREAD_POOL.submit(()->{
            HttpURLConnection connection = null;
            Response response = new Response();
            response.setStatusCode(0);
            try
            {
                response.setUrl(new URL(url));
                connection = initConnection(url, method, parameters, encoding, timeoutConn, timeoutRead, setHeaders, addHeaders);
                if(connection != null)
                {
                    connection.connect();
                    response.setUrl(connection.getURL());
                    response.setStatusCode(connection.getResponseCode());
                    response.setResponseMessage(connection.getResponseMessage());
                    response.setContentType(connection.getContentType());
                    response.setContentEncoding(connection.getContentEncoding());
                    response.setContentLength(connection.getContentLength());
                    response.setIfModifiedSince(connection.getIfModifiedSince());
                    response.setLastModified(connection.getLastModified());
                    response.setIfNoneMatch(connection.getHeaderField("If-None-Match"));
                    response.setETag(connection.getHeaderField("ETag"));
                    response.setDate(connection.getDate());
                    response.setExpires(connection.getExpiration());
                    response.setHeaders(connection.getHeaderFields());
                    InputStream inputStream = response.getStatusCode() == HttpURLConnection.HTTP_OK ? connection.getInputStream() : connection.getErrorStream();
                    if(inputStream != null)
                    {
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int len;
                        while((len = inputStream.read(buffer)) != -1)
                        {
                            outputStream.write(buffer, 0, len);
                        }
                        response.setContentBytes(outputStream.toByteArray());
                        if(response.getContentLength() < 0)
                        {
                            response.setContentLength(response.getContentBytes().length);
                        }
                        outputStream.close();
                        inputStream.close();
                    }
                } else
                {
                    response.setRequestError(RequestError.INTERNAL);
                }
            } catch(UnsupportedEncodingException e)
            {
                response.setRequestError(RequestError.INVALID_ENCODING);
                e.printStackTrace();
            } catch(MalformedURLException e)
            {
                response.setRequestError(RequestError.INVALID_URL);
                e.printStackTrace();
            } catch(IOException e)
            {
                response.setRequestError(RequestError.NETWORK);
                e.printStackTrace();
            } catch(Exception e)
            {
                response.setRequestError(RequestError.UNKNOWN);
                e.printStackTrace();
            } finally
            {
                handler.handleResponse(requestId, response);
                if(connection != null)
                {
                    connection.disconnect();
                }
            }
        });
    }

    /**
     * Initialize the connection
     *
     * @param urlString       [NonNull] String form of host urlString. A full expression is expected!
     * @param requestMethod   [NonNull] {@link RequestMethod}
     * @param parameters      [Nullable] A Map that contains some parameters.
     * @param requestEncoding [Nullable] Encoding for the request data.
     * @param timeoutConn     Connection timeout(ms)
     * @param timeoutRead     Data timeout(ms)
     * @param setHeaders      [Nullable] A Map that contains some http headers which should be set to the request.
     * @param addHeaders      [Nullable] A Map that contains some http headers which should be added to the request.
     *
     * @return Prepared HttpURLConnection or Null.
     *
     * @throws NoSuchElementException
     * @throws ProtocolException
     * @throws UnsupportedEncodingException
     * @throws MalformedURLException
     */
    private HttpURLConnection initConnection(String urlString, RequestMethod requestMethod, Map<String, Object> parameters, String requestEncoding, int timeoutConn, int timeoutRead, Map<String, String> setHeaders, Map<String, List<String>> addHeaders) throws IOException, UnsupportedEncodingException, MalformedURLException, ProtocolException
    {
        Objects.requireNonNull(urlString);
        Objects.requireNonNull(requestMethod);
        String requestData = createRequestData(parameters, requestEncoding);
        URL url = null;
        switch(requestMethod)
        {
            case GET:
            case DELETE:
                url = new URL(requestData != null && requestData.length() > 0 ? urlString + '?' + requestData : urlString);
                break;
            case POST:
            case PUT:
                url = new URL(urlString);
                break;
        }
        HttpURLConnection connection = null;
        if(PROTOCOL_HTTP.equalsIgnoreCase(url.getProtocol()))
        {
            connection = (HttpURLConnection)url.openConnection();
        } else if(PROTOCOL_HTTPS.equalsIgnoreCase(url.getProtocol()))
        {
            connection = (HttpsURLConnection)url.openConnection();
        }
        if(connection != null)
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
            connection.setRequestMethod(requestMethod.name());
            connection.setConnectTimeout(timeoutConn > 0 ? timeoutConn : DEFAULT_TIMEOUT_CONN);
            connection.setReadTimeout(timeoutRead > 0 ? timeoutRead : DEFAULT_TIMEOUT_READ);
            connection.setDoInput(true);
            if(requestMethod == RequestMethod.POST || requestMethod == RequestMethod.PUT)
            {
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                if(requestData != null && requestData.length() > 0)
                {
                    OutputStream out = connection.getOutputStream();
                    out.write(requestData.getBytes(requestEncoding));
                    out.flush();
                    out.close();
                }
            }
        }
        return connection;
    }

    /**
     * Prepare the request data with the given args in the form of x-www-form-urlencoded.
     *
     * @param args     [Nullable] A Map that contains some args/Empty.
     * @param encoding [Nullable] Encoding for the request data.
     *
     * @return A String like "name1=value1&name2=value2"
     *
     * @throws UnsupportedEncodingException
     */
    private String createRequestData(Map<String, Object> args, String encoding) throws UnsupportedEncodingException
    {
        if(args != null)
        {
            int size = args.size();
            if(size > 0)
            {
                StringBuilder stringBuilder = new StringBuilder(size * 10);
                if(encoding == null || encoding.length() == 0)
                {
                    encoding = Charset.defaultCharset().name();
                }
                Iterator<Map.Entry<String, Object>> iterator = args.entrySet().iterator();
                Map.Entry<String, Object> arg = iterator.next();
                String key = arg.getKey();
                Object value = arg.getValue();
                if(key != null && key.length() > 0)
                {
                    stringBuilder.append(key).append('=').append(encode(value, encoding));
                }
                while(iterator.hasNext())
                {
                    arg = iterator.next();
                    key = arg.getKey();
                    value = arg.getValue();
                    if(key != null && key.length() > 0)
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
     * Get encoded string for the specified object using the specified encoding.
     *
     * @param object   The object.
     * @param encoding The encoding name.
     *
     * @return Encoded string.
     *
     * @throws UnsupportedEncodingException
     */
    private String encode(Object object, String encoding) throws UnsupportedEncodingException
    {
        return URLEncoder.encode(object == null ? "" : object.toString(), encoding);
    }
}