package com.rzdata.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rzdata.system.model.SysDictData;
import com.rzdata.core.mybatisplus.core.BaseMapperPlus;

import java.util.List;

/**
 * 字典表 数据层
 *
 * @author ruoyi
 */
public interface SysDictDataMapper extends BaseMapperPlus<SysDictData> {

	default List<SysDictData> selectDictDataByType(String dictType) {
		return selectList(
			new LambdaQueryWrapper<SysDictData>()
				.eq(SysDictData::getStatus, "0")
				.eq(SysDictData::getDictType, dictType)
				.orderByAsc(SysDictData::getDictSort));
	}
}
