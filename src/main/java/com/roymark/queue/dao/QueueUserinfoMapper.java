package com.roymark.queue.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.roymark.queue.entity.QueueUserinfo;
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
 * @since 2020-01-13
 */
@Repository
public interface QueueUserinfoMapper extends BaseMapper<QueueUserinfo> {
        public IPage<QueueUserinfo> searchUserInfoList(Page page, @Param("userinfo") QueueUserinfo userinfo);
        public List<QueueUserinfo> searchUserInfoList(@Param("userinfo") QueueUserinfo userinfo);

        public QueueUserinfo searchUserInfoById(@Param("value") int value);
        }