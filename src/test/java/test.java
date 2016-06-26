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

import cn.sel.shc.constant.RequestError;
import cn.sel.shc.constant.StandardEncoding;
import cn.sel.shc.core.HttpClient;
import cn.sel.shc.core.RequestHolder;
import cn.sel.shc.core.Response;
import cn.sel.shc.core.ResponseHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class test
{
    private static final Logger logger = Logger.getGlobal();
    private static final ResponseHandler RESPONSE_HANDLER = new ResponseHandler()
    {
        @Override
        public boolean onFinished(int requestId, Response response)
        {
            logger.info(String.format("Request(%d) finished.\n%s\n%s", requestId, response, response.getContentString()));
            return false;
        }

        @Override
        public void onSuccess(int requestId, Response response)
        {
            logger.info(String.format("Request(%d)\tResult:Success", requestId));
        }

        @Override
        public void onFailure(int requestId, Response response)
        {
            logger.info(String.format("Request(%d)\tResult:Failure", requestId));
        }

        @Override
        protected void onError(int requestId, RequestError requestError)
        {
            logger.info(String.format("Request(%d)\tResult:Error(%s)", requestId, requestError));
        }
    };

    public static void main(String[] args)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("param1", "value1");
        params.put("param2", "value2");
        params.put("param3", "呵呵");
        RequestHolder requestHolder = HttpClient.getInstance().prepare();
        requestHolder.setHeader("TOKEN", "TOKEN_STRING");
        requestHolder.setRequestEncoding(StandardEncoding.UTF_8);
        requestHolder.get(0, "http://127.0.0.1:8080", params, RESPONSE_HANDLER);
    }
}