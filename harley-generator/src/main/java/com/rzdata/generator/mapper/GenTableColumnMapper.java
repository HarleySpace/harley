package com.rzdata.generator.mapper;

import com.rzdata.core.mybatisplus.core.BaseMapperPlus;
import com.rzdata.generator.model.GenTableColumn;

import java.util.List;

/**
 * 业务字段 数据层
 *
 * @author ruoyi
 */
public interface GenTableColumnMapper extends BaseMapperPlus<GenTableColumn> {
    /**
     * 根据表名称查询列信息
     *
     * @param tableName 表名称
     * @return 列信息
     */
    public List<GenTableColumn> selectDbTableColumnsByName(String tableName);

}
