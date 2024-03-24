package xyz.kbws.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.kbws.model.entity.Chart;
import xyz.kbws.service.ChartService;
import xyz.kbws.mapper.ChartMapper;
import org.springframework.stereotype.Service;

/**
* @author hsy
* @description 针对表【chart(图表信息表)】的数据库操作Service实现
* @createDate 2024-03-23 22:57:23
*/
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService{

}




