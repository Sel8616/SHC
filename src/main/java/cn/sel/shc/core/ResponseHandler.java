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
package cn.sel.shc.core;

import java.net.HttpURLConnection;

/**
 * Handle the responses received by {@link HttpClient}<br/>
 * <li>1.One ResponseHandler can recognize different {@link HttpResponse}s by their 'requestId's.</li>
 * <li>2.A request can be false canceled by calling 'cancelTask(int requestId)'. If the response hasn't arrived, it will be ignored.</li>
 *
 * @see HttpClient
 */
public abstract class ResponseHandler
{
    /**
     * The request was finished(success/fail/error), and one of the other 3 abstract methods will be invoked according to the status code if 'false' was returned.
     *
     * @param requestId    An integer value to identify the request.
     * @param httpResponse httpResponse
     *
     * @return <li>true: The httpResponse has been handled in this method.</li><li>false: Not handled yet.</li>
     */
    protected abstract boolean onFinished(int requestId, HttpResponse httpResponse);

    /**
     * The server returned 200 and given an expected result.
     *
     * @param requestId    An integer value to identify the request.
     * @param httpResponse The httpResponse.
     */
    protected abstract void onSuccess(int requestId, HttpResponse httpResponse);

    /**
     * The server had received the request,but refused to work and returned a non-200 status code.
     *
     * @param requestId    An integer value to identify the request.
     * @param httpResponse The httpResponse.
     */
    protected abstract void onFailure(int requestId, HttpResponse httpResponse);

    /**
     * The request hasn't been sent out.
     *
     * @param requestId An integer value to identify the request.
     * @param error     Error message.
     */
    protected abstract void onError(int requestId, String error);

    /**
     * @param requestId    See {@link HttpClient}
     * @param httpResponse {@link HttpResponse}
     */
    void handleResponse(int requestId, HttpResponse httpResponse)
    {
        if(httpResponse != null)
        {
            int statusCode = httpResponse.getStatusCode();
            if(!onFinished(requestId, httpResponse))
            {
                switch(statusCode)
                {
                    case 0:
                        onError(requestId, httpResponse.getContentString());
                        break;
                    case HttpURLConnection.HTTP_OK:
                        onSuccess(requestId, httpResponse);
                        break;
                    default:
                        onFailure(requestId, httpResponse);
                        break;
                }
            }
        }
    }
}