package xyz.kbws.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import xyz.kbws.model.entity.Chart;

import java.util.List;
import java.util.Map;

/**
 * @author hsy
 * @description 针对表【chart(图表信息表)】的数据库操作Mapper
 * @createDate 2024-03-23 22:57:23
 * @Entity xyz.kbws.model.entity.Chart
 */
public interface ChartMapper extends BaseMapper<Chart> {
    List<Map<String, Object>> queryChartData(String querySql);
}




