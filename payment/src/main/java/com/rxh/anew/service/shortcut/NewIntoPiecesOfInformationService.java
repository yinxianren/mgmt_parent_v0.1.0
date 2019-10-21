package com.rxh.anew.service.shortcut;


import com.rxh.anew.inner.ParamRule;
import com.rxh.anew.service.CommonSerivceInterface;

import java.util.Map;

public interface NewIntoPiecesOfInformationService  extends CommonSerivceInterface {

    Map<String, ParamRule>  getParamMapByIPOI();


}
