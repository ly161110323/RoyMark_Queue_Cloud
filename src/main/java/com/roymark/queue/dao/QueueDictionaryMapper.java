package com.roymark.queue.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.roymark.queue.entity.QueueDictionary;
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
 * @since 2020-02-24
 */
@Repository
public interface QueueDictionaryMapper extends BaseMapper<QueueDictionary> {

      public List<QueueDictionary> searchDictionary(@Param("dictionary") QueueDictionary dictionary);
      public IPage<QueueDictionary> searchDictionary(Page page, @Param("dictionary") QueueDictionary dictionary);

    public List<QueueDictionary> getDictionaryByType(@Param("dictionary") QueueDictionary dictionary);

    //获取exeType对应的字典
    public List<QueueDictionary> getExeTypeDicTionaryInfo(@Param("dictionary") QueueDictionary dictionary);

    public List<QueueDictionary> getDictionaryByTypeIdList(@Param("dictionary") QueueDictionary dictionary);

        }