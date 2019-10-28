package com.rxh.anew.wallet;

import org.apache.activemq.command.ActiveMQObjectMessage;

public interface PayWalletComponent {

    void payOrderWallet( ActiveMQObjectMessage objectMessage);

    void transOrderWallet( ActiveMQObjectMessage objectMessage);

}
