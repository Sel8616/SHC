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
package cn.sel.shc.main;

import cn.sel.shc.constant.RequestContentType;
import cn.sel.shc.constant.RequestMethod;
import cn.sel.shc.object.RequestArg;
import cn.sel.shc.object.UploadData;
import cn.sel.shc.object.UploadFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimetypesFileTypeMap;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Singleton/Less configuration->To be Simple!
 *
 * @see Requester,Uploader,ResponseHandler
 */
public final class HttpClient implements Requester, Uploader
{
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClient.class);
    private static final String PROTOCOL_HTTP = "HTTP";
    private static final String PROTOCOL_HTTPS = "HTTPS";
    private static final int DEFAULT_TIMEOUT_CONN = 5000;
    private static final int DEFAULT_TIMEOUT_READ = 10000;
    private static final boolean DEFAULT_USE_CACHES = false;
    private static final String DEFAULT_REQUEST_ENCODING = Charset.defaultCharset().name();
    private final ExecutorService POOL_NORMAL = Executors.newCachedThreadPool();
    private final ExecutorService POOL_UPLOAD = Executors.newCachedThreadPool();

    public static HttpClient getInstance()
    {
        return SingletonHolder.INSTANCE;
    }

    private HttpClient()
    {
    }

    @Override
    public void get(int requestId, String url, List<RequestArg> argList, ResponseHandler responseHandler)
    {
        sendHttpRequest(requestId, url, RequestMethod.GET, argList, responseHandler, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void get(int requestId, String url, ResponseHandler handler)
    {
        sendHttpRequest(requestId, url, RequestMethod.GET, null, handler, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void get(int requestId, String url, List<RequestArg> argList)
    {
        sendHttpRequest(requestId, url, RequestMethod.GET, argList, null, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void get(int requestId, String url)
    {
        sendHttpRequest(requestId, url, RequestMethod.GET, null, null, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void post(int requestId, String url, List<RequestArg> argList, ResponseHandler responseHandler)
    {
        sendHttpRequest(requestId, url, RequestMethod.POST, argList, responseHandler, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void post(int requestId, String url, ResponseHandler handler)
    {
        sendHttpRequest(requestId, url, RequestMethod.POST, null, handler, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void post(int requestId, String url, List<RequestArg> argList)
    {
        sendHttpRequest(requestId, url, RequestMethod.POST, argList, null, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void post(int requestId, String url)
    {
        sendHttpRequest(requestId, url, RequestMethod.POST, null, null, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void put(int requestId, String url, List<RequestArg> argList, ResponseHandler responseHandler)
    {
        sendHttpRequest(requestId, url, RequestMethod.PUT, argList, responseHandler, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void put(int requestId, String url, ResponseHandler handler)
    {
        sendHttpRequest(requestId, url, RequestMethod.PUT, null, handler, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void put(int requestId, String url, List<RequestArg> argList)
    {
        sendHttpRequest(requestId, url, RequestMethod.PUT, argList, null, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void put(int requestId, String url)
    {
        sendHttpRequest(requestId, url, RequestMethod.PUT, null, null, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void delete(int requestId, String url, List<RequestArg> argList, ResponseHandler responseHandler)
    {
        sendHttpRequest(requestId, url, RequestMethod.DELETE, argList, responseHandler, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void delete(int requestId, String url, ResponseHandler handler)
    {
        sendHttpRequest(requestId, url, RequestMethod.DELETE, null, handler, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void delete(int requestId, String url, List<RequestArg> argList)
    {
        sendHttpRequest(requestId, url, RequestMethod.DELETE, argList, null, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void delete(int requestId, String url)
    {
        sendHttpRequest(requestId, url, RequestMethod.DELETE, null, null, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void uploadFileList(int requestId, String url, List<UploadFile> fileList, ResponseHandler responseHandler)
    {
        sendUploadFileRequest(requestId, url, fileList, responseHandler, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ);
    }

    @Override
    public void uploadFileList(int requestId, String url, List<UploadFile> fileList)
    {
        sendUploadFileRequest(requestId, url, fileList, null, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ);
    }

    @Override
    public void uploadDataList(int requestId, String url, List<UploadData> dataList, ResponseHandler responseHandler)
    {
        sendUploadDataRequest(requestId, url, dataList, responseHandler, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ);
    }

    @Override
    public void uploadDataList(int requestId, String url, List<UploadData> dataList)
    {
        sendUploadDataRequest(requestId, url, dataList, null, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ);
    }

    @Override
    public void uploadFile(int requestId, String url, UploadFile file, ResponseHandler responseHandler)
    {
        sendUploadFileRequest(requestId, url, Collections.singletonList(file), responseHandler, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ);
    }

    @Override
    public void uploadFile(int requestId, String url, UploadFile file)
    {
        sendUploadFileRequest(requestId, url, Collections.singletonList(file), null, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ);
    }

    @Override
    public void uploadData(int requestId, String url, UploadData data, ResponseHandler responseHandler)
    {
        sendUploadDataRequest(requestId, url, Collections.singletonList(data), responseHandler, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ);
    }

    @Override
    public void uploadData(int requestId, String url, UploadData data)
    {
        sendUploadDataRequest(requestId, url, Collections.singletonList(data), null, DEFAULT_REQUEST_ENCODING, null, null, DEFAULT_TIMEOUT_CONN, DEFAULT_TIMEOUT_READ);
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
     * Send the HTTP request
     *
     * @param requestId       The identification number of this request.
     * @param url             [NonNull] The string of the host url.
     * @param requestMethod   [NonNull] {@link RequestMethod}
     * @param argList         [Nullable] A Map that contains some argList.
     * @param responseHandler [Nullable] Implementation of {@link ResponseHandler}
     * @param requestEncoding [Nullable] Encoding for the request's argList.
     * @param setHeaders      [Nullable] A Map that contains some http headers which should be set to the request.
     * @param addHeaders      [Nullable] A Map that contains some http headers which should be added to the request.
     * @param timeoutConn     Connection timeout(ms).
     * @param timeoutRead     Read operation timeout(ms).
     * @param ifUseCaches     Use caches or not.
     */
    void sendHttpRequest(int requestId, String url, RequestMethod requestMethod, List<RequestArg> argList, ResponseHandler responseHandler, String requestEncoding, Map<String, String> setHeaders, Map<String, List<String>> addHeaders, int timeoutConn, int timeoutRead, boolean ifUseCaches)
    {
        Objects.requireNonNull(url);
        Objects.requireNonNull(requestMethod);
        boolean handleResponse = responseHandler != null;
        POOL_NORMAL.submit(()->
        {
            HttpResponse httpResponse = null;
            if(handleResponse)
            {
                httpResponse = new HttpResponse();
                preHandleRequest(requestId, url, responseHandler, requestMethod, httpResponse);
            }
            HttpURLConnection connection = null;
            try
            {
                boolean isBodyNeeded = requestMethod == RequestMethod.POST || requestMethod == RequestMethod.PUT;
                RequestContentType contentType = isBodyNeeded ? RequestContentType.FORM_URL_ENCODED : null;
                String requestData = createRequestData(argList, requestEncoding);
                String fullUrl = createFullUrl(url, requestMethod, requestData);
                connection = createConnection(fullUrl);
                setConnAttributes(connection, requestMethod.name(), timeoutConn, timeoutRead, isBodyNeeded, !isBodyNeeded && ifUseCaches);
                fillRequestHeaders(connection, contentType, setHeaders, addHeaders);
                fillRequestBody(connection, isBodyNeeded, requestData, requestEncoding);
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
                    httpResponse.setContentBytes(e.getMessage().getBytes());
                }
                LOGGER.error(String.format("[SHC]Request(#%d#[%s]->%s) failed!", requestId, requestMethod, url), e);
            } finally
            {
                if(handleResponse)
                {
                    responseHandler.handleResponse(requestId, httpResponse);
                }
                if(connection != null)
                {
                    connection.disconnect();
                    connection = null;
                }
            }
        });
    }

    void sendUploadFileRequest(int requestId, String url, List<UploadFile> files, ResponseHandler responseHandler, String requestEncoding, Map<String, String> setHeaders, Map<String, List<String>> addHeaders, int timeoutConn, int timeoutRead)
    {
        Objects.requireNonNull(url);
        RequestMethod requestMethod = RequestMethod.POST;
        boolean handleResponse = responseHandler != null;
        POOL_UPLOAD.submit(()->
        {
            HttpResponse httpResponse = null;
            if(handleResponse)
            {
                httpResponse = new HttpResponse();
                preHandleRequest(requestId, url, responseHandler, requestMethod, httpResponse);
            }
            HttpURLConnection connection = null;
            try
            {
                connection = createConnection(url);
                setConnAttributes(connection, requestMethod.name(), timeoutConn, timeoutRead, true, false);
                fillRequestHeaders(connection, RequestContentType.FORM_MULTI_PART, setHeaders, addHeaders);
                writeUploadFile(connection, files, RequestContentType.FORM_MULTI_PART.boundary(), requestEncoding);
                LOGGER.info(String.format("[SHC]Request(%d) [%s] -> %s", requestId, requestMethod, url));
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
                    httpResponse.setContentBytes(e.getMessage().getBytes());
                }
                LOGGER.error(String.format("[SHC]Request(#%d#[%s]->%s) failed!", requestId, requestMethod, url), e);
            } finally
            {
                if(handleResponse)
                {
                    responseHandler.handleResponse(requestId, httpResponse);
                }
                if(connection != null)
                {
                    connection.disconnect();
                    connection = null;
                }
            }
        });
    }

    void sendUploadDataRequest(int requestId, String url, List<UploadData> dataList, ResponseHandler responseHandler, String requestEncoding, Map<String, String> setHeaders, Map<String, List<String>> addHeaders, int timeoutConn, int timeoutRead)
    {
        Objects.requireNonNull(url);
        RequestMethod requestMethod = RequestMethod.POST;
        boolean handleResponse = responseHandler != null;
        POOL_UPLOAD.submit(()->
        {
            HttpResponse httpResponse = null;
            if(handleResponse)
            {
                httpResponse = new HttpResponse();
                preHandleRequest(requestId, url, responseHandler, requestMethod, httpResponse);
            }
            HttpURLConnection connection = null;
            try
            {
                connection = createConnection(url);
                setConnAttributes(connection, requestMethod.name(), timeoutConn, timeoutRead, true, false);
                fillRequestHeaders(connection, RequestContentType.FORM_MULTI_PART, setHeaders, addHeaders);
                writeUploadData(connection, dataList, RequestContentType.FORM_MULTI_PART.boundary(), requestEncoding);
                LOGGER.info(String.format("[SHC]Request(%d) [%s] -> %s", requestId, requestMethod, url));
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
                    httpResponse.setContentBytes(e.getMessage().getBytes());
                }
                LOGGER.error(String.format("[SHC]Request(#%d#[%s]->%s) failed!", requestId, requestMethod, url), e);
            } finally
            {
                if(handleResponse)
                {
                    responseHandler.handleResponse(requestId, httpResponse);
                }
                if(connection != null)
                {
                    connection.disconnect();
                    connection = null;
                }
            }
        });
    }

    /**
     * Refuse the request if there is another one identified by the same id is in process.
     */
    private void preHandleRequest(int requestId, String url, ResponseHandler responseHandler, RequestMethod requestMethod, HttpResponse httpResponse)
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

    private String encode(Object object, String encoding)
            throws UnsupportedEncodingException
    {
        return URLEncoder.encode(object == null ? "" : String.valueOf(object), encoding);
    }

    /**
     * Prepare the request data(query strings) which looks like 'name1=value1&name2=value2' with the given args and requestEncoding.
     */
    private String createRequestData(List<RequestArg> args, String requestEncoding)
            throws UnsupportedEncodingException
    {
        if(args != null)
        {
            int size = args.size();
            if(size > 0)
            {
                StringBuilder stringBuilder = new StringBuilder(size * 10);
                if(requestEncoding == null || requestEncoding.isEmpty())
                {
                    requestEncoding = DEFAULT_REQUEST_ENCODING;
                }
                Iterator<RequestArg> iterator = args.iterator();
                RequestArg requestArg = iterator.next();
                String name = requestArg.getName();
                Object value = requestArg.getValue();
                if(name != null && !name.isEmpty())
                {
                    stringBuilder.append(name).append('=').append(encode(value, requestEncoding));
                }
                while(iterator.hasNext())
                {
                    requestArg = iterator.next();
                    name = requestArg.getName();
                    value = requestArg.getValue();
                    if(name != null && !name.isEmpty())
                    {
                        stringBuilder.append('&').append(name).append('=').append(encode(value, requestEncoding));
                    }
                }
                return stringBuilder.toString();
            }
        }
        return null;
    }

    /**
     * For GET and DELETE requests, the FULL url = url + ? + query string.
     */
    private String createFullUrl(String url, RequestMethod requestMethod, String requestData)
    {
        String fullUrl;
        switch(requestMethod)
        {
            case GET:
            case DELETE:
                fullUrl = requestData != null && !requestData.isEmpty() ? url + '?' + requestData : url;
                break;
            case POST:
            case PUT:
                fullUrl = url;
                break;
            default:
                throw new IllegalStateException("Unexpected data flow!");
        }
        return fullUrl;
    }

    /**
     * Open and return the connection referred to the given url.
     */
    private HttpURLConnection createConnection(String url)
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
    private void setConnAttributes(HttpURLConnection connection, String requestMethod, int timeoutConn, int timeoutRead, boolean ifDoOutput, boolean ifUseCaches)
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
    private void fillRequestHeaders(HttpURLConnection connection, RequestContentType contentType, Map<String, String> setHeaders, Map<String, List<String>> addHeaders)
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

    /**
     * Write multi-part form data.
     */
    private void writeUploadFile(HttpURLConnection connection, List<UploadFile> files, String boundary, String encoding)
            throws IOException
    {
        if(files != null && !files.isEmpty())
        {
            if(encoding == null || encoding.isEmpty())
            {
                encoding = DEFAULT_REQUEST_ENCODING;
            }
            MimetypesFileTypeMap mimeMapper = new MimetypesFileTypeMap();
            OutputStream connOutputStream = null;
            try
            {
                connOutputStream = connection.getOutputStream();
                String filename;
                File file;
                FileInputStream fileInputStream = null;
                for(UploadFile uploadFile : files)
                {
                    filename = uploadFile.getFilename();
                    file = uploadFile.getFile();
                    String partHeader = createPartHeader(boundary, filename, mimeMapper.getContentType(file));
                    connOutputStream.write(partHeader.getBytes(encoding));
                    try
                    {
                        fileInputStream = new FileInputStream(file);
                        byte[] buffer = new byte[8192];
                        int count;
                        while((count = fileInputStream.read(buffer)) != -1)
                        {
                            connOutputStream.write(buffer, 0, count);
                        }
                        fileInputStream.close();
                    } finally
                    {
                        if(fileInputStream != null)
                        {
                            fileInputStream.close();
                        }
                    }
                    connOutputStream.write("\r\n".getBytes());
                }
                String lastBoundary = createLastBoundary(boundary);
                connOutputStream.write(lastBoundary.getBytes(encoding));
                connOutputStream.flush();
            } finally
            {
                if(connOutputStream != null)
                {
                    connOutputStream.close();
                }
            }
        }
    }

    /**
     * Write multi-part form data.
     */
    private void writeUploadData(HttpURLConnection connection, List<UploadData> files, String boundary, String encoding)
            throws IOException
    {
        if(files != null && !files.isEmpty())
        {
            if(encoding == null || encoding.isEmpty())
            {
                encoding = DEFAULT_REQUEST_ENCODING;
            }
            OutputStream out = connection.getOutputStream();
            String filename;
            for(UploadData uploadData : files)
            {
                filename = uploadData.getFilename();
                byte[] data = uploadData.getData();
                String partHeader = createPartHeader(boundary, filename, "text/plain");
                out.write(partHeader.getBytes(encoding));
                out.write(data);
                out.write("\r\n".getBytes());
            }
            String lastBoundary = createLastBoundary(boundary);
            out.write(lastBoundary.getBytes(encoding));
            out.flush();
            out.close();
        }
    }

    private String createPartHeader(String boundary, String filename, String contentType)
    {
        return "--" + boundary +
                "\r\n" +
                "Content-Disposition: form-data; name=\"" + filename + "\"; filename=\"" + filename + '"' +
                "\r\n" +
                "Content-Type: " + contentType +
                "\r\n\r\n";
    }

    private String createLastBoundary(String boundary)
    {
        return String.format("--%s--\r\n", boundary);
    }

    /**
     * Read all info and data from the httpResponse.
     */
    private void readResponse(HttpURLConnection connection, HttpResponse httpResponse, String url)
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

    /**
     * Singleton instance holder
     */
    private static class SingletonHolder
    {
        private static final HttpClient INSTANCE = new HttpClient();
    }
}