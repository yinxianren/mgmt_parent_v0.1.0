package com.rxh.cache.redis;

import com.rxh.cache.AbstractPayCache;
import com.rxh.exception.PayException;
import com.rxh.service.square.MerchantCardService;
import com.rxh.square.pojo.MerchantCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  描述： 获取是商户绑卡缓存信息，锁级别为-商户锁
 * @author  panda
 * @date 20190802
 */
@Component
public class MerchantCardCache extends AbstractPayCache{

    @Autowired
    private MerchantCardService merchantCardService;
    private final String tableMapKeyName="merchant_card";

    /**
     *   获取成功的商户绑卡信息
     * @param merchantId
     * @param terminalMerId
     * @param backAcountNum
     * @return
     * @throws PayException
     */
    public MerchantCard getOne(String merchantId,String terminalMerId,String backAcountNum)throws PayException{
        List<MerchantCard> merchantCardList=selecByMerchantId(merchantId,terminalMerId);
        if(!isHasElement(merchantCardList)) return null;
        return merchantCardList.stream()
                .filter(t->t.getBackAcountNum().equals(backAcountNum))
                .findAny()
                .orElse(null)
                ;
    }

    /**
     *   获取终端号旗下指定卡所有成功绑卡信息
     * @param merchantId
     * @param terminalMerId
     * @param backAcountNum
     * @return
     * @throws PayException
     */
    public List<MerchantCard> getMore(String merchantId,String terminalMerId,String backAcountNum)throws PayException{
        return  selecByMerchantId(merchantId,terminalMerId)
                .stream()
                .filter(t->t.getCardNum().equals(backAcountNum))
                .distinct()
                .collect(Collectors.toList())
                ;
    }

    /**
     *  返回终端商户旗下所有成功绑卡信息
     * @param merchantId
     * @param terminalMerId
     * @return
     * @throws PayException
     */
    public List<MerchantCard> getMore(String merchantId,String terminalMerId)throws PayException{
        return  selecByMerchantId(merchantId,terminalMerId);
    }

    /**
     *  返回终端商户旗下所有成功绑卡信息
     * @param merchantId
     * @param cardNum
     * @return
     * @throws PayException
     */
    public List<MerchantCard> getMoreOne(String merchantId, String cardNum)throws PayException{
        return  selecByCardNum(merchantId,cardNum);
    }

    /**
     *
     * @param merchantCard
     * @return
     * @throws PayException
     */
    public boolean putOne(MerchantCard merchantCard) throws PayException{
        String merchantId = merchantCard.getMerId();
        String terminalMerId = merchantCard.getTerminalMerId();
        final String ObjectKey = merchantId + "-" + terminalMerId;
        try {
            List<MerchantCard> merchantCardList = (List<MerchantCard>) redisTemplate.opsForHash().get(tableMapKeyName, ObjectKey);
            if (!isHasElement(merchantCardList)) {
                merchantCardList = new ArrayList<>(1);
                merchantCardList.add(merchantCard);
            }else{
                merchantCardList.add(merchantCard);
            }

            final List<MerchantCard> list=merchantCardList;
            pool.execute(()-> put(tableMapKeyName,list, ObjectKey));
            return true;
        }catch (Exception e){
            throw  new PayException(e.getMessage());
        }
    }

    /**
     *   根据商户id 从redis 缓存获取商户绑卡信息,只保存成功绑卡信息！
     * @param merchantId
     * @return
     * @throws PayException
     */
    private  List<MerchantCard>  selecByMerchantId(String merchantId,String terminalMerId) throws PayException {
        try {
            final String ObjectKey=merchantId+"-"+terminalMerId;
            List<MerchantCard> merchantCardList= (List<MerchantCard>) redisTemplate.opsForHash().get(tableMapKeyName, ObjectKey);
            if(!isHasElement(merchantCardList)){
                merchantCardList=merchantCardService.select(new MerchantCard ()
                        .lsetMerId(merchantId)
                        .lsetTerminalMerId(terminalMerId)
                        .lsetStatus(0)
                );
                final List<MerchantCard> list=merchantCardList;
                if(isHasElement(list)){
                    pool.execute(()->put(tableMapKeyName,list,ObjectKey));
                }
            }
            return merchantCardList;
        }catch (Exception e){
            throw new PayException(e.getMessage());
        }
    }

    /**
     *   根据商户id 从redis 缓存获取商户绑卡信息,只保存成功绑卡信息！
     * @param merchantId
     * @return
     * @throws PayException
     */
    private List<MerchantCard> selecByCardNum(String merchantId,String cardNum) throws PayException {
        try {
            final String ObjectKey=merchantId+"-"+cardNum;
            List<MerchantCard> merchantCardList= (List<MerchantCard>) redisTemplate.opsForHash().get(tableMapKeyName, ObjectKey);
            if(!isHasElement(merchantCardList)){
                merchantCardList=merchantCardService.select(new MerchantCard ()
                        .lsetMerId(merchantId)
//                        .lsetCardNum(cardNum)
                        .lsetStatus(0)
                );
                final List<MerchantCard> list=merchantCardList;
                if(isHasElement(list)){
                    pool.execute(()->put(tableMapKeyName,list,ObjectKey));
                }
            }
            return merchantCardList;
        }catch (Exception e){
            throw new PayException(e.getMessage());
        }
    }




}
