package com.mercury.platform.shared;

import com.mercury.platform.shared.pojo.CurrencyMessage;
import com.mercury.platform.shared.pojo.ItemMessage;
import com.mercury.platform.shared.pojo.Message;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class MessageParser {
    private final static String poeTradePattern = "^(.*\\s)?(.+):.+ to buy your (.+) listed for (\\d+(\\.\\d+)?)? (.+) in (Hardcore Legacy|Standard|Hardcore|Legacy)\\.?\\s*(\\(stash tab \"(.*)\"; position: left (\\d+), top (\\d+)\\))?(.*)$";
    private final static String poeCurrencyPattern = "^(.*\\s)?(.+):.+ to buy your (\\d+(\\.\\d+)?)? (.+) for my (\\d+(\\.\\d+)?)? (.+) in (Hardcore Legacy|Standard|Hardcore|Legacy)\\.?\\s*(.*)$";
    private final static String poeTradeNoBuyout = "^(.*\\s)?(.+):.+ to buy your (.+) in (Hardcore Legacy|Standard|Hardcore|Legacy)\\.?\\s*(\\(stash tab \"(.*)\"; position: left (\\d+), top (\\d+)\\))?\\s*(.*)$";

    private Pattern poeTradeItemPattern;
    private Pattern poeTradeCurrencyPattern;
    private Pattern poeTradeNoBuyoutPattern;

    public MessageParser() {
        poeTradeItemPattern = Pattern.compile(poeTradePattern);
        poeTradeCurrencyPattern = Pattern.compile(poeCurrencyPattern);
        poeTradeNoBuyoutPattern = Pattern.compile(poeTradeNoBuyout);
    }

    public Message parse(String fullMessage){
        String sourceMessage = StringUtils.substringAfter(fullMessage, "From ");
        Matcher poeTradeItemMatcher = poeTradeItemPattern.matcher(sourceMessage);
        if(poeTradeItemMatcher.find()){
            ItemMessage message = new ItemMessage();
            message.setSourceString(fullMessage);
            message.setWhisperNickname(poeTradeItemMatcher.group(2));
            message.setItemName(poeTradeItemMatcher.group(3));
            message.setCurCount(Double.parseDouble(poeTradeItemMatcher.group(4)));
            message.setCurrency(poeTradeItemMatcher.group(6));
            message.setLeague(poeTradeItemMatcher.group(7));
            if(poeTradeItemMatcher.group(9) != null) {
                message.setTabName(poeTradeItemMatcher.group(9));
                message.setLeft(Integer.parseInt(poeTradeItemMatcher.group(10)));
                message.setTop(Integer.parseInt(poeTradeItemMatcher.group(11)));
            }
            message.setOffer(poeTradeItemMatcher.group(12));
            return message;
        }
        Matcher poeTradeCurrencyMatcher = poeTradeCurrencyPattern.matcher(sourceMessage);
        if(poeTradeCurrencyMatcher.find()){
            CurrencyMessage message = new CurrencyMessage();
            message.setSourceString(fullMessage);
            message.setWhisperNickname(poeTradeCurrencyMatcher.group(2));
            message.setCurrForSaleCount(Double.parseDouble(poeTradeCurrencyMatcher.group(3)));
            message.setCurrForSaleTitle(poeTradeCurrencyMatcher.group(5));
            message.setCurCount(Double.parseDouble(poeTradeCurrencyMatcher.group(6)));
            message.setCurrency(poeTradeCurrencyMatcher.group(8));
            message.setLeague(poeTradeCurrencyMatcher.group(9));
            message.setOffer(poeTradeCurrencyMatcher.group(10));
            return message;
        }
        Matcher poeTradeNoBuyoutMatcher = poeTradeNoBuyoutPattern.matcher(sourceMessage);
        if(poeTradeNoBuyoutMatcher.find()){
            ItemMessage message = new ItemMessage();
            message.setSourceString(fullMessage);
            message.setWhisperNickname(poeTradeNoBuyoutMatcher.group(2));
            message.setItemName(poeTradeNoBuyoutMatcher.group(3));
            message.setCurCount(0d);
            message.setCurrency("???");
            message.setLeague(poeTradeNoBuyoutMatcher.group(4));
            if(poeTradeNoBuyoutMatcher.group(6) != null) {
                message.setTabName(poeTradeNoBuyoutMatcher.group(6));
                message.setLeft(Integer.parseInt(poeTradeNoBuyoutMatcher.group(7)));
                message.setTop(Integer.parseInt(poeTradeNoBuyoutMatcher.group(8)));
            }
            message.setOffer(poeTradeNoBuyoutMatcher.group(9));
            return message;
        }
        return null;
    }
}
