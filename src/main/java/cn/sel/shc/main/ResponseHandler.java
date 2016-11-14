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

import java.util.HashSet;
import java.util.Set;

/**
 * Handle the responses received by {@link HttpClient}.
 * <p>1.One instance could handle multi responses identified by different requestIds.</p>
 * <p>2.A request can be false canceled by calling 'cancelTask(int requestId)', then the response will be ignored.</p>
 *
 * @see HttpClient
 */
public abstract class ResponseHandler
{
    /**
     * RequestId container.
     */
    Set<Integer> ids = new HashSet<>();

    /**
     * The request was finished(success/fail/error), and one of the other 3 abstract methods will be invoked according to the status code if 'false' was returned.
     *
     * @param requestId    An integer value to identify the request.
     * @param httpResponse httpResponse
     *
     * @return <p>true: The httpResponse has been handled in this method.</p><p>false: Not handled yet.</p>
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

    public synchronized void cancel(int requestId)
    {
        ids.remove(requestId);
    }

    synchronized void register(int requestId)
    {
        ids.add(requestId);
    }

    synchronized boolean isRegistered(int requestId)
    {
        return ids.contains(requestId);
    }

    /**
     * @param requestId    See {@link HttpClient}
     * @param httpResponse {@link HttpResponse}
     */
    void handleResponse(int requestId, HttpResponse httpResponse)
    {
        if(isRegistered(requestId))
        {
            remove(requestId);
            if(httpResponse != null)
            {
                int statusCode = httpResponse.getStatusCode();
                if(!onFinished(requestId, httpResponse))
                {
                    if(statusCode / 200 == 1)
                    {
                        onSuccess(requestId, httpResponse);
                    } else if(statusCode < 1)
                    {
                        onError(requestId, httpResponse.getContentString());
                    } else
                    {
                        onFailure(requestId, httpResponse);
                    }
                }
            }
        } else
        {
            if(httpResponse != null)
            {
                httpResponse.dispose();
            }
        }
    }

    private synchronized void remove(int requestId)
    {
        ids.remove(requestId);
    }
}