package com.hklk.oplatform.dao.inter;

import com.hklk.oplatform.entity.table.Consumables;

import java.util.List;
public interface ConsumablesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Consumables record);

    int insertSelective(Consumables record);

    Consumables selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Consumables record);

    int updateByPrimaryKey(Consumables record);

    List<Consumables> queryConsumablesByCurId(Integer curriculumId);
}