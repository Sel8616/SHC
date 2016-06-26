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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hold attributes for a new request which is going to be send out but still has other attributes to add.
 */
public class RequestHolder
{
    private final Map<String, String> SET_HEADERS = new HashMap<>();
    private final Map<String, List<String>> ADD_HEADERS = new HashMap<>();
    private String REQUEST_ENCODING;
    private int TIMEOUT_CONN = 0;
    private int TIMEOUT_READ = 0;

    public RequestHolder setRequestEncoding(String encoding)
    {
        REQUEST_ENCODING = encoding;
        return this;
    }

    public RequestHolder setConnTimeout(int connTimeout)
    {
        TIMEOUT_CONN = connTimeout;
        return this;
    }

    public RequestHolder setReadTimeout(int readTimeout)
    {
        TIMEOUT_READ = readTimeout;
        return this;
    }

    public RequestHolder setHeader(String name, String value)
    {
        if(name != null && name.length() > 0 && value != null)
        {
            SET_HEADERS.put(name, value);
        }
        return this;
    }

    public RequestHolder addHeader(String name, String value)
    {
        if(name != null && name.length() > 0 && value != null)
        {
            List<String> values = ADD_HEADERS.get(name);
            if(values != null)
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
     * @see HttpClient#sendHttpRequest(int, String, RequestMethod, Map, ResponseHandler, String, Map, Map, int, int)
     */
    public void get(int requestId, String urlString, Map<String, Object> parameters, ResponseHandler responseHandler)
    {
        HttpClient.getInstance().sendHttpRequest(requestId, urlString, RequestMethod.GET, parameters, responseHandler, REQUEST_ENCODING, SET_HEADERS, ADD_HEADERS, TIMEOUT_CONN, TIMEOUT_READ);
    }

    /**
     * @see HttpClient#sendHttpRequest(int, String, RequestMethod, Map, ResponseHandler, String, Map, Map, int, int)
     */
    public void get(int requestId, String urlString, ResponseHandler responseHandler)
    {
        HttpClient.getInstance().sendHttpRequest(requestId, urlString, RequestMethod.GET, null, responseHandler, REQUEST_ENCODING, SET_HEADERS, ADD_HEADERS, TIMEOUT_CONN, TIMEOUT_READ);
    }

    /**
     * @see HttpClient#sendHttpRequest(int, String, RequestMethod, Map, ResponseHandler, String, Map, Map, int, int)
     */
    public void post(int requestId, String urlString, Map<String, Object> parameters, ResponseHandler responseHandler)
    {
        HttpClient.getInstance().sendHttpRequest(requestId, urlString, RequestMethod.POST, parameters, responseHandler, REQUEST_ENCODING, SET_HEADERS, ADD_HEADERS, TIMEOUT_CONN, TIMEOUT_READ);
    }

    /**
     * @see HttpClient#sendHttpRequest(int, String, RequestMethod, Map, ResponseHandler, String, Map, Map, int, int)
     */
    public void post(int requestId, String urlString, ResponseHandler responseHandler)
    {
        HttpClient.getInstance().sendHttpRequest(requestId, urlString, RequestMethod.POST, null, responseHandler, REQUEST_ENCODING, SET_HEADERS, ADD_HEADERS, TIMEOUT_CONN, TIMEOUT_READ);
    }

    /**
     * @see HttpClient#sendHttpRequest(int, String, RequestMethod, Map, ResponseHandler, String, Map, Map, int, int)
     */
    public void put(int requestId, String urlString, Map<String, Object> parameters, ResponseHandler responseHandler)
    {
        HttpClient.getInstance().sendHttpRequest(requestId, urlString, RequestMethod.PUT, parameters, responseHandler, REQUEST_ENCODING, SET_HEADERS, ADD_HEADERS, TIMEOUT_CONN, TIMEOUT_READ);
    }

    /**
     * @see HttpClient#sendHttpRequest(int, String, RequestMethod, Map, ResponseHandler, String, Map, Map, int, int)
     */
    public void put(int requestId, String urlString, ResponseHandler responseHandler)
    {
        HttpClient.getInstance().sendHttpRequest(requestId, urlString, RequestMethod.PUT, null, responseHandler, REQUEST_ENCODING, SET_HEADERS, ADD_HEADERS, TIMEOUT_CONN, TIMEOUT_READ);
    }

    /**
     * @see HttpClient#sendHttpRequest(int, String, RequestMethod, Map, ResponseHandler, String, Map, Map, int, int)
     */
    public void delete(int requestId, String urlString, Map<String, Object> parameters, ResponseHandler responseHandler)
    {
        HttpClient.getInstance().sendHttpRequest(requestId, urlString, RequestMethod.DELETE, parameters, responseHandler, REQUEST_ENCODING, SET_HEADERS, ADD_HEADERS, TIMEOUT_CONN, TIMEOUT_READ);
    }

    /**
     * @see HttpClient#sendHttpRequest(int, String, RequestMethod, Map, ResponseHandler, String, Map, Map, int, int)
     */
    public void delete(int requestId, String urlString, ResponseHandler responseHandler)
    {
        HttpClient.getInstance().sendHttpRequest(requestId, urlString, RequestMethod.DELETE, null, responseHandler, REQUEST_ENCODING, SET_HEADERS, ADD_HEADERS, TIMEOUT_CONN, TIMEOUT_READ);
    }
}