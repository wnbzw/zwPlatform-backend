package com.zw.zwplatform.sentinel;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.stereotype.Component;

@Component
public class SentinelTest {
    public static void main(String[] args) {
        Entry entry = null;

        try {
            // 资源名可使用任意有业务语义的字符串
            entry = SphU.entry("hello world");
            // 被保护的业务逻辑
            // do something...
        } catch (BlockException e1) {
            // 资源访问阻止，被限流或被降级
            // 进行相应的处理操作
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }

    }
}
