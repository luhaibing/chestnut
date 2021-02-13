package com.mercer.mine.p5;

import com.mercer.mine.function.Test;

import org.jetbrains.annotations.NotNull;

/**
 * @author ：mercer
 * @date ：2021-02-02  17:21
 * @description ：
 */
public class J5 implements Test {

    @Override
    public void doAction() {

    }

    @NotNull
    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public int getAge() {
        return 0;
    }
}
