package com.alibaba.repeater.console.service.impl;

import com.alibaba.jvm.sandbox.repeater.plugin.core.wrapper.RecordWrapper;
import com.alibaba.jvm.sandbox.repeater.plugin.core.wrapper.SerializerWrapper;
import com.alibaba.jvm.sandbox.repeater.plugin.domain.RepeatModel;
import com.alibaba.jvm.sandbox.repeater.plugin.domain.RepeaterResult;
import com.alibaba.repeater.console.common.domain.PageResult;
import com.alibaba.repeater.console.common.domain.RecordBO;
import com.alibaba.repeater.console.common.domain.RecordDetailBO;
import com.alibaba.repeater.console.common.params.RecordParams;
import com.alibaba.repeater.console.dal.dao.RecordDao;
import com.alibaba.repeater.console.dal.model.Record;
import com.alibaba.repeater.console.service.RecordService;
import com.alibaba.repeater.console.service.convert.ModelConverter;
import com.alibaba.repeater.console.service.util.ConvertUtil;
import com.alibaba.repeater.console.service.util.ResultHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.stream.Collectors;

/**
 * {@link RecordServiceImpl} 使用mysql实现存储
 * <p>
 *
 * @author zhaoyb1990
 */
@Service("recordService")
@Slf4j
public class RecordServiceImpl implements RecordService {

    @Resource
    private RecordDao recordDao;
    @Resource
    private ModelConverter<Record, RecordBO> recordConverter;
    @Resource
    private ModelConverter<Record, RecordDetailBO> recordDetailConverter;

    @Override
    public RepeaterResult<String> saveRecord(String body) {
        try {
            RecordWrapper wrapper = SerializerWrapper.hessianDeserialize(body, RecordWrapper.class);
            if (wrapper == null || StringUtils.isEmpty(wrapper.getAppName())) {
                return RepeaterResult.builder().success(false).message("invalid request").build();
            }
            Record record = ConvertUtil.convertWrapper(wrapper, body);
            recordDao.insert(record);
            return RepeaterResult.builder().success(true).message("operate success").data("-/-").build();
        } catch (Throwable throwable) {
            return RepeaterResult.builder().success(false).message(throwable.getMessage()).build();
        }
    }

    @Override
    public RepeaterResult<String> get(String appName, String traceId) {
        Record record = recordDao.selectByAppNameAndTraceId(appName, traceId);
        if (record == null) {
            return RepeaterResult.builder().success(false).message("data not exits appName(" + appName + ") traceId(" + traceId + ")").build();
        }
        return RepeaterResult.builder().success(true).message("operate success").data(record.getWrapperRecord()).build();
    }

    @Override
    public PageResult<RecordBO> query(RecordParams params) {
        Page<Record> page = recordDao.selectByAppNameOrTraceId(params);
        PageResult<RecordBO> result = new PageResult<>();
        result.setSuccess(true);
        result.setCount(page.getTotalElements());
        result.setTotalPage(page.getTotalPages());
        result.setPageIndex(page.getNumber());
        result.setPageSize(page.getSize());
        result.setData(page.getContent().stream().map(recordConverter::convert).collect(Collectors.toList()));
        return result;
    }

    @Override
    public RepeaterResult<RecordDetailBO> getDetail(RecordParams params) {
        Record record = recordDao.selectByAppNameAndTraceId(params.getAppName(), params.getTraceId());
        if (record == null) {
            return RepeaterResult.builder().message("data not found").build();
        }
        return RepeaterResult.builder().success(true).data(recordDetailConverter.convert(record)).build();
    }

    @Override
    public RepeaterResult<RepeatModel> callback(String repeatId) {
        return null;
    }

    @Override
    public RepeaterResult<Void> delete(Long id) {
        try {
            recordDao.delete(id);
            return ResultHelper.success();
        } catch (Exception e) {
            return ResultHelper.fail("删除错误 - " + e.getMessage());
        }
    }
}
