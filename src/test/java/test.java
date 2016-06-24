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
import cn.sel.shc.core.Response;
import cn.sel.shc.core.ResponseHandler;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class test
{
    private static final ResponseHandler RESPONSE_HANDLER = new ResponseHandler()
    {
        @Override
        public boolean onFinished(int requestId, Response response)
        {
            System.out.println("Request(" + requestId + ") finished.");
            return false;
        }

        @Override
        public void onSuccess(int requestId, Response response)
        {
            System.out.println("Request(" + requestId + ") succeeded.");
            System.out.println("Result:Success\tResponse:" + response);
        }

        @Override
        public void onFailure(int requestId, int statusCode)
        {
            System.out.println("Request(" + requestId + ") failed.");
            System.out.println("Result:Failure" + "\tStatusCode:" + statusCode);
        }

        @Override
        protected void onError(int requestId, RequestError errorCode)
        {
            System.out.println("Request(" + requestId + ") error.");
            System.out.println("Result:Error" + "\tErrorCode:" + errorCode);
        }
    };

    public static void main(String[] args) throws UnsupportedEncodingException
    {
        Map<String, Object> params = new HashMap<>();
        params.put("param1", "value1");
        params.put("param2", "value2");
        params.put("param3", "呵呵");
        HttpClient.RequestHolder requestHolder = HttpClient.getInstance().prepare();
        requestHolder.setHeader("TOKEN", "TOKEN_STRING");
        requestHolder.setRequestEncoding(StandardEncoding.UTF_8);
        requestHolder.post(0, "http://localhost:8080", params, RESPONSE_HANDLER);
    }
}