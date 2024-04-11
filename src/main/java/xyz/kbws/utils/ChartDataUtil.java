package xyz.kbws.utils;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.kbws.common.ErrorCode;
import xyz.kbws.exception.ThrowUtils;
import xyz.kbws.manager.AIManager;
import xyz.kbws.model.dto.chart.ChartGenResult;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author kbws
 * @date 2024/3/25
 * @description: 把查询到的数据转换成字符串
 */
@Component
@Slf4j
public class ChartDataUtil {

    // 定义正则表达式
    private static final String regex = "【【【【【 (前端代码|分析结论) 【【【【【[\\s\\S]*?【【【【【 ";

    public static String changeDataToCSV(List<Map<String, Object>> chartOriginalData) {
        List<Set<String>> columnSets = chartOriginalData.stream()
                .map(Map::keySet)
                .collect(Collectors.toList());
        List<String> columnHeader = columnSets.stream()
                .map(column -> column.stream().filter(ObjectUtil::isNotNull).collect(Collectors.joining(",")))
                .collect(Collectors.toList());
        // 拿到对应的 value 拼接上
        List<String> columnDataList = chartOriginalData.stream().map(columnData -> {
            StringBuilder result = new StringBuilder();
            String[] headers = columnHeader.get(0).split(",");
            for (int i = 0; i < headers.length; i++) {
                String data = (String) columnData.get(headers[i]);
                result.append(data);
                if (i != headers.length - 1) {
                    result.append(",");
                }
            }
            result.append("\n");
            return result.toString();
        }).collect(Collectors.toList());
        // 将 columnDataList 中的数据添加到 stringJoiner
        StringJoiner stringJoiner = new StringJoiner("");
        stringJoiner.add(columnHeader.get(0)).add("\n");
        columnDataList.forEach(stringJoiner::add);
        return stringJoiner.toString();
    }

    /**
     * 获取 AI 生成结果
     * @param aiManager  AI 能力
     * @param goal
     * @param cvsData
     * @param chartType
     * @return
     */
    public static ChartGenResult getGenResult(final AIManager aiManager, final String goal, final String cvsData, final String chartType) {
        String promote = AIManager.PRECONDITION + "分析需求 " + goal + " \n原始数据如下: " + cvsData + "\n生成图标的类型是: " + chartType;
        String resultData = aiManager.sendMesToAIUseXingHuo(promote);
        resultData = resultData.replace(regex, "");
        log.info("AI 生成的信息: {}", resultData);
        ThrowUtils.throwIf(resultData.split("【【【【【").length < 3, ErrorCode.SYSTEM_ERROR);
        String genChart = resultData.split("【【【【【")[1].trim();
        String genResult = resultData.split("【【【【【")[2].trim();
        return new ChartGenResult(genChart, genResult);
    }
}
