package com.hklk.oplatform.service;

import com.hklk.oplatform.entity.table.SStudent;

import java.util.List;

public interface SStudentService {

    int deleteByPrimaryKey(Integer id);

    int insertOrUpdateByPrimaryKeySelective(SStudent sStudent);

    List<SStudent> queryStudentByClassId(Integer classId);

    SStudent selectBySNumForValidate(Integer schoolId, String sNum);
}
