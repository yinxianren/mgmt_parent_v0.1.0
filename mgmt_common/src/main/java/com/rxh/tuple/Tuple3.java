package com.rxh.tuple;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 下午9:15
 * Description:
 */
public class Tuple3<A,B,C>  extends Tuple2<A,B> {
    public final C _3;
    public Tuple3(A _, B _2,C _3) {
        super(_, _2);
        this._3 = _3;
    }
}
