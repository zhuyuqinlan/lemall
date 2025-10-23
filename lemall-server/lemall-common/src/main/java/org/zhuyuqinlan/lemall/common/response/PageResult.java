package org.zhuyuqinlan.lemall.common.response;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageResult<T> {
    private long total;      // 总数
    private long pageNum;    // 当前页
    private long pageSize;   // 每页数量
    private List<T> list; // 数据列表

    private PageResult(long total, long pageNum, long pageSize, List<T> records) {
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.list = records;
    }

    // 通用构造方法
    public static <T> PageResult<T> of(long total, long pageNum, long pageSize, List<T> records) {
        return new PageResult<>(total, pageNum, pageSize, records);
    }

    // MyBatis-Plus 转换
    public static <T> PageResult<T> fromMybatis(IPage<T> page) {
        return new PageResult<>(
                page.getTotal(),
                page.getCurrent(),
                page.getSize(),
                page.getRecords()
        );
    }

    // MongoDB 转换
    public static <T> PageResult<T> fromMongo(Page<T> page) {
        return new PageResult<>(
                page.getTotalElements(),
                page.getNumber() + 1, // MongoDB 页码从0开始
                page.getSize(),
                page.getContent()
        );
    }
}

