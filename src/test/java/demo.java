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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class demo
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

    public static void main(String[] args)
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try
        {
            int tag;
            String url, method, encoding;
            while(true)
            {
                try
                {
                    System.out.print("INPUT >>> Request ID:");
                    tag = Integer.parseInt(reader.readLine());
                    break;
                } catch(NumberFormatException | IOException e)
                {
                    e.printStackTrace();
                }
            }
            System.out.print("INPUT >>> URL:");
            url = reader.readLine();
            while(true)
            {
                System.out.println("CHOOSE >>> Method: 1-GET  2-POST  3-PUT  4-DELETE");
                method = reader.readLine();
                if(method.equals("1") || method.equals("2") || method.equals("3") || method.equals("4"))
                {
                    break;
                }
            }
            System.out.println("CHOOSE >>> RequestEncoding: 1-ISO8859-1  2-UTF8  3-GB2312  4-GBK  5-BIG5  OR Input Directly...");
            encoding = reader.readLine();
            switch(encoding)
            {
                case "1":
                    encoding = StandardEncoding.ISO;
                    break;
                case "2":
                    encoding = StandardEncoding.UTF_8;
                    break;
                case "3":
                    encoding = StandardEncoding.GB2312;
                    break;
                case "4":
                    encoding = StandardEncoding.GBK;
                    break;
                case "5":
                    encoding = StandardEncoding.BIG5;
                    break;
                default:
                    break;
            }
            HttpClient httpClient = HttpClient.getInstance();
            switch(method)
            {
                case "1":
                    httpClient.prepare().setRequestEncoding(encoding).get(tag, url, null, RESPONSE_HANDLER);
                    break;
                case "2":
                    httpClient.prepare().setRequestEncoding(encoding).post(tag, url, null, RESPONSE_HANDLER);
                    break;
                case "3":
                    httpClient.prepare().setRequestEncoding(encoding).put(tag, url, null, RESPONSE_HANDLER);
                    break;
                case "4":
                    httpClient.prepare().setRequestEncoding(encoding).delete(tag, url, null, RESPONSE_HANDLER);
                    break;
            }
        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}