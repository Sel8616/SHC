package cn.sel.hsharp.core;

import cn.sel.hsharp.constant.RequestMethod;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hold attributes for a new request which is going to be send out but still has other attributes to add.
 */
public final class RequestHolder
{
    /**
     * For whom to hold the attributes.
     */
    private final HttpClient client;
    /**
     * To encode the request parameters.
     */
    private String REQUEST_ENCODING;
    /**
     * Custom headers to set
     */
    private final Map<String, String> SET_HEADERS = new HashMap<>();
    /**
     * Custom headers to add
     */
    private final Map<String, List<String>> ADD_HEADERS = new HashMap<>();

    RequestHolder(@NotNull HttpClient httpClient)
    {
        client = httpClient;
    }

    /**
     * @param requestEncoding Value of {@link #REQUEST_ENCODING}
     *
     * @return this
     */
    public final RequestHolder setRequestEncoding(String requestEncoding)
    {
        REQUEST_ENCODING = requestEncoding;
        return this;
    }

    /**
     * Reset the specific header with new value.
     *
     * @param name  Header name
     * @param value Header value
     *
     * @return this
     */
    public final RequestHolder setHeader(@NotNull String name, @NotNull String value)
    {
        if (name != null && name.length() > 0 && value != null)
        {
            SET_HEADERS.put(name, value);
        }
        return this;
    }

    /**
     * Add new value to the specific header
     *
     * @param name  Header name
     * @param value Header value
     *
     * @return this
     */
    public final RequestHolder addHeader(@NotNull String name, @NotNull String value)
    {
        if (name != null && name.length() > 0 && value != null)
        {
            List<String> values = ADD_HEADERS.get(name);
            if (values != null)
            {
                values.add(value);
            } else
            {
                values = new ArrayList<>(1);
                values.add(value);
                ADD_HEADERS.put(name, values);
            }
        }
        return this;
    }

    /**
     * @param requestId       Registered in {@link ResponseHandler}
     * @param url             String form of host url. A full expression is expected!
     * @param parameters      A {@link Map} that contains some parameters/Empty/Null.
     * @param responseHandler Implementation of {@link ResponseHandler}
     */
    public final void get(int requestId, @NotNull String url, @Nullable Map<String, Object> parameters, @NotNull ResponseHandler responseHandler)
    {
        client.sendHttpRequest(requestId, url, parameters, RequestMethod.GET, responseHandler, REQUEST_ENCODING, SET_HEADERS, ADD_HEADERS);
    }

    /**
     * @param requestId       Registered in {@link ResponseHandler}
     * @param url             String form of host url. A full expression is expected!
     * @param parameters      A {@link Map} that contains some parameters/Empty/Null.
     * @param responseHandler Implementation of {@link ResponseHandler}
     */
    public final void post(int requestId, @NotNull String url, @Nullable Map<String, Object> parameters, @NotNull ResponseHandler responseHandler)
    {
        client.sendHttpRequest(requestId, url, parameters, RequestMethod.POST, responseHandler, REQUEST_ENCODING, SET_HEADERS, ADD_HEADERS);
    }

    /**
     * @param requestId       Registered in {@link ResponseHandler}
     * @param url             String form of host url. A full expression is expected!
     * @param parameters      A {@link Map} that contains some parameters/Empty/Null.
     * @param responseHandler Implementation of {@link ResponseHandler}
     */
    public final void put(int requestId, @NotNull String url, @Nullable Map<String, Object> parameters, @NotNull ResponseHandler responseHandler)
    {
        client.sendHttpRequest(requestId, url, parameters, RequestMethod.PUT, responseHandler, REQUEST_ENCODING, SET_HEADERS, ADD_HEADERS);
    }

    /**
     * @param requestId       Registered in {@link ResponseHandler}
     * @param url             String form of host url. A full expression is expected!
     * @param parameters      A {@link Map} that contains some parameters/Empty/Null.
     * @param responseHandler Implementation of {@link ResponseHandler}
     */
    public final void delete(int requestId, @NotNull String url, @Nullable Map<String, Object> parameters, @NotNull ResponseHandler responseHandler)
    {
        client.sendHttpRequest(requestId, url, parameters, RequestMethod.DELETE, responseHandler, REQUEST_ENCODING, SET_HEADERS, ADD_HEADERS);
    }
}