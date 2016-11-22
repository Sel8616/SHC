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

import cn.sel.shc.constant.RequestMethod;
import cn.sel.shc.object.RequestArgs;
import cn.sel.shc.object.UploadData;
import cn.sel.shc.object.UploadFile;

import java.util.*;

/**
 * Hold attributes for a new http request which is going to be send out but still has other attributes to add.
 */
public class RequestHolder implements Requester, Uploader
{
    private final Map<String, String> HEADERS_SET = new HashMap<>();
    private final Map<String, List<String>> HEADERS_ADD = new HashMap<>();
    private String REQUEST_ENCODING;
    private boolean DEFAULT_USE_CACHES;
    private int TIMEOUT_CONN;
    private int TIMEOUT_READ;

    @Override
    public void get(int requestId, String urlString, RequestArgs requestArgs, ResponseHandler responseHandler)
    {
        HttpClient.getInstance().sendHttpRequest(requestId, urlString, RequestMethod.GET, requestArgs, responseHandler, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void get(int requestId, String urlString, ResponseHandler responseHandler)
    {
        HttpClient.getInstance().sendHttpRequest(requestId, urlString, RequestMethod.GET, null, responseHandler, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void get(int requestId, String urlString, RequestArgs requestArgs)
    {
        HttpClient.getInstance().sendHttpRequest(requestId, urlString, RequestMethod.GET, requestArgs, null, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void get(int requestId, String urlString)
    {
        HttpClient.getInstance().sendHttpRequest(requestId, urlString, RequestMethod.GET, null, null, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void post(int requestId, String urlString, RequestArgs requestArgs, ResponseHandler responseHandler)
    {
        HttpClient.getInstance().sendHttpRequest(requestId, urlString, RequestMethod.POST, requestArgs, responseHandler, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void post(int requestId, String urlString, ResponseHandler responseHandler)
    {
        HttpClient.getInstance().sendHttpRequest(requestId, urlString, RequestMethod.POST, null, responseHandler, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void post(int requestId, String urlString, RequestArgs requestArgs)
    {
        HttpClient.getInstance().sendHttpRequest(requestId, urlString, RequestMethod.POST, requestArgs, null, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void post(int requestId, String urlString)
    {
        HttpClient.getInstance().sendHttpRequest(requestId, urlString, RequestMethod.POST, null, null, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void put(int requestId, String urlString, RequestArgs requestArgs, ResponseHandler responseHandler)
    {
        HttpClient.getInstance().sendHttpRequest(requestId, urlString, RequestMethod.PUT, requestArgs, responseHandler, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void put(int requestId, String urlString, ResponseHandler responseHandler)
    {
        HttpClient.getInstance().sendHttpRequest(requestId, urlString, RequestMethod.PUT, null, responseHandler, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void put(int requestId, String urlString, RequestArgs requestArgs)
    {
        HttpClient.getInstance().sendHttpRequest(requestId, urlString, RequestMethod.PUT, requestArgs, null, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void put(int requestId, String urlString)
    {
        HttpClient.getInstance().sendHttpRequest(requestId, urlString, RequestMethod.PUT, null, null, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void delete(int requestId, String urlString, RequestArgs requestArgs, ResponseHandler responseHandler)
    {
        HttpClient.getInstance().sendHttpRequest(requestId, urlString, RequestMethod.DELETE, requestArgs, responseHandler, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void delete(int requestId, String urlString, ResponseHandler responseHandler)
    {
        HttpClient.getInstance().sendHttpRequest(requestId, urlString, RequestMethod.DELETE, null, responseHandler, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void delete(int requestId, String urlString, RequestArgs requestArgs)
    {
        HttpClient.getInstance().sendHttpRequest(requestId, urlString, RequestMethod.DELETE, requestArgs, null, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void delete(int requestId, String urlString)
    {
        HttpClient.getInstance().sendHttpRequest(requestId, urlString, RequestMethod.DELETE, null, null, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ, DEFAULT_USE_CACHES);
    }

    @Override
    public void uploadFileList(int requestId, String url, List<UploadFile> fileList, ResponseHandler responseHandler)
    {
        HttpClient.getInstance().sendUploadFileRequest(requestId, url, fileList, responseHandler, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ);
    }

    @Override
    public void uploadFileList(int requestId, String url, List<UploadFile> fileList)
    {
        HttpClient.getInstance().sendUploadFileRequest(requestId, url, fileList, null, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ);
    }

    @Override
    public void uploadDataList(int requestId, String url, List<UploadData> dataList, ResponseHandler responseHandler)
    {
        HttpClient.getInstance().sendUploadDataRequest(requestId, url, dataList, responseHandler, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ);
    }

    @Override
    public void uploadDataList(int requestId, String url, List<UploadData> dataList)
    {
        HttpClient.getInstance().sendUploadDataRequest(requestId, url, dataList, null, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ);
    }

    @Override
    public void uploadFile(int requestId, String url, UploadFile file, ResponseHandler responseHandler)
    {
        HttpClient.getInstance().sendUploadFileRequest(requestId, url, Collections.singletonList(file), responseHandler, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ);
    }

    @Override
    public void uploadFile(int requestId, String url, UploadFile file)
    {
        HttpClient.getInstance().sendUploadFileRequest(requestId, url, Collections.singletonList(file), null, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ);
    }

    @Override
    public void uploadData(int requestId, String url, UploadData data, ResponseHandler responseHandler)
    {
        HttpClient.getInstance().sendUploadDataRequest(requestId, url, Collections.singletonList(data), responseHandler, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ);
    }

    @Override
    public void uploadData(int requestId, String url, UploadData data)
    {
        HttpClient.getInstance().sendUploadDataRequest(requestId, url, Collections.singletonList(data), null, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ);
    }

    public RequestHolder setRequestEncoding(String encoding)
    {
        REQUEST_ENCODING = encoding;
        return this;
    }

    public RequestHolder setUseCaches(boolean ifUseCaches)
    {
        DEFAULT_USE_CACHES = ifUseCaches;
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
        if(name != null && !name.isEmpty() && value != null)
        {
            HEADERS_SET.put(name, value);
        }
        return this;
    }

    public RequestHolder addHeader(String name, String value)
    {
        if(name != null && !name.isEmpty() && value != null)
        {
            List<String> values = HEADERS_ADD.get(name);
            if(values != null)
            {
                values.add(value);
            } else
            {
                values = new ArrayList<>(1);
                values.add(value);
                HEADERS_ADD.put(name, values);
            }
        }
        return this;
    }
}