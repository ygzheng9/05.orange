package com.metis.wx;

import com.jfinal.weixin.sdk.jfinal.MsgControllerAdapter;
import com.jfinal.weixin.sdk.msg.in.*;
import com.jfinal.weixin.sdk.msg.in.event.*;
import com.jfinal.weixin.sdk.msg.in.speech_recognition.InSpeechRecognitionResults;
import com.jfinal.weixin.sdk.msg.out.OutCustomMsg;
import com.jfinal.weixin.sdk.msg.out.OutTextMsg;

public class WeixinMsgController extends MsgControllerAdapter {
    @Override
    protected void processInTextMsg(InTextMsg inTextMsg) {
        OutTextMsg outMsg = new OutTextMsg(inTextMsg);
        outMsg.setContent("文本消息~");
        render(outMsg);
    }

    @Override
    protected void processInVoiceMsg(InVoiceMsg inVoiceMsg) {
        OutTextMsg outMsg = new OutTextMsg(inVoiceMsg);
        outMsg.setContent("语音消息~");
    }

    @Override
    protected void processInVideoMsg(InVideoMsg inVideoMsg) {
        OutTextMsg outMsg = new OutTextMsg(inVideoMsg);
        outMsg.setContent("接收视频消息~");
    }

    @Override
    protected void processInShortVideoMsg(InShortVideoMsg inShortVideoMsg) {
        OutTextMsg outMsg = new OutTextMsg(inShortVideoMsg);
        outMsg.setContent("小视频消息~");
    }

    @Override
    protected void processInLocationMsg(InLocationMsg inLocationMsg) {
        //转发给多客服PC客户端
        OutCustomMsg outCustomMsg = new OutCustomMsg(inLocationMsg);
        render(outCustomMsg);
    }

    @Override
    protected void processInLinkMsg(InLinkMsg inLinkMsg)
    {
        //转发给多客服PC客户端
        OutCustomMsg outCustomMsg = new OutCustomMsg(inLinkMsg);
        render(outCustomMsg);
    }

    @Override
    protected void processInCustomEvent(InCustomEvent inCustomEvent)
    {
        log.debug("测试方法：processInCustomEvent()");
        renderNull();
    }

    @Override
    protected void processInImageMsg(InImageMsg inImageMsg)
    {
        //转发给多客服PC客户端
        OutCustomMsg outCustomMsg = new OutCustomMsg(inImageMsg);
        render(outCustomMsg);
    }

    /**
     * 实现父类抽方法，处理关注/取消关注消息
     */
    @Override
    protected void processInFollowEvent(InFollowEvent inFollowEvent)
    {
        if (InFollowEvent.EVENT_INFOLLOW_SUBSCRIBE.equals(inFollowEvent.getEvent()))
        {
            log.debug("关注：" + inFollowEvent.getFromUserName());
            OutTextMsg outMsg = new OutTextMsg(inFollowEvent);
            outMsg.setContent("这是Jfinal-weixin测试服务</br>\r\n感谢您的关注");
            render(outMsg);
        }
        // 如果为取消关注事件，将无法接收到传回的信息
        if (InFollowEvent.EVENT_INFOLLOW_UNSUBSCRIBE.equals(inFollowEvent.getEvent()))
        {
            log.debug("取消关注：" + inFollowEvent.getFromUserName());
        }
    }

    @Override
    protected void processInQrCodeEvent(InQrCodeEvent inQrCodeEvent)
    {
        if (InQrCodeEvent.EVENT_INQRCODE_SUBSCRIBE.equals(inQrCodeEvent.getEvent()))
        {
            log.debug("扫码未关注：" + inQrCodeEvent.getFromUserName());
            OutTextMsg outMsg = new OutTextMsg(inQrCodeEvent);
            outMsg.setContent("感谢您的关注，二维码内容：" + inQrCodeEvent.getEventKey());
            render(outMsg);
        }
        if (InQrCodeEvent.EVENT_INQRCODE_SCAN.equals(inQrCodeEvent.getEvent()))
        {
            log.debug("扫码已关注：" + inQrCodeEvent.getFromUserName());
        }
    }

    @Override
    protected void processInLocationEvent(InLocationEvent inLocationEvent)
    {
        log.debug("发送地理位置事件：" + inLocationEvent.getFromUserName());
        OutTextMsg outMsg = new OutTextMsg(inLocationEvent);
        outMsg.setContent("地理位置是：" + inLocationEvent.getLatitude());
        render(outMsg);
    }

    @Override
    protected void processInMassEvent(InMassEvent inMassEvent)
    {
        log.debug("测试方法：processInMassEvent()");
        renderNull();
    }

    /**
     * 实现父类抽方法，处理自定义菜单事件
     */
    @Override
    protected void processInMenuEvent(InMenuEvent inMenuEvent)
    {
        log.debug("菜单事件：" + inMenuEvent.getFromUserName());
        OutTextMsg outMsg = new OutTextMsg(inMenuEvent);
        outMsg.setContent("菜单事件内容是：" + inMenuEvent.getEventKey());
        render(outMsg);
    }

    @Override
    protected void processInSpeechRecognitionResults(InSpeechRecognitionResults inSpeechRecognitionResults)
    {
        log.debug("语音识别事件：" + inSpeechRecognitionResults.getFromUserName());
        OutTextMsg outMsg = new OutTextMsg(inSpeechRecognitionResults);
        outMsg.setContent("语音识别内容是：" + inSpeechRecognitionResults.getRecognition());
        render(outMsg);
    }

    @Override
    protected void processInTemplateMsgEvent(InTemplateMsgEvent inTemplateMsgEvent)
    {
        log.debug("测试方法：processInTemplateMsgEvent()");
        renderNull();
    }
}
