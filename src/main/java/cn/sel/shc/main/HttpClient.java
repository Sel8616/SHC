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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Singleton/Less configuration->To be Simple!
 *
 * @see DefaultRequestSender , UploadRequestSender ,ResponseHandler
 */
public abstract class HttpClient
{
    protected static final String PROTOCOL_HTTP = "HTTP";
    protected static final String PROTOCOL_HTTPS = "HTTPS";
    protected static final int DEFAULT_TIMEOUT_CONN = 5000;
    protected static final int DEFAULT_TIMEOUT_READ = 10000;
    protected static final boolean DEFAULT_USE_CACHES = false;
    protected static final String DEFAULT_REQUEST_ENCODING = Charset.defaultCharset().name();
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public static DefaultHttpClient getDefault()
    {
        return InstancesHolder._default;
    }

    public static UploadHttpClient getUpload()
    {
        return InstancesHolder._upload;
    }

    protected abstract byte[] getRequestBody(HttpURLConnection connection, String requestEncoding);

    /**
     * Refuse the request if there is another one identified by the same id is in process.
     */
    protected void registerRequest(int requestId, String url, ResponseHandler responseHandler, RequestMethod requestMethod, HttpResponse httpResponse)
    {
        if(responseHandler.isRegistered(requestId))
        {
            String msg = String.format("[SHC]Request(#%d#[%s]->%s) failed! Another request identified by the same id is in process.", requestId, requestMethod, url);
            LOGGER.error(msg);
            httpResponse.setContentBytes(msg.getBytes());
            responseHandler.handleResponse(requestId, httpResponse);
            return;
        }
        responseHandler.register(requestId);
    }

    /**
     * Open and return the connection referred to the given url.
     */
    protected HttpURLConnection createConnection(String url)
            throws IOException
    {
        HttpURLConnection connection;
        URL _url = new URL(url);
        String protocol = _url.getProtocol();
        if(PROTOCOL_HTTP.equalsIgnoreCase(protocol))
        {
            connection = (HttpURLConnection)_url.openConnection();
        } else if(PROTOCOL_HTTPS.equalsIgnoreCase(protocol))
        {
            connection = (HttpsURLConnection)_url.openConnection();
        } else
        {
            throw new ProtocolException(String.format("Unsupported protocol '%s'!", protocol));
        }
        return connection;
    }

    /**
     * Set the attributes of the prepared connection.
     */
    protected void setConnAttributes(HttpURLConnection connection, String requestMethod, int timeoutConn, int timeoutRead, boolean ifDoOutput, boolean ifUseCaches)
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
    protected void fillRequestHeaders(HttpURLConnection connection, RequestContentType contentType, Map<String, String> setHeaders, Map<String, List<String>> addHeaders)
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
        if(contentType != null)
        {
            connection.setRequestProperty("Content-Type", contentType.value());
        }
    }

    /**
     * Read all info and data from the httpResponse.
     */
    protected void readResponse(HttpURLConnection connection, HttpResponse httpResponse, String url)
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
            ByteArrayOutputStream contentSavingStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while((len = inputStream.read(buffer)) != -1)
            {
                contentSavingStream.write(buffer, 0, len);
            }
            httpResponse.setContentBytes(contentSavingStream.toByteArray());
            if(httpResponse.getContentLength() < 0)
            {
                httpResponse.setContentLength(httpResponse.getContentBytes().length);
            }
            contentSavingStream.close();
            inputStream.close();
        }
    }

    protected void checkUrl(String url)
    {
        if(url == null || url.isEmpty())
        {
            throw new IllegalArgumentException("[SHC] Param 'url' must not be null or empty!");
        }
    }

    /**
     * Singleton instances holder
     */
    private static class InstancesHolder
    {
        private static final DefaultHttpClient _default = new DefaultHttpClient();
        private static final UploadHttpClient _upload = new UploadHttpClient();
    }
}