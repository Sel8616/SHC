/*
 * Copyright 2015-2017 Erlu Shang (sel8616@gmail.com/philshang@163.com)
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
package cn.sel.shc_test;

import cn.sel.shc.main.HttpClient;
import cn.sel.shc.main.HttpResponse;
import cn.sel.shc.main.ResponseHandler;
import cn.sel.shc.main.UploadHttpClient;
import cn.sel.shc.object.UploadFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class ClientUploadFile
{
    private static final Logger LOGGER = LoggerFactory.getLogger("SHC_TEST_UPLOAD_CLIENT");
    private static final UploadHttpClient HTTP_CLIENT = HttpClient.getUpload();
    private static final ResponseHandler RESPONSE_HANDLER = new ResponseHandler()
    {
        @Override
        public boolean onFinished(int requestId, HttpResponse httpResponse)
        {
            LOGGER.info("Request({}) finished.\n{}\n{}", requestId, httpResponse, httpResponse.getContentString());
            return false;
        }

        @Override
        public void onSuccess(int requestId, HttpResponse httpResponse)
        {
            LOGGER.info("Request({})\tResult: Success", requestId);
        }

        @Override
        public void onFailure(int requestId, HttpResponse httpResponse)
        {
            LOGGER.info("Request({})\tResult: Failure", requestId);
        }

        @Override
        protected void onError(int requestId, String error)
        {
            LOGGER.info("Request({})\tResult: Error({})", requestId, error);
        }
    };

    public static void main(String... args)
            throws UnsupportedEncodingException
    {
        ClassLoader classLoader = ClientUploadFile.class.getClassLoader();
        URL resource1 = classLoader.getResource("uploading.txt");
        URL resource2 = classLoader.getResource("sharingan.png");
        String file1 = URLDecoder.decode(resource1.getPath(), "UTF-8");
        String file2 = URLDecoder.decode(resource2.getPath(), "UTF-8");
        List<UploadFile> fileList = new ArrayList<>();
        fileList.add(new UploadFile(file1));
        fileList.add(new UploadFile(file2));
        HTTP_CLIENT.uploadFileList(1, "http://localhost:8008/upload", fileList, RESPONSE_HANDLER);
    }
}