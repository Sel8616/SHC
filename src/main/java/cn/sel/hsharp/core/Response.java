package cn.sel.hsharp.core;

import cn.sel.hsharp.constant.RequestError;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class Response
{
    URL URL;
    int StatusCode;
    String ContentType;
    String ContentEncoding;
    int ContentLength;
    byte[] ContentBytes;
    RequestError ErrorCode;
    Map<String, List<String>> Headers;

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

    public String getContentString()
    {
        try
        {
            if (ContentEncoding != null && ContentEncoding.length() > 0)
            {
                return new String(ContentBytes, ContentEncoding);
            }
        } catch (UnsupportedEncodingException e)
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
        if (Headers != null)
        {
            String result = "";
            for (Map.Entry<String, List<String>> entry : Headers.entrySet())
            {
                String string = "[";
                List<String> values = entry.getValue();
                for (String v : values)
                {
                    string += v + ',';
                }
                if (values.size() > 0)
                {
                    string = string.substring(0, string.lastIndexOf(','));
                }
                string += "]";
                result += String.format("%s:%s,", entry.getKey(), string);
            }
            if (Headers.size() > 0)
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