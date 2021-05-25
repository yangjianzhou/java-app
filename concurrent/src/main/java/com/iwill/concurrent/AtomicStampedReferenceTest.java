package com.iwill.concurrent;

import java.util.concurrent.atomic.AtomicStampedReference;

public class AtomicStampedReferenceTest {

    public static void main(String[] args) {
        AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<Integer>(1,1);
        boolean result = atomicStampedReference.compareAndSet(2,2,1,2);
    }
}
