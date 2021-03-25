package com.roymark.queue.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * <p>
 *
 * </p>
 *
 * @author wangfz
 * @since 2020-02-24
 */
public class QueueDictionary implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表流水号
     */
    @TableId(value = "Dictionary_Ls", type = IdType.AUTO)
    private Integer dictionaryLs;
    /**
     * 字典类型编号
     */
    @TableField(value = "Dictionary_Id")
    private String dictionaryId;
    /**
     * 字典类型名称
     */
    @TableField(value = "Dictionary_Name")
    private String dictionaryName;
    /**
     * 所属字典类型
     */
    @TableField(value = "Dictionarytype_Ls")
    private Integer dictionarytypeLs;
    /**
     * 所属区域
     */
    @TableField(value = "Area_Ls")
    private Integer areaLs;
    /**
     * 是否启用
     */
    @TableField(value = "Dictionary_IsShow")
    private String dictionaryIsshow;
    /**
     * 字典参数1
     */
    @TableField(value = "Dictionary_Parameter_1")
    private String dictionaryParameter1;
    /**
     * 字典参数2
     */
    @TableField(value = "Dictionary_Parameter_2")
    private String dictionaryParameter2;
    /**
     * 字典参数3
     */
    @TableField(value = "Dictionary_Parameter_3")
    private String dictionaryParameter3;
    /**
     * 字典参数4
     */
    @TableField(value = "Dictionary_Parameter_4")
    private String dictionaryParameter4;
    /**
     * 字典参数5
     */
    @TableField(value = "Dictionary_Parameter_5")
    private String dictionaryParameter5;
    /**
     * 字典参数6
     */
    @TableField(value = "Dictionary_Parameter_6")
    private String dictionaryParameter6;
    /**
     * 字典参数7
     */
    @TableField(value = "Dictionary_Parameter_7")
    private String dictionaryParameter7;
    /**
     * 创建用户
     */
    @TableField(value = "Create_User")
    private String createUser;
    /**
     * 创建时间
     */
    @TableField(value = "Create_Time")
    private LocalDateTime createTime;
    /**
     * 最后一次修改用户
     */
    @TableField(value = "Update_User")
    private String updateUser;
    /**
     * 最后一次修改时间
     */
    @TableField(value = "Update_Time")
    private LocalDateTime updateTime;
    /**
     * 数据状态
     */
    @TableField(value = "Status")
    private String Status;

    @TableField(exist = false)
    private String dictionarytypeId;

    @TableField(exist = false)
    private String dictionarytypeName;


    public String getDictionarytypeId() {
        return dictionarytypeId;
    }

    public void setDictionarytypeId(String dictionarytypeId) {
        this.dictionarytypeId = dictionarytypeId;
    }

    public String getDictionarytypeName() {
        return dictionarytypeName;
    }

    public void setDictionarytypeName(String dictionarytypeName) {
        this.dictionarytypeName = dictionarytypeName;
    }

    public Integer getDictionaryLs() {
        return dictionaryLs;
    }

    public void setDictionaryLs(Integer dictionaryLs) {
        this.dictionaryLs = dictionaryLs;
    }

    public String getDictionaryId() {
        return dictionaryId;
    }

    public void setDictionaryId(String dictionaryId) {
        this.dictionaryId = dictionaryId;
    }

    public String getDictionaryName() {
        return dictionaryName;
    }

    public void setDictionaryName(String dictionaryName) {
        this.dictionaryName = dictionaryName;
    }

    public Integer getDictionarytypeLs() {
        return dictionarytypeLs;
    }

    public void setDictionarytypeLs(Integer dictionarytypeLs) {
        this.dictionarytypeLs = dictionarytypeLs;
    }

    public Integer getAreaLs() {
        return areaLs;
    }

    public void setAreaLs(Integer areaLs) {
        this.areaLs = areaLs;
    }

    public String getDictionaryIsshow() {
        return dictionaryIsshow;
    }

    public void setDictionaryIsshow(String dictionaryIsshow) {
        this.dictionaryIsshow = dictionaryIsshow;
    }

    public String getDictionaryParameter1() {
        return dictionaryParameter1;
    }

    public void setDictionaryParameter1(String dictionaryParameter1) {
        this.dictionaryParameter1 = dictionaryParameter1;
    }

    public String getDictionaryParameter2() {
        return dictionaryParameter2;
    }

    public void setDictionaryParameter2(String dictionaryParameter2) {
        this.dictionaryParameter2 = dictionaryParameter2;
    }

    public String getDictionaryParameter3() {
        return dictionaryParameter3;
    }

    public void setDictionaryParameter3(String dictionaryParameter3) {
        this.dictionaryParameter3 = dictionaryParameter3;
    }

    public String getDictionaryParameter4() {
        return dictionaryParameter4;
    }

    public void setDictionaryParameter4(String dictionaryParameter4) {
        this.dictionaryParameter4 = dictionaryParameter4;
    }

    public String getDictionaryParameter5() {
        return dictionaryParameter5;
    }

    public void setDictionaryParameter5(String dictionaryParameter5) {
        this.dictionaryParameter5 = dictionaryParameter5;
    }

    public String getDictionaryParameter6() {
        return dictionaryParameter6;
    }

    public void setDictionaryParameter6(String dictionaryParameter6) {
        this.dictionaryParameter6 = dictionaryParameter6;
    }

    public String getDictionaryParameter7() {
        return dictionaryParameter7;
    }

    public void setDictionaryParameter7(String dictionaryParameter7) {
        this.dictionaryParameter7 = dictionaryParameter7;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

}