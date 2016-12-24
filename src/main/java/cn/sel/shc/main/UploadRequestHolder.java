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

import cn.sel.shc.object.UploadData;
import cn.sel.shc.object.UploadFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * About the parameters:
 * <p>···><b>requestId</b></p>
 * The identification number of this request.
 * <p>···><b>url</b></p>
 * [NonNull] The url string of the server.
 * <p>···><b>file/fileList</b></p>
 * [Nullable] A {@link Map} contains some parameters.
 * <p>···><b>responseHandler</b></p>
 * [Nullable] Implementation of {@link ResponseHandler}. The response will be ignored if null was passed.
 */
public class UploadRequestHolder extends RequestHolder implements UploadRequestSender
{
    @Override
    public void uploadFileList(int requestId, String url, List<UploadFile> fileList, ResponseHandler responseHandler)
    {
        HttpClient.getUpload().sendUploadFileRequest(requestId, url, fileList, responseHandler, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ);
    }

    @Override
    public void uploadFileList(int requestId, String url, List<UploadFile> fileList)
    {
        HttpClient.getUpload().sendUploadFileRequest(requestId, url, fileList, null, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ);
    }

    @Override
    public void uploadDataList(int requestId, String url, List<UploadData> dataList, ResponseHandler responseHandler)
    {
        HttpClient.getUpload().sendUploadDataRequest(requestId, url, dataList, responseHandler, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ);
    }

    @Override
    public void uploadDataList(int requestId, String url, List<UploadData> dataList)
    {
        HttpClient.getUpload().sendUploadDataRequest(requestId, url, dataList, null, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ);
    }

    @Override
    public void uploadFile(int requestId, String url, UploadFile file, ResponseHandler responseHandler)
    {
        HttpClient.getUpload().sendUploadFileRequest(requestId, url, Collections.singletonList(file), responseHandler, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ);
    }

    @Override
    public void uploadFile(int requestId, String url, UploadFile file)
    {
        HttpClient.getUpload().sendUploadFileRequest(requestId, url, Collections.singletonList(file), null, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ);
    }

    @Override
    public void uploadData(int requestId, String url, UploadData data, ResponseHandler responseHandler)
    {
        HttpClient.getUpload().sendUploadDataRequest(requestId, url, Collections.singletonList(data), responseHandler, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ);
    }

    @Override
    public void uploadData(int requestId, String url, UploadData data)
    {
        HttpClient.getUpload().sendUploadDataRequest(requestId, url, Collections.singletonList(data), null, REQUEST_ENCODING, HEADERS_SET, HEADERS_ADD, TIMEOUT_CONN, TIMEOUT_READ);
    }
}