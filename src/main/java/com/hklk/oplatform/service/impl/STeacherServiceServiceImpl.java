package com.hklk.oplatform.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hklk.oplatform.dao.inter.STeacherMapper;
import com.hklk.oplatform.entity.table.STeacher;
import com.hklk.oplatform.entity.vo.PageTableForm;
import com.hklk.oplatform.service.STeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class STeacherServiceServiceImpl implements STeacherService {

    @Autowired
    STeacherMapper sTeacherMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return sTeacherMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertOrUpdateByPrimaryKeySelective(STeacher sTeacher) {
        if (sTeacher.getId() != null) {
            return sTeacherMapper.updateByPrimaryKeySelective(sTeacher);
        } else {
            return sTeacherMapper.insertSelective(sTeacher);
        }
    }

    @Override
    public STeacher selectBySTeacher(STeacher sTeacher) {
        return sTeacherMapper.selectBySTeacher(sTeacher);
    }

    @Override
    public PageTableForm<STeacher> queryTeacherBySchoolId(Integer schoolId, int pageNum, int pageSize) {
        Page<STeacher> page = PageHelper.startPage(pageNum, pageSize, true);
        sTeacherMapper.queryTeacherBySchoolId(schoolId);
        PageTableForm<STeacher> pageTableForm = new PageTableForm<>(page);
        return pageTableForm;
    }

}
