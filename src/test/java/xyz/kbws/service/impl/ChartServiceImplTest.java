package xyz.kbws.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.kbws.manager.AIManager;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author kbws
 * @date 2024/3/25
 * @description:
 */
@SpringBootTest
@Slf4j
public class ChartServiceImplTest {
    @Resource
    private AIManager aiManager;

    @Test
    void asyncProcessChartData() {
        String goal = "分析一下用户趋势";
        String chartType = "折线图";
        String cvsData = "用户,数量\n12,10\n13,12\n14,12";
        String promote = AIManager.PRECONDITION + "分析需求 " + goal + " \n原始数据如下: " + cvsData + "\n生成图标的类型是: " + chartType;
        String resultData = null;
        try {
            resultData = aiManager.sendMesToAIUseXingHuo(promote);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.info("response: {}", resultData);
    }
}
