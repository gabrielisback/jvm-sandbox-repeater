package com.alibaba.jvm.sandbox.repeater.plugin.core.eventbus;

import com.alibaba.jvm.sandbox.repeater.plugin.domain.SubscribeEvent;

import java.util.Map;

/**
 * <p>
 *
 * @author zhaoyb1990
 */
public class RepeatEvent extends SubscribeEvent {

    private Map<String, String> requestParams;

    public Map<String, String> getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(Map<String, String> requestParams) {
        this.requestParams = requestParams;
    }
}
