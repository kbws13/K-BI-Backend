package xyz.kbws.model.dto.chart;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import xyz.kbws.common.ErrorCode;
import xyz.kbws.exception.ThrowUtils;

/**
 * @author kbws
 * @date 2024/3/25
 * @description:
 */
@Getter
@NoArgsConstructor
public class ChartGenResult {
    /**
     * AI 生成的图标数据
     */
    private String genChart;

    /**
     * AI 生成的分析结果
     */
    private String genResult;

    public ChartGenResult(String genChart, String genResult) {
        ThrowUtils.throwIf(StringUtils.isAnyBlank(genChart, genResult), ErrorCode.PARAMS_ERROR);
        this.genChart = genChart;
        this.genResult = genResult;
    }
}
