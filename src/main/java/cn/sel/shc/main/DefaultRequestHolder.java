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
import cn.sel.shc.object.RequestArgs;

public class DefaultRequestHolder extends RequestHolder implements DefaultRequestSender
{
    private RequestContentType CONTENT_TYPE;
    private boolean USE_CACHES;

    @Override
    public void get(int requestId, String urlString, RequestArgs requestArgs, ResponseHandler responseHandler)
    {
        HttpClient.getDefault().sendHttpRequest(requestId, urlString, requestArgs, responseHandler, RequestMethod.GET, CONTENT_TYPE, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ, USE_CACHES);
    }

    @Override
    public void get(int requestId, String urlString, ResponseHandler responseHandler)
    {
        HttpClient.getDefault().sendHttpRequest(requestId, urlString, null, responseHandler, RequestMethod.GET, CONTENT_TYPE, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ, USE_CACHES);
    }

    @Override
    public void get(int requestId, String urlString, RequestArgs requestArgs)
    {
        HttpClient.getDefault().sendHttpRequest(requestId, urlString, requestArgs, null, RequestMethod.GET, CONTENT_TYPE, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ, USE_CACHES);
    }

    @Override
    public void get(int requestId, String urlString)
    {
        HttpClient.getDefault().sendHttpRequest(requestId, urlString, null, null, RequestMethod.GET, CONTENT_TYPE, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ, USE_CACHES);
    }

    @Override
    public void post(int requestId, String urlString, RequestArgs requestArgs, ResponseHandler responseHandler)
    {
        HttpClient.getDefault().sendHttpRequest(requestId, urlString, requestArgs, responseHandler, RequestMethod.POST, CONTENT_TYPE, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ, USE_CACHES);
    }

    @Override
    public void post(int requestId, String urlString, ResponseHandler responseHandler)
    {
        HttpClient.getDefault().sendHttpRequest(requestId, urlString, null, responseHandler, RequestMethod.POST, CONTENT_TYPE, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ, USE_CACHES);
    }

    @Override
    public void post(int requestId, String urlString, RequestArgs requestArgs)
    {
        HttpClient.getDefault().sendHttpRequest(requestId, urlString, requestArgs, null, RequestMethod.POST, CONTENT_TYPE, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ, USE_CACHES);
    }

    @Override
    public void post(int requestId, String urlString)
    {
        HttpClient.getDefault().sendHttpRequest(requestId, urlString, null, null, RequestMethod.POST, CONTENT_TYPE, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ, USE_CACHES);
    }

    @Override
    public void put(int requestId, String urlString, RequestArgs requestArgs, ResponseHandler responseHandler)
    {
        HttpClient.getDefault().sendHttpRequest(requestId, urlString, requestArgs, responseHandler, RequestMethod.PUT, CONTENT_TYPE, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ, USE_CACHES);
    }

    @Override
    public void put(int requestId, String urlString, ResponseHandler responseHandler)
    {
        HttpClient.getDefault().sendHttpRequest(requestId, urlString, null, responseHandler, RequestMethod.PUT, CONTENT_TYPE, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ, USE_CACHES);
    }

    @Override
    public void put(int requestId, String urlString, RequestArgs requestArgs)
    {
        HttpClient.getDefault().sendHttpRequest(requestId, urlString, requestArgs, null, RequestMethod.PUT, CONTENT_TYPE, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ, USE_CACHES);
    }

    @Override
    public void put(int requestId, String urlString)
    {
        HttpClient.getDefault().sendHttpRequest(requestId, urlString, null, null, RequestMethod.PUT, CONTENT_TYPE, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ, USE_CACHES);
    }

    @Override
    public void delete(int requestId, String urlString, RequestArgs requestArgs, ResponseHandler responseHandler)
    {
        HttpClient.getDefault().sendHttpRequest(requestId, urlString, requestArgs, responseHandler, RequestMethod.DELETE, CONTENT_TYPE, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ, USE_CACHES);
    }

    @Override
    public void delete(int requestId, String urlString, ResponseHandler responseHandler)
    {
        HttpClient.getDefault().sendHttpRequest(requestId, urlString, null, responseHandler, RequestMethod.DELETE, CONTENT_TYPE, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ, USE_CACHES);
    }

    @Override
    public void delete(int requestId, String urlString, RequestArgs requestArgs)
    {
        HttpClient.getDefault().sendHttpRequest(requestId, urlString, requestArgs, null, RequestMethod.DELETE, CONTENT_TYPE, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ, USE_CACHES);
    }

    @Override
    public void delete(int requestId, String urlString)
    {
        HttpClient.getDefault().sendHttpRequest(requestId, urlString, null, null, RequestMethod.DELETE, CONTENT_TYPE, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ, USE_CACHES);
    }

    public DefaultRequestHolder setContentType(RequestContentType contentType)
    {
        CONTENT_TYPE = contentType;
        return this;
    }

    public DefaultRequestHolder setUseCaches(boolean ifUseCaches)
    {
        USE_CACHES = ifUseCaches;
        return this;
    }
}