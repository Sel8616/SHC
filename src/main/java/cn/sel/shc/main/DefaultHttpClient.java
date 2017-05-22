/*
 * Copyright 2015-2017 Erlu Shang (sel8616@gmail.com/philshang@163.com)
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
package cn.sel.shc.main;

import cn.sel.shc.constant.RequestContentType;
import cn.sel.shc.constant.RequestMethod;
import cn.sel.shc.object.RequestArgs;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Singleton/Less configuration->To be Simple!
 *
 * @see DefaultRequestSender , UploadRequestSender ,ResponseHandler
 */
public class DefaultHttpClient extends HttpClient implements DefaultRequestSender
{
    private final ExecutorService threadPool = Executors.newCachedThreadPool();

    DefaultHttpClient()
    {
    }

    @Override
    public void get(int requestId, String url, RequestArgs requestArgs, ResponseHandler responseHandler)
    {
        sendHttpRequest(requestId, url, requestArgs, responseHandler, RequestMethod.GET, null, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void get(int requestId, String url, ResponseHandler handler)
    {
        sendHttpRequest(requestId, url, null, handler, RequestMethod.GET, null, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void get(int requestId, String url, RequestArgs requestArgs)
    {
        sendHttpRequest(requestId, url, requestArgs, null, RequestMethod.GET, null, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void get(int requestId, String url)
    {
        sendHttpRequest(requestId, url, null, null, RequestMethod.GET, null, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void post(int requestId, String url, RequestArgs requestArgs, ResponseHandler responseHandler)
    {
        sendHttpRequest(requestId, url, requestArgs, responseHandler, RequestMethod.POST, null, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void post(int requestId, String url, ResponseHandler handler)
    {
        sendHttpRequest(requestId, url, null, handler, RequestMethod.POST, null, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void post(int requestId, String url, RequestArgs requestArgs)
    {
        sendHttpRequest(requestId, url, requestArgs, null, RequestMethod.POST, null, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void post(int requestId, String url)
    {
        sendHttpRequest(requestId, url, null, null, RequestMethod.POST, null, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void put(int requestId, String url, RequestArgs requestArgs, ResponseHandler responseHandler)
    {
        sendHttpRequest(requestId, url, requestArgs, responseHandler, RequestMethod.PUT, null, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void put(int requestId, String url, ResponseHandler handler)
    {
        sendHttpRequest(requestId, url, null, handler, RequestMethod.PUT, null, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void put(int requestId, String url, RequestArgs requestArgs)
    {
        sendHttpRequest(requestId, url, requestArgs, null, RequestMethod.PUT, null, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void put(int requestId, String url)
    {
        sendHttpRequest(requestId, url, null, null, RequestMethod.PUT, null, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void delete(int requestId, String url, RequestArgs requestArgs, ResponseHandler responseHandler)
    {
        sendHttpRequest(requestId, url, requestArgs, responseHandler, RequestMethod.DELETE, null, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void delete(int requestId, String url, ResponseHandler handler)
    {
        sendHttpRequest(requestId, url, null, handler, RequestMethod.DELETE, null, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void delete(int requestId, String url, RequestArgs requestArgs)
    {
        sendHttpRequest(requestId, url, requestArgs, null, RequestMethod.DELETE, null, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void delete(int requestId, String url)
    {
        sendHttpRequest(requestId, url, null, null, RequestMethod.DELETE, null, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    protected byte[] getRequestBody(HttpURLConnection connection, String requestEncoding)
    {
        return new byte[0];
    }

    /**
     * Create a holder to cache custom headers and connection attributes.
     *
     * @return {@link DefaultRequestHolder}
     */
    public DefaultRequestHolder prepare()
    {
        return new DefaultRequestHolder();
    }

    /**
     * Send the HTTP request
     *
     * @param requestId       The identification number of this request.
     * @param url             [NonNull] The string of the host url.
     * @param requestArgs     [Nullable] A Map that contains some requestArgs.
     * @param responseHandler [Nullable] Implementation of {@link ResponseHandler}
     * @param requestMethod   [NonNull] {@link RequestMethod}
     * @param contentType     [Nullable] {@link RequestContentType}
     * @param requestEncoding [Nullable] Encoding for the request's requestArgs.
     * @param setHeaders      [Nullable] A Map that contains some http headers which should be set to the request.
     * @param addHeaders      [Nullable] A Map that contains some http headers which should be added to the request.
     * @param timeoutConn     Connection timeout(ms).
     * @param timeoutRead     Read operation timeout(ms).
     * @param ifUseCaches     Use caches or not.
     */
    protected void sendHttpRequest(int requestId, String url, RequestArgs requestArgs, ResponseHandler responseHandler, RequestMethod requestMethod, RequestContentType contentType, String requestEncoding, Map<String, String> setHeaders, Map<String, List<String>> addHeaders, int timeoutConn, int timeoutRead, boolean ifUseCaches)
    {
        checkUrl(url);
        if(requestMethod == null)
        {
            throw new IllegalArgumentException("[SHC] Param 'requestMethod' must not be null!");
        }
        boolean handleResponse = responseHandler != null;
        threadPool.submit(()->
        {
            HttpResponse httpResponse = null;
            if(handleResponse)
            {
                httpResponse = new HttpResponse();
                registerRequest(requestId, url, responseHandler, requestMethod, httpResponse);
            }
            HttpURLConnection connection = null;
            try
            {
                boolean isPostOrPut = requestMethod == RequestMethod.POST || requestMethod == RequestMethod.PUT;
                RequestContentType requestContentType = contentType != null ? contentType : isPostOrPut ? RequestContentType.FORM_URL_ENCODED : null;
                String requestData = createRequestData(requestArgs, requestEncoding);
                String fullUrl = createFullUrl(url, requestMethod, requestData);
                connection = createConnection(fullUrl);
                setConnAttributes(connection, requestMethod.name(), timeoutConn, timeoutRead, isPostOrPut, !isPostOrPut && ifUseCaches);
                fillRequestHeaders(connection, requestContentType, setHeaders, addHeaders);
                fillRequestBody(connection, isPostOrPut, requestData, requestEncoding);
                connection.connect();
                if(handleResponse)
                {
                    readResponse(connection, httpResponse, url);
                } else
                {
                    connection.getInputStream();
                }
            } catch(Exception e)
            {
                if(handleResponse)
                {
                    httpResponse.setContent(e.getMessage());
                }
                LOGGER.error(String.format("[SHC] Request(#%d#[%s]->%s) failed!", requestId, requestMethod, url), e);
            } finally
            {
                if(handleResponse)
                {
                    responseHandler.handleResponse(requestId, httpResponse);
                }
                if(connection != null)
                {
                    connection.disconnect();
                }
            }
        });
    }

    /**
     * Prepare the request data(query strings) which looks like 'name1=value1&name2=value2' with the given args and requestEncoding.
     */
    private String createRequestData(RequestArgs args, String requestEncoding)
            throws UnsupportedEncodingException
    {
        if(args != null)
        {
            return args.toString(requestEncoding == null || requestEncoding.isEmpty() ? DEFAULT_REQUEST_ENCODING : requestEncoding);
        }
        return null;
    }

    /**
     * For GET and DELETE requests, the FULL url = url + ? + query string.
     */
    private String createFullUrl(String url, RequestMethod requestMethod, String requestData)
    {
        String fullUrl;
        if(requestMethod == RequestMethod.GET || requestMethod == RequestMethod.DELETE)
        {
            fullUrl = requestData != null && !requestData.isEmpty() ? url + '?' + requestData : url;
        } else if(requestMethod == RequestMethod.POST || requestMethod == RequestMethod.PUT)
        {
            fullUrl = url;
        } else
        {
            throw new IllegalStateException("Unexpected data flow!");
        }
        return fullUrl;
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
                OutputStream connOutputStream = null;
                try
                {
                    connOutputStream = connection.getOutputStream();
                    connOutputStream.write(requestData.getBytes(requestEncoding == null || requestEncoding.isEmpty() ? DEFAULT_REQUEST_ENCODING : requestEncoding));
                    connOutputStream.flush();
                    connOutputStream.close();
                } finally
                {
                    if(connOutputStream != null)
                    {
                        connOutputStream.close();
                    }
                }
            }
        }
    }
}