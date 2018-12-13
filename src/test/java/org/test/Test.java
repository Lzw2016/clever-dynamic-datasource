package org.test;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-12-13 11:23 <br/>
 */
@Slf4j
public class Test {

    @org.junit.Test
    public void test() {
        log.info("### {}", new HashSet<String>() {{
            add("111");
            add("222");
            add("333");
            add("444");
        }});

        AtomicInteger index = new AtomicInteger((Integer.MAX_VALUE - 5));
        for (int i = 0; i < 15; i++) {
            log.info("### {}", Math.abs(index.getAndAdd(1) % 3));
        }
//        log.info("### {}", (-2147483648 * -1));
    }
}
