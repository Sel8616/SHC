package cn.sel.hsharp.core;

import cn.sel.hsharp.constant.RequestError;
import cn.sel.hsharp.constant.RequestMethod;
import cn.sel.hsharp.constant.StandardEncoding;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Singleton/Less configuration->To be Simple!<br/>
 *
 * @see ResponseHandler
 */
public final class HttpClient
{
    //region Constants--------------------------------------------------------------------------------------------------
    /**
     * The ONLY Instance
     */
    private static final HttpClient THE_CLIENT = new HttpClient();
    /**
     * Default size of thread pool
     */
    private static final int DEFAULT_POOL_SIZE = 8;
    /**
     * Default Connection Timeout(ms)
     */
    private static final int DEFAULT_TIMEOUT_CONN = 5000;
    /**
     * Default Read Timeout(ms)
     */
    private static final int DEFAULT_TIMEOUT_READ = 10000;
    /**
     * HTTP protocol name
     */
    public static final String PROTOCOL_HTTP = "HTTP";
    /**
     * HTTPS protocol name
     */
    public static final String PROTOCOL_HTTPS = "HTTPS";
    /**
     * Lock
     */
    private final Object LOCK = new Object();
    //endregion Constants-----------------------------------------------------------------------------------------------
    //region Properties-------------------------------------------------------------------------------------------------
    /**
     * Thread pool
     */
    private static ExecutorService THREAD_POOL = null;
    /**
     * Connection Timeout(ms)
     */
    private static int TIMEOUT_CONN = 0;
    /**
     * Data Timeout(ms)
     */
    private static int TIMEOUT_READ = 0;
    /**
     * Request will be encoded with this unless the 'requestEncoding' parameter was specific.
     * It will be initialized as {@link StandardEncoding#UTF_8}
     */
    private static String DEFAULT_REQUEST_ENCODING = null;
    //endregion Properties----------------------------------------------------------------------------------------------
    //region Constructors & Initialization------------------------------------------------------------------------------

    /**
     * Prevent instantiation by other class
     */
    private HttpClient()
    {
    }

    /**
     * Get the ONLY instance, and init properties with default values.
     *
     * @return {@link #THE_CLIENT}
     */
    public static HttpClient getInstance()
    {
        DEFAULT_REQUEST_ENCODING = Charset.defaultCharset().name();
        TIMEOUT_CONN = DEFAULT_TIMEOUT_CONN;
        TIMEOUT_READ = DEFAULT_TIMEOUT_READ;
        THREAD_POOL = Executors.newFixedThreadPool(DEFAULT_POOL_SIZE);
        return THE_CLIENT;
    }

    /**
     * Get the ONLY instance, and init properties with specific values.
     *
     * @param connectionTimeout      {@link #TIMEOUT_CONN}
     * @param readTimeout            {@link #TIMEOUT_READ}
     * @param threadPoolSize         {@link #THREAD_POOL} size
     * @param defaultRequestEncoding {@link #DEFAULT_REQUEST_ENCODING}
     *
     * @return {@link #THE_CLIENT}
     */
    public static HttpClient getInstance(int connectionTimeout, int readTimeout, int threadPoolSize, String defaultRequestEncoding)
    {
        DEFAULT_REQUEST_ENCODING = defaultRequestEncoding != null && defaultRequestEncoding.length() > 0 ? defaultRequestEncoding : Charset.defaultCharset().name();
        TIMEOUT_CONN = connectionTimeout > 0 ? connectionTimeout : DEFAULT_TIMEOUT_CONN;
        TIMEOUT_READ = readTimeout > 0 ? readTimeout : DEFAULT_TIMEOUT_READ;
        THREAD_POOL = Executors.newFixedThreadPool(threadPoolSize > 0 ? threadPoolSize : DEFAULT_POOL_SIZE);
        return THE_CLIENT;
    }
    //endregion Constructors & Initialization---------------------------------------------------------------------------
    //region Methods----------------------------------------------------------------------------------------------------

    /**
     * Create a holder to cache custom headers and connection attributes.
     *
     * @return {@link RequestHolder}
     */
    public final RequestHolder prepare()
    {
        return new RequestHolder(this);
    }

    /**
     * @param requestId       Registered in {@link ResponseHandler}
     * @param url             String form of host url. A full expression is expected!
     * @param parameters      A {@link Map} that contains some parameters/Empty/Null.
     * @param responseHandler Implementation of {@link ResponseHandler}
     */
    public final void get(int requestId, @NotNull String url, @Nullable Map<String, Object> parameters, @NotNull ResponseHandler responseHandler)
    {
        sendHttpRequest(requestId, url, parameters, RequestMethod.GET, responseHandler, null, null, null);
    }

