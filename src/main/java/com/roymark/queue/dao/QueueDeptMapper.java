package com.roymark.queue.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.roymark.queue.entity.QueueDept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper接口
 * </p>
 *
 * @author wangfz
 * @since 2020-02-06
 */
@Repository
public interface QueueDeptMapper extends BaseMapper<QueueDept> {


        public List<QueueDept> selectDeptList(@Param("dept") QueueDept dept);

        public IPage<QueueDept> selectDeptList(Page page, @Param("dept") QueueDept dept);

        public void  updateMatterStatusByDeptLs(@Param("list") List<String> list);
        public void  updateWindowMatterStatusByDeptLs(@Param("list") List<String> list);
        public void  updateTakeMatterCaseStatusByDeptLs(@Param("list") List<String> list);
        public void  updateTakeMatterCaseInfoStatusByDeptLs(@Param("list") List<String> list);
        public void  updateMatterTableStatusByDeptLs(@Param("list") List<String> list);
        public void  updateMatterTableEmptyStatusByDeptLs(@Param("list") List<String> list);
        public void  updateMatterGuideStatusByDeptLs(@Param("list") List<String> list);
        public void  updateMatterLabelStatusByDeptLs(@Param("list") List<String> list);

        public void  deleteDeptByPrimaryKeyDeptStatus(@Param("list") List<String> list);

        }