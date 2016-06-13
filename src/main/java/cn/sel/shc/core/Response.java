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
    private URL URL;
    private int StatusCode;
    private String ContentType;
    private String ContentEncoding;
    private int ContentLength;
    private byte[] ContentBytes;
    private RequestError ErrorCode;
    private Map<String, List<String>> Headers;

    public URL getURL()
    {
        return URL;
    }

    void setURL(URL URL)
    {
        this.URL = URL;
    }

    public int getStatusCode()
    {
        return StatusCode;
    }

    void setStatusCode(int statusCode)
    {
        StatusCode = statusCode;
    }

    public String getContentType()
    {
        return ContentType;
    }

    void setContentType(String contentType)
    {
        ContentType = contentType;
    }

    public String getContentEncoding()
    {
        return ContentEncoding;
    }

    void setContentEncoding(String contentEncoding)
    {
        ContentEncoding = contentEncoding;
    }

    public int getContentLength()
    {
        return ContentLength;
    }

    void setContentLength(int contentLength)
    {
        ContentLength = contentLength;
    }

    public byte[] getContentBytes()
    {
        return ContentBytes;
    }

    public void setContentBytes(byte[] contentBytes)
    {
        ContentBytes = contentBytes;
    }

    public String getContentString()
    {
        try
        {
            if(ContentEncoding != null && ContentEncoding.length() > 0)
            {
                return new String(ContentBytes, ContentEncoding);
            }
        } catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return new String(ContentBytes);
    }

    public String getContentString(String encoding) throws UnsupportedEncodingException
    {
        return encoding != null && encoding.length() > 0 ? new String(ContentBytes, encoding) : getContentString();
    }

    public RequestError getErrorCode()
    {
        return ErrorCode;
    }

    void setErrorCode(RequestError errorCode)
    {
        ErrorCode = errorCode;
    }

    public String getHeader(String name)
    {
        List<String> headers = getHeaders(name);
        return headers == null || headers.size() == 0 ? null : headers.get(0);
    }

    public List<String> getHeaders(String name)
    {
        return Headers.get(name);
    }

    public Map<String, List<String>> getHeaders()
    {
        return Headers;
    }

    void setHeaders(Map<String, List<String>> headerFields)
    {
        Headers = headerFields;
    }

    private String getHeadersString()
    {
        if(Headers != null)
        {
            String result = "";
            for(Map.Entry<String, List<String>> entry : Headers.entrySet())
            {
                String string = "[";
                List<String> values = entry.getValue();
                for(String v : values)
                {
                    string += v + ',';
                }
                if(values.size() > 0)
                {
                    string = string.substring(0, string.lastIndexOf(','));
                }
                string += "]";
                result += String.format("%s:%s,", entry.getKey(), string);
            }
            if(Headers.size() > 0)
            {
                result = result.substring(0, result.lastIndexOf(','));
            }
            return '[' + result + ']';
        }
        return null;
    }

    @Override
    public String toString()
    {
        return "Response{" +
                "URL=" + URL +
                ", StatusCode=" + StatusCode +
                ", ContentType='" + ContentType + '\'' +
                ", ContentEncoding='" + ContentEncoding + '\'' +
                ", ContentLength=" + ContentLength +
                ", Content='" + getContentString() + '\'' +
                ", ErrorCode=" + ErrorCode +
                ", Headers=" + getHeadersString() +
                '}';
    }
}