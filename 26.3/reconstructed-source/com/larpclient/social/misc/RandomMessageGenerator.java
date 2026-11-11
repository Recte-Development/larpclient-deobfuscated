package com.larpclient.social.misc;

import java.util.random.RandomGenerator;

@FunctionalInterface
public interface RandomMessageGenerator {
   String next(double var1, RandomGenerator var3);
}
