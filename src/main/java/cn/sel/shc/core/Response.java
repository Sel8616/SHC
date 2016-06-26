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

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class Response
{
    private URL url;
    private int statusCode;
    private String responseMessage;
    private String contentType;
    private String contentEncoding;
    private int contentLength;
    private long date;
    private long expires;
    private long ifModifiedSince;
    private long lastModified;
    private String ifNoneMatch;
    private String ETag;
    private RequestError requestError;
    private byte[] contentBytes;
    private Map<String, List<String>> headers;

    public URL getUrl()
    {
        return url;
    }

    void setUrl(URL url)
    {
        this.url = url;
    }

    public int getStatusCode()
    {
        return statusCode;
    }

    void setStatusCode(int statusCode)
    {
        this.statusCode = statusCode;
    }

    void setResponseMessage(String responseMessage)
    {
        this.responseMessage = responseMessage;
    }

    public String getResponseMessage()
    {
        return responseMessage;
    }

    public String getContentType()
    {
        return contentType;
    }

    void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    public String getContentEncoding()
    {
        return contentEncoding;
    }

    void setContentEncoding(String contentEncoding)
    {
        this.contentEncoding = contentEncoding;
    }

    public int getContentLength()
    {
        return contentLength;
    }

    void setContentLength(int contentLength)
    {
        this.contentLength = contentLength;
    }

    public byte[] getContentBytes()
    {
        return contentBytes;
    }

    void setContentBytes(byte[] contentBytes)
    {
        this.contentBytes = contentBytes;
    }

    public String getContentString()
    {
        if(contentBytes != null && contentBytes.length > 0)
        {
            if(contentEncoding != null && contentEncoding.length() > 0)
            {
                try
                {
                    return new String(contentBytes, contentEncoding);
                } catch(UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
            } else
            {
                return new String(contentBytes);
            }
        }
        return null;
    }

    public String getContentString(String encoding) throws UnsupportedEncodingException
    {
        return encoding != null && encoding.length() > 0 ? new String(contentBytes, encoding) : getContentString();
    }

    public RequestError getRequestError()
    {
        return requestError;
    }

    void setRequestError(RequestError requestError)
    {
        this.requestError = requestError;
    }

    public long getIfModifiedSince()
    {
        return ifModifiedSince;
    }

    void setIfModifiedSince(long ifModifiedSince)
    {
        this.ifModifiedSince = ifModifiedSince;
    }

    public long getLastModified()
    {
        return lastModified;
    }

    void setLastModified(long lastModified)
    {
        this.lastModified = lastModified;
    }

    public String getIfNoneMatch()
    {
        return ifNoneMatch;
    }

    void setIfNoneMatch(String ifNoneMatch)
    {
        this.ifNoneMatch = ifNoneMatch;
    }

    public String getETag()
    {
        return ETag;
    }

    void setETag(String ETag)
    {
        this.ETag = ETag;
    }

    public long getDate()
    {
        return date;
    }

    void setDate(long date)
    {
        this.date = date;
    }

    public long getExpires()
    {
        return expires;
    }

    void setExpires(long expires)
    {
        this.expires = expires;
    }

    public String getHeader(String name)
    {
        List<String> headerList = getHeaders(name);
        return headerList == null || headerList.size() == 0 ? null : headerList.get(0);
    }

    public List<String> getHeaders(String name)
    {
        return headers.get(name);
    }

    public Map<String, List<String>> getHeaders()
    {
        return headers;
    }

    void setHeaders(Map<String, List<String>> headerFields)
    {
        headers = headerFields;
    }

    public String getHeadersString()
    {
        return String.valueOf(headers);
    }

    @Override
    public String toString()
    {
        return "Response{" +
                "url=" + url +
                ", statusCode=" + statusCode +
                ", responseMessage='" + responseMessage + '\'' +
                ", contentType='" + contentType + '\'' +
                ", contentEncoding='" + contentEncoding + '\'' +
                ", contentLength=" + contentLength +
                ", date=" + date +
                ", expires=" + expires +
                ", ifModifiedSince=" + ifModifiedSince +
                ", lastModified=" + lastModified +
                ", ifNoneMatch='" + ifNoneMatch + '\'' +
                ", ETag='" + ETag + '\'' +
                ", requestError=" + requestError +
                ", headers=" + headers +
                '}';
    }
}