    /**
     * @param requestId       Registered in {@link ResponseHandler}
     * @param url             String form of host url. A full expression is expected!
     * @param parameters      A {@link Map} that contains some parameters/Empty/Null.
     * @param responseHandler Implementation of {@link ResponseHandler}
     */
    public final void post(int requestId, @NotNull String url, @Nullable Map<String, Object> parameters, @NotNull ResponseHandler responseHandler)
    {
        sendHttpRequest(requestId, url, parameters, RequestMethod.POST, responseHandler, null, null, null);
    }

    /**
     * @param requestId       Registered in {@link ResponseHandler}
     * @param url             String form of host url. A full expression is expected!
     * @param parameters      A {@link Map} that contains some parameters/Empty/Null.
     * @param responseHandler Implementation of {@link ResponseHandler}
     */
    public final void put(int requestId, @NotNull String url, @Nullable Map<String, Object> parameters, @NotNull ResponseHandler responseHandler)
    {
        sendHttpRequest(requestId, url, parameters, RequestMethod.PUT, responseHandler, null, null, null);
    }

    /**
     * @param requestId       Registered in {@link ResponseHandler}
     * @param url             String form of host url. A full expression is expected!
     * @param parameters      A {@link Map} that contains some parameters/Empty/Null.
     * @param responseHandler Implementation of {@link ResponseHandler}
     */
    public final void delete(int requestId, @NotNull String url, @Nullable Map<String, Object> parameters, @NotNull ResponseHandler responseHandler)
    {
        sendHttpRequest(requestId, url, parameters, RequestMethod.DELETE, responseHandler, null, null, null);
    }

    /**
     * Send the HTTP request
     *
     * @param requestId       Registered in {@link ResponseHandler}
     * @param urlString       String form of host urlString. A full expression is expected!
     * @param parameters      A {@link Map} that contains some parameters/Empty/Null.
     * @param requestMethod   {@link RequestMethod}/Null
     * @param responseHandler Implementation of {@link ResponseHandler}
     */
    @NotNull
    void sendHttpRequest(final int requestId, @NotNull final String urlString, @Nullable final Map<String, Object> parameters, @NotNull final RequestMethod requestMethod, @NotNull final ResponseHandler responseHandler, @Nullable final String requestEncoding, @Nullable final Map<String, String> setHeaders, @Nullable final Map<String, List<String>> addHeaders)
    {
        THREAD_POOL.submit(new Runnable()
        {
            @Override
            public void run()
            {
                responseHandler.registerRequest(requestId);
                Response response = new Response();
                try
                {
                    HttpURLConnection connection = initConnection(urlString, requestMethod, parameters, requestEncoding, setHeaders, addHeaders);
                    if (connection != null)
                    {
                        connection.connect();
                        response.URL = connection.getURL();
                        response.StatusCode = connection.getResponseCode();
                        response.ContentType = connection.getContentType();
                        response.ContentEncoding = connection.getContentEncoding();
                        response.Headers = connection.getHeaderFields();
                        InputStream inputStream;
                        inputStream = response.StatusCode == HttpURLConnection.HTTP_OK ? connection.getInputStream() : connection.getErrorStream();
                        if (inputStream != null)
                        {//When it actually received something, read them.
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            byte[] buffer = new byte[1024];
                            int len;
                            while ((len = inputStream.read(buffer)) != -1)
                            {
                                response.ContentLength += len;
                                outputStream.write(buffer, 0, len);
                            }
                            response.ContentBytes = outputStream.toByteArray();
                            inputStream.close();
                            outputStream.close();
                        }
                    } else
                    {
                        response.URL = new URL(urlString);
                        response.StatusCode = 0;
                        response.ErrorCode = RequestError.INTERNAL;
                    }
                } catch (NoSuchElementException e)
                {
                    response.ErrorCode = RequestError.INVALID_CONTENT_TYPE;
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e)
                {
                    response.ErrorCode = RequestError.INVALID_ENCODING;
                    e.printStackTrace();
                } catch (ProtocolException e)
                {
                    response.ErrorCode = RequestError.NETWORK;
                    e.printStackTrace();
                } catch (MalformedURLException e)
                {
                    response.ErrorCode = RequestError.INVALID_URL;
                    e.printStackTrace();
                } catch (IOException e)
                {
                    response.ErrorCode = RequestError.NETWORK;
                    e.printStackTrace();
                } catch (Exception e)
                {
                    response.ErrorCode = RequestError.UNKNOWN;
                    e.printStackTrace();
                } finally
                {
                    responseHandler.handleResponse(requestId, response);
                }
            }
        });
    }

