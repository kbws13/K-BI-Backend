package xyz.kbws.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import xyz.kbws.common.ErrorCode;
import xyz.kbws.exception.ThrowUtils;

/**
 * @author kbws
 * @date 2024/3/25
 * @description: bi 返回的值
 */
@Data
@NoArgsConstructor
public class BiResponse {

    private Long chartId;

    private String genChart;

    private String genResult;

    /**
     * 这里可以校验 AI 生成的内容
     */
    public BiResponse(Long chartId, String genChart, String genResult) {
        ThrowUtils.throwIf(StringUtils.isAnyBlank(genChart, genResult) || (chartId != null && chartId <= 0), ErrorCode.PARAMS_ERROR);
        this.chartId = chartId;
        this.genChart = genChart;
        this.genResult = genResult;
    }

    public BiResponse(Long chartId) {
        this.chartId = chartId;
    }
}
