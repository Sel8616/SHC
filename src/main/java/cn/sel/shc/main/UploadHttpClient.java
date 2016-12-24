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
import cn.sel.shc.object.UploadData;
import cn.sel.shc.object.UploadFile;
import cn.sel.shc.object.UploadObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Singleton/Less configuration->To be Simple!
 *
 * @see DefaultRequestSender , UploadRequestSender ,ResponseHandler
 */
public class UploadHttpClient extends HttpClient implements UploadRequestSender
{
    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    private final String multiPartBoundary = RequestContentType.BOUNDARY;

    UploadHttpClient()
    {
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

    @Override
    protected byte[] getRequestBody(HttpURLConnection connection, String requestEncoding)
    {
        return new byte[0];
    }

    /**
     * Create a holder to cache custom headers and connection attributes.
     *
     * @return {@link DefaultRequestHolder}
     */
    public UploadRequestHolder prepare()
    {
        return new UploadRequestHolder();
    }

    void sendUploadFileRequest(int requestId, String url, List<UploadFile> fileList, ResponseHandler responseHandler, String requestEncoding, Map<String, String> setHeaders, Map<String, List<String>> addHeaders, int timeoutConn, int timeoutRead)
    {
        sendUploadRequest(requestId, url, fileList, responseHandler, requestEncoding, setHeaders, addHeaders, timeoutConn, timeoutRead);
    }

    void sendUploadDataRequest(int requestId, String url, List<UploadData> dataList, ResponseHandler responseHandler, String requestEncoding, Map<String, String> setHeaders, Map<String, List<String>> addHeaders, int timeoutConn, int timeoutRead)
    {
        sendUploadRequest(requestId, url, dataList, responseHandler, requestEncoding, setHeaders, addHeaders, timeoutConn, timeoutRead);
    }

    private void sendUploadRequest(int requestId, String url, List<? extends UploadObject> objects, ResponseHandler responseHandler, String requestEncoding, Map<String, String> setHeaders, Map<String, List<String>> addHeaders, int timeoutConn, int timeoutRead)
    {
        checkUrl(url);
        RequestMethod requestMethod = RequestMethod.POST;
        boolean handleResponse = responseHandler != null;
        threadPool.submit(()->
        {
            HttpResponse httpResponse = null;
            if(handleResponse)
            {
                httpResponse = new HttpResponse();
                registerRequest(requestId, url, responseHandler, requestMethod, httpResponse);
            }
            if(objects == null || objects.isEmpty())
            {
                if(handleResponse)
                {
                    httpResponse.setContent("No object to upload!");
                }
            } else
            {
                HttpURLConnection connection = null;
                try
                {
                    connection = createConnection(url);
                    setConnAttributes(connection, requestMethod.name(), timeoutConn, timeoutRead, true, false);
                    fillRequestHeaders(connection, RequestContentType.FORM_MULTI_PART, setHeaders, addHeaders);
                    writeUploadFile(connection, objects, requestEncoding);
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
                        httpResponse.setContent(e.getMessage());
                    }
                    LOGGER.error(String.format("[SHC]Request(#%d#[%s]->%s) failed!", requestId, requestMethod, url), e);
                } finally
                {
                    if(connection != null)
                    {
                        connection.disconnect();
                    }
                }
            }
            if(handleResponse)
            {
                responseHandler.handleResponse(requestId, httpResponse);
            }
        });
    }

    /**
     * Write multi-part form data.
     */
    private void writeUploadFile(HttpURLConnection connection, List<? extends UploadObject> objects, String encoding)
            throws IOException
    {
        if(objects != null && !objects.isEmpty())
        {
            if(encoding == null || encoding.isEmpty())
            {
                encoding = DEFAULT_REQUEST_ENCODING;
            }
            OutputStream connOutputStream = null;
            try
            {
                connOutputStream = connection.getOutputStream();
                for(UploadObject uploadObject : objects)
                {
                    if(uploadObject != null)
                    {
                        String partHeader = createPartHeader(uploadObject);
                        InputStream uploadInputStream = null;
                        try
                        {
                            uploadInputStream = uploadObject.getInputStream();
                            connOutputStream.write(partHeader.getBytes(encoding));
                            byte[] buffer = new byte[8192];
                            int count;
                            while((count = uploadInputStream.read(buffer)) != -1)
                            {
                                connOutputStream.write(buffer, 0, count);
                            }
                            uploadInputStream.close();
                        } finally
                        {
                            if(uploadInputStream != null)
                            {
                                uploadInputStream.close();
                            }
                        }
                        connOutputStream.write("\r\n".getBytes());
                    }
                }
                String lastBoundary = createLastBoundary();
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

    private String createPartHeader(UploadObject uploadObject)
    {
        return String.format("--%s\r\nContent-Disposition: form-data; name=\"%s\"; filename=\"%s\"\r\nContent-Type: %s\r\n\r\n", multiPartBoundary, uploadObject.getName(), uploadObject.getFilename(), uploadObject.getContentType());
    }

    private String createLastBoundary()
    {
        return String.format("--%s--\r\n", multiPartBoundary);
    }
}