package org.sgmp.webapp.action.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sgmp.webapp.ActionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fep.bp.realinterface.RealTimeProxy376;
import fep.bp.realinterface.mto.CollectObject_TransMit;
import fep.bp.realinterface.mto.CommandItem;
import fep.bp.realinterface.mto.MTO_376;
import fep.bp.utils.BaudRate;
import fep.bp.utils.MeterType;
import fep.bp.utils.SerialPortPara;
import fep.common.exception.BPException;

/**
 * 保护器控制命令下发
 * 
 * @author Nick
 *
 */
@Component
public class ProtectorControlCommandSendingAction extends AbstractSimpleInteractionAction {

    /**
     * 
     */
    private static final long serialVersionUID = 288101067967162582L;

    private static final Logger logger = LoggerFactory.getLogger(ProtectorControlCommandSendingAction.class);

    @Autowired
    private RealTimeProxy376 realTimeProxy376;

    private String action;
    private String paramsAndValues;

    /**
     * 
     * @throws ActionException
     */
    public void send() throws ActionException {
        logger.info("soType             : " + getSoType());
        logger.info("soId               : " + getSoId());
        logger.info("soName             : " + getSoName());
        logger.info("soOrgId            : " + getSoOrgId());
        logger.info("soTgId             : " + getSoTgId());
        logger.info("soTgId             : " + getSoTgId());
        logger.info("soTermId           : " + getSoTermId());
        logger.info("soGpId             : " + getSoGpId());
        logger.info("action             : " + action);
        logger.info("paramsAndValues    : " + paramsAndValues);
        MTO_376 mto = new MTO_376();
        List<CollectObject_TransMit> cotList = new ArrayList<CollectObject_TransMit>();
        CollectObject_TransMit cot = new CollectObject_TransMit();
        cot.setTerminalAddr("96123458");
        cot.setEquipProtocol("100");
        cot.setMeterAddr("000000000001");
        cot.setMeterType(MeterType.Meter645);
        cot.setFuncode((byte) 27);
        cot.setPort((byte) 1);
        SerialPortPara spp = new SerialPortPara();
        spp.setBaudrate(BaudRate.bps_2400);
        try {
            spp.setStopbit(1);
            spp.setCheckbit(0);
            spp.setOdd_even_bit(1);
            spp.setDatabit(8);
        }
        catch(BPException _bpe) {
            logger.error("send error", _bpe);
            throw new ActionException("ActionException", _bpe.getCause());
        }
        cot.setSerialPortPara(spp);
        try {
            cot.setWaitforPacket((byte) 10);
        }
        catch(BPException _bpe) {
            logger.error("send error", _bpe);
            throw new ActionException("ActionException", _bpe.getCause());
        }
        cot.setWaitforByte((byte) 5);
        List<CommandItem> ciList = new ArrayList<CommandItem>();
        CommandItem ci = new CommandItem();
        ci.setIdentifier("80000710");
        Map<String, String> datacellParam = new HashMap<String, String>();
        datacellParam.put("0710", "0002");
        ci.setDatacellParam(datacellParam);
        ciList.add(ci);
        cot.setCommandItems(ciList);
        cotList.add(cot);
        mto.setCollectObjects_Transmit(cotList);
        long collectId = 0L;
        try {
            collectId = realTimeProxy376.transmitMsg(mto);
        }
        catch(Exception _e) {
            logger.error("send error", _e);
            throw new ActionException("ActionException", _e.getCause());
        }

        try {
            Thread.sleep(10000);
        }
        catch(InterruptedException _ie) {
            _ie.printStackTrace();
        }

        try {
            Map<String, String> resultMap = realTimeProxy376.getReturnByControl_TransMit(collectId);
            logger.info("getReturnByControl_TransMit : " + resultMap.toString());
        }
        catch(Exception _e) {
            logger.error("send error", _e);
            throw new ActionException("ActionException", _e.getCause());
        }

        responseText(String.valueOf(collectId));
    }

    /**
     * 
     * @throws ActionException
     */
    public void createTask() throws ActionException {
        
    }

    /**
     * 
     * @throws ActionException
     */
    public void getReceiveResult() throws ActionException {
        
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

}