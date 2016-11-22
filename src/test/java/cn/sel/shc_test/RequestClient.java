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
package cn.sel.shc_test;

import cn.sel.shc.main.HttpClient;
import cn.sel.shc.main.HttpResponse;
import cn.sel.shc.main.RequestHolder;
import cn.sel.shc.main.ResponseHandler;
import cn.sel.shc.object.RequestArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestClient
{
    private static final Logger LOGGER = LoggerFactory.getLogger("SHC_TEST_REQUEST_CLIENT");
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
    {
        RequestArgs requestArgs = new RequestArgs();
        requestArgs.put("param1", "abc");
        requestArgs.put("param2", 123);
        RequestHolder requestHolder = HttpClient.getInstance().prepare();
        requestHolder.get(1, "http://localhost:8008", requestArgs, RESPONSE_HANDLER);
        requestHolder.post(2, "http://localhost:8008", requestArgs, RESPONSE_HANDLER);
        requestHolder.put(3, "http://localhost:8008", requestArgs, RESPONSE_HANDLER);
        requestHolder.delete(4, "http://localhost:8008", requestArgs, RESPONSE_HANDLER);
        RequestArgs get = new RequestArgs();
        requestArgs.put("method", "GET");
        requestArgs.put("num", 111);
        HttpClient.getInstance().get(11, "http://localhost:8008", get);
        RequestArgs post = new RequestArgs();
        post.put("method", "POST");
        post.put("num", 222);
        HttpClient.getInstance().post(12, "http://localhost:8008", post);
        RequestArgs put = new RequestArgs();
        put.put("method", "PUT");
        put.put("num", 333);
        HttpClient.getInstance().put(13, "http://localhost:8008", put);
        RequestArgs delete = new RequestArgs();
        delete.put("method", "DELETE");
        delete.put("num", 333);
        HttpClient.getInstance().delete(14, "http://localhost:8008", delete);
    }
}