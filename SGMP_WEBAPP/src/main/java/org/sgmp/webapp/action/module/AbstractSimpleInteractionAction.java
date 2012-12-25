package org.sgmp.webapp.action.module;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.sgmp.webapp.ActionException;
import org.sgmp.webapp.action.AbstractSimpleAction;
import org.sgmp.webapp.constant.ConstantMTOType;
import org.sgmp.webapp.service.module.SimpleInteractionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import fep.bp.realinterface.RealTimeInterface;
import fep.bp.realinterface.mto.MTO_376;

/**
 * 
 * @author Nick
 *
 */
public abstract class AbstractSimpleInteractionAction extends AbstractSimpleAction {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 3446273927169317499L;

    private static final Logger logger = LoggerFactory.getLogger(AbstractSimpleInteractionAction.class);

    public static final String TYPE_TERMINAL_PARAMETER = "terminal-parameter";             // 集中器参数
    public static final String TYPE_PROTECTOR_PARAMETER = "protector-parameter";           // 保护器参数
    public static final String TYPE_PROTECTOR_CONTROL = "protector-control";               // 保护器控制

    @Autowired
    private SimpleInteractionService simpleInteractionService;

    @Autowired
    private RealTimeInterface realTimeProxy376;

    private MTO_376 mto376;

    private String mtoType;             // 终端（集中器）规约
    private String meterType;           // 测量点（电表/保护器）规约

    private String type;
    private String action;
    private String paramsAndValues;

    private long taskId;
    private Map<?, ?> resultMap;

    private String soType;              // selection object type
    private String soId;                // selection object id
    private String soName;              // selection object name
    private String soOrgId;             // selection object orgId
    private String soTgId;              // selection object tgId
    private String soTermId;            // selection object termId
    private String soGpId;              // selection object gpId

    /**
     * 组数据帧，发送至集中器
     * 
     * @throws ActionException
     */
    public void send() throws ActionException {
        beforeSend();

        if(StringUtils.equals(mtoType, ConstantMTOType.GW_376)) {               // 376规约
            if(StringUtils.equals(type, TYPE_TERMINAL_PARAMETER)) {                 // 集中器参数
                if(StringUtils.equals(action, "write")) {       // 写
                    try {
                        taskId = realTimeProxy376.writeParameters(mto376);
                    }
                    catch(Exception _e) {
                        logger.error("send error", _e);
                        throw new ActionException("ActionException", _e.getCause());
                    }
                }
                else if(StringUtils.equals(action, "read")) {   // 读
                    try {
                        taskId = realTimeProxy376.readParameters(mto376);
                    }
                    catch(Exception _e) {
                        logger.error("send error", _e);
                        throw new ActionException("ActionException", _e.getCause());
                    }
                }
            }
            else if(StringUtils.equals(type, TYPE_PROTECTOR_PARAMETER)) {           // 保护器参数
                if(StringUtils.equals(action, "write")) {       // 写
                    try {
                        taskId = realTimeProxy376.transmitMsg(mto376);
                    }
                    catch(Exception _e) {
                        logger.error("send error", _e);
                        throw new ActionException("ActionException", _e.getCause());
                    }
                }
                else if(StringUtils.equals(action, "read")) {   // 读
                    try {
                        taskId = realTimeProxy376.transmitMsg(mto376);
                    }
                    catch(Exception _e) {
                        logger.error("send error", _e);
                        throw new ActionException("ActionException", _e.getCause());
                    }
                }
            }
            else if(StringUtils.equals(type, TYPE_PROTECTOR_CONTROL)) {             // 保护器控制
                if(StringUtils.equals(action, "write")) {       // 写
                    try {
                        taskId = realTimeProxy376.transmitMsg(mto376);
                    }
                    catch(Exception _e) {
                        logger.error("send error", _e);
                        throw new ActionException("ActionException", _e.getCause());
                    }
                }
            }
        }

        afterSend();
        responseText(String.valueOf(taskId));
    }

    /**
     * 接收集中器返回数据帧，并解析数据帧
     * 
     * @throws ActionException
     */
    public void receive() throws ActionException {
        beforeReceive();

        if(StringUtils.equals(mtoType, ConstantMTOType.GW_376)) {               // 376规约
            if(StringUtils.equals(type, TYPE_TERMINAL_PARAMETER)) {                 // 集中器参数
                if(StringUtils.equals(action, "write")) {       // 写
                    try {
                        resultMap = realTimeProxy376.getReturnByWriteParameter(taskId);
                    }
                    catch(Exception _e) {
                        logger.error("send error", _e);
                        throw new ActionException("ActionException", _e.getCause());
                    }
                }
                else if(StringUtils.equals(action, "read")) {   // 读
                    try {
                        resultMap = realTimeProxy376.getReturnByReadParameter(taskId);
                    }
                    catch(Exception _e) {
                        logger.error("send error", _e);
                        throw new ActionException("ActionException", _e.getCause());
                    }
                }
            }
            else if(StringUtils.equals(type, TYPE_PROTECTOR_PARAMETER)) {           // 保护器参数
                
            }
            else if(StringUtils.equals(type, TYPE_PROTECTOR_CONTROL)) {             // 保护器控制
                if(StringUtils.equals(action, "write")) {       // 写
                    try {
                        resultMap = realTimeProxy376.getReturnByControl_TransMit(taskId);
                    }
                    catch(Exception _e) {
                        logger.error("send error", _e);
                        throw new ActionException("ActionException", _e.getCause());
                    }
                }
            }
        }
        logger.debug(resultMap.toString());

        afterReceive();
    }

    /**
     * 
     */
    public void beforeSend() {
        
    }

    /**
     * 
     */
    public void beforeReceive() {
        
    }

    /**
     * 
     */
    public void afterSend() {
        
    }

    /**
     * 
     */
    public void afterReceive() {
        
    }

    public MTO_376 getMto376() {
        return mto376;
    }

    public void setMto376(MTO_376 mto376) {
        this.mto376 = mto376;
    }

    public String getMtoType() {
        return mtoType;
    }

    public void setMtoType(String mtoType) {
        this.mtoType = mtoType;
    }

    public String getMeterType() {
        return meterType;
    }

    public void setMeterType(String meterType) {
        this.meterType = meterType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getParamsAndValues() {
        return paramsAndValues;
    }

    public void setParamsAndValues(String paramsAndValues) {
        this.paramsAndValues = paramsAndValues;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public Map<?, ?> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<?, ?> resultMap) {
        this.resultMap = resultMap;
    }

    public String getSoType() {
        return soType;
    }

    public void setSoType(String soType) {
        this.soType = soType;
    }

    public String getSoId() {
        return soId;
    }

    public void setSoId(String soId) {
        this.soId = soId;
    }

    public String getSoName() {
        return soName;
    }

    public void setSoName(String soName) {
        this.soName = soName;
    }

    public String getSoOrgId() {
        return soOrgId;
    }

    public void setSoOrgId(String soOrgId) {
        this.soOrgId = soOrgId;
    }

    public String getSoTgId() {
        return soTgId;
    }

    public void setSoTgId(String soTgId) {
        this.soTgId = soTgId;
    }

    public String getSoTermId() {
        return soTermId;
    }

    public void setSoTermId(String soTermId) {
        this.soTermId = soTermId;
    }

    public String getSoGpId() {
        return soGpId;
    }

    public void setSoGpId(String soGpId) {
        this.soGpId = soGpId;
    }

}