    /**
     * Initialize the connection
     *
     * @param urlString     String form of host urlString. A full expression is expected!
     * @param requestMethod {@link RequestMethod}/Null
     * @param parameters    A {@link Map} that contains some parameters/Empty/Null.
     *
     * @return Prepared HttpURLConnection or Null.
     *
     * @throws NoSuchElementException
     * @throws ProtocolException
     */
    private HttpURLConnection initConnection(@NotNull String urlString, @NotNull RequestMethod requestMethod, @Nullable Map<String, Object> parameters, @Nullable String requestEncoding, @Nullable Map<String, String> setHeaders, @Nullable Map<String, List<String>> addHeaders)
            throws IOException, UnsupportedEncodingException, MalformedURLException, ProtocolException
    {
        Objects.requireNonNull(urlString);
        Objects.requireNonNull(requestMethod);
        String queryString = createQueryString(parameters, requestEncoding);
        URL url = null;
        switch (requestMethod)
        {
            case GET:
            case DELETE:
                url = new URL(queryString != null && queryString.length() > 0 ? urlString + '?' + queryString : urlString);
                break;
            case POST:
            case PUT:
                url = new URL(urlString);
                break;
        }
        HttpURLConnection connection = null;
        if (PROTOCOL_HTTP.equalsIgnoreCase(url.getProtocol()))
        {
            connection = (HttpURLConnection)url.openConnection();
        } else if (PROTOCOL_HTTPS.equalsIgnoreCase(url.getProtocol()))
        {
            connection = (HttpsURLConnection)url.openConnection();
        }
        if (connection != null)
        {
            if (setHeaders != null)
            {
                for (Map.Entry<String, String> entry : setHeaders.entrySet())
                {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            if (addHeaders != null)
            {
                for (Map.Entry<String, List<String>> entry : addHeaders.entrySet())
                {
                    String key = entry.getKey();
                    List<String> values = entry.getValue();
                    for (String value : values)
                    {
                        connection.addRequestProperty(key, value);
                    }
                }
            }
            connection.setRequestMethod(requestMethod.name());
            connection.setConnectTimeout(TIMEOUT_CONN);
            connection.setReadTimeout(TIMEOUT_READ);
            connection.setDoInput(true);
            if (requestMethod == RequestMethod.POST || requestMethod == RequestMethod.PUT)
            {
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                if (queryString != null && queryString.length() > 0)
                {//Fill request body
                    OutputStream out = connection.getOutputStream();
                    out.write(queryString.getBytes());
                    out.flush();
                    out.close();
                }
            }
        }
        return connection;
    }

    /**
     * Make a QueryString from the parameters, excluding '?'
     *
     * @param params          {@link Map}
     * @param requestEncoding Parameters encoding
     *
     * @return QueryString (A String like "name1=value1&name2=value2&").
     *
     * @throws UnsupportedEncodingException
     */
    @Nullable
    private String createQueryString(@Nullable Map<String, Object> params, @Nullable String requestEncoding)
            throws UnsupportedEncodingException
    {
        if (params != null)
        {
            int size = params.size();
            if (size > 0)
            {
                //Specific the capacity based on speculation for better performance since calculation for an exact value might cost more CPU time.
                StringBuilder stringBuilder = new StringBuilder(size * 10);
                if (requestEncoding == null || requestEncoding.length() == 0)
                {
                    requestEncoding = Charset.defaultCharset().name();
                }
                Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
                Map.Entry<String, Object> next = iterator.next();
                String key = next.getKey();
                Object value = next.getValue();
                if (key != null && key.length() > 0)
                {
                    stringBuilder.append(next.getKey()).append('=').append(URLEncoder.encode(paramFilter(value), requestEncoding));
                }
                while (iterator.hasNext())
                {
                    next = iterator.next();
                    key = next.getKey();
                    value = next.getValue();
                    if (key != null && key.length() > 0)
                    {
                        stringBuilder.append('&').append(key).append('=').append(URLEncoder.encode(paramFilter(value), requestEncoding));
                    }
                }
                return stringBuilder.toString();
            }
        }
        return null;
    }

    /**
     * Replace null with "".
     *
     * @param object Uncertain object.
     *
     * @return String
     */
    @NotNull
    private String paramFilter(Object object)
    {
        return object == null ? "" : object.toString();
    }
    //endregion Methods-------------------------------------------------------------------------------------------------
}