package xyz.kbws.bizmq;

import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import xyz.kbws.common.ErrorCode;
import xyz.kbws.exception.BusinessException;
import xyz.kbws.manager.AIManager;
import xyz.kbws.model.dto.chart.ChartGenResult;
import xyz.kbws.model.entity.Chart;
import xyz.kbws.service.ChartService;
import xyz.kbws.utils.ChartDataUtil;

import javax.annotation.Resource;

/**
 * @author kbws
 * @date 2024/4/19
 * @description:
 */
@Component
@Slf4j
public class BiConsumer {

    @Resource
    private ChartService chartService;

    @Resource
    private AIManager aiManager;

    // 指定程序监听的消息队列和确认机制
    @SneakyThrows
    @RabbitListener(queues = {BiMqConstant.BI_QUEUE_NAME}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        log.info("receiveMessage message = {}", message);
        if (StringUtils.isBlank(message)) {
            //如果失败 消息拒绝
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "消息为空");
        }
        long chartId = Long.parseLong(message);
        Chart chart = chartService.getById(chartId);
        if (chart == null) {
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "图表为空");
        }
        // 先修改图表任务状态为 “执行中”。等执行成功后，修改为 “已完成”、保存执行结果；执行失败后，状态修改为 “失败”，记录任务失败信息。
        Chart updateChart = new Chart();
        updateChart.setId(chart.getId());
        updateChart.setStatus("running");
        boolean b = chartService.updateById(updateChart);
        if (!b) {
            channel.basicNack(deliveryTag, false, false);
            handleChartUpdateError(chart.getId(), "更新图表执行中状态失败");
            return;
        }
        // 发送给 AI 分析数据
        ChartGenResult chartGenResult = ChartDataUtil.getGenResult(aiManager, chart.getGoal(), chart.getChartData(), chart.getChartType());
        // 调用AI得到结果之后,再更新一次
        String genChart = chartGenResult.getGenChart();
        String genResult = chartGenResult.getGenResult();
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chart.getId());
        updateChartResult.setGenChart(genChart);
        updateChartResult.setGenResult(genResult);
        updateChartResult.setStatus("succeed");
        updateChartResult.setStatus("图表生成成功");
        // todo 定义状态为枚举值
        boolean updateResult = chartService.updateById(updateChartResult);
        if (!updateResult) {
            handleChartUpdateError(chart.getId(), "更新图表成功状态失败");
        }
        // 手动确认消息的接收，向RabbitMQ发送确认消息
        channel.basicAck(deliveryTag, false);
    }

    private void handleChartUpdateError(long chartId, String execMessage) {
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chartId);
        updateChartResult.setStatus("failed");
        updateChartResult.setExecMessage("execMessage");
        boolean updateResult = chartService.updateById(updateChartResult);
        if (!updateResult) {
            log.error("更新图表失败状态失败" + chartId + "," + execMessage);
        }
    }

}
