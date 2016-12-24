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

import java.util.List;

/**
 * About the parameters:
 * <p>···><b>requestId</b></p>
 * The identification number of this request.
 * <p>···><b>url</b></p>
 * [NonNull] The url string of the server.
 * <p>···><b>fileList/dataList</b></p>
 * [Nullable] A {@link List} contains some files or byte arrays.
 * <p>···><b>responseHandler</b></p>
 * [Nullable] Implementation of {@link ResponseHandler}. The client will overlook the response if null was passed.
 */
interface UploadRequestSender
{
    /**
     * @see UploadRequestSender
     */
    void uploadFileList(int requestId, String url, List<UploadFile> fileList, ResponseHandler responseHandler);

    /**
     * @see UploadRequestSender
     */
    void uploadFileList(int requestId, String url, List<UploadFile> fileList);

    /**
     * @see UploadRequestSender
     */
    void uploadDataList(int requestId, String url, List<UploadData> dataList, ResponseHandler responseHandler);

    /**
     * @see UploadRequestSender
     */
    void uploadDataList(int requestId, String url, List<UploadData> dataList);

    /**
     * @see UploadRequestSender
     */
    void uploadFile(int requestId, String url, UploadFile fileList, ResponseHandler responseHandler);

    /**
     * @see UploadRequestSender
     */
    void uploadFile(int requestId, String url, UploadFile fileList);

    /**
     * @see UploadRequestSender
     */
    void uploadData(int requestId, String url, UploadData dataList, ResponseHandler responseHandler);

    /**
     * @see UploadRequestSender
     */
    void uploadData(int requestId, String url, UploadData dataList);
}
