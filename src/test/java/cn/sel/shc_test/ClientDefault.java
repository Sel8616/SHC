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

import cn.sel.shc.main.*;
import cn.sel.shc.object.RequestArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientDefault
{
    private static final Logger LOGGER = LoggerFactory.getLogger("SHC_TEST_REQUEST_CLIENT");
    private static final DefaultHttpClient HTTP_CLIENT = HttpClient.getDefault();
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
        DefaultRequestHolder defaultRequestHolder = HTTP_CLIENT.prepare();
        defaultRequestHolder.get(1, "http://localhost:8008", requestArgs, RESPONSE_HANDLER);
        defaultRequestHolder.post(2, "http://localhost:8008", requestArgs, RESPONSE_HANDLER);
        defaultRequestHolder.put(3, "http://localhost:8008", requestArgs, RESPONSE_HANDLER);
        defaultRequestHolder.delete(4, "http://localhost:8008", requestArgs, RESPONSE_HANDLER);
        RequestArgs argsGet = new RequestArgs();
        argsGet.put("method", "GET");
        argsGet.put("num", 111);
        HTTP_CLIENT.get(11, "http://localhost:8008", argsGet);
        RequestArgs argsPost = new RequestArgs();
        argsPost.put("method", "POST");
        argsPost.put("num", 222);
        HTTP_CLIENT.post(12, "http://localhost:8008", argsPost);
        RequestArgs argsPut = new RequestArgs();
        argsPut.put("method", "PUT");
        argsPut.put("num", 333);
        HTTP_CLIENT.put(13, "http://localhost:8008", argsPut);
        RequestArgs argsDelete = new RequestArgs();
        argsDelete.put("method", "DELETE");
        argsDelete.put("num", 333);
        HTTP_CLIENT.delete(14, "http://localhost:8008", argsDelete);
    }
}