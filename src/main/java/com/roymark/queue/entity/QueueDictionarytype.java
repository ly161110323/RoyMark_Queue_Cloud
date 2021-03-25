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
    public class QueueDictionarytype implements Serializable {

private static final long serialVersionUID = 1L;

/**
 * 是否启用
 */
        @TableField(value="DictionaryType_IsShow")
private String dictionarytypeIsshow;
/**
 * 表流水号
 */
        @TableId(value="DictionaryType_Ls",type = IdType.AUTO)
    private Integer dictionarytypeLs;
/**
 * 字典类型编号
 */
        @TableField(value="DictionaryType_Id")
private String dictionarytypeId;
/**
 * 字典类型名称
 */
        @TableField(value="DictionaryType_Name")
private String dictionarytypeName;
/**
 * 字段名称
 */
        @TableField(value="Field_Name")
private String fieldName;
/**
 * 字段类型
 */
        @TableField(value="Field_Type")
private String fieldType;
/**
 * 字段说明
 */
        @TableField(value="Field_state")
private String fieldState;
/**
 * 创建用户
 */
        @TableField(value="Create_User")
private String createUser;
/**
 * 创建时间
 */
        @TableField(value="Create_Time")
private LocalDateTime createTime;
/**
 * 最后一次修改用户
 */
        @TableField(value="Update_User")
private String updateUser;
/**
 * 最后一次修改时间
 */
        @TableField(value="Update_Time")
private LocalDateTime updateTime;
/**
 * 数据状态
 */
        @TableField(value="Status")
private String Status;


public String getDictionarytypeIsshow() {
        return dictionarytypeIsshow;
        }

    public void setDictionarytypeIsshow(String dictionarytypeIsshow) {
        this.dictionarytypeIsshow = dictionarytypeIsshow;
        }

public Integer getDictionarytypeLs() {
        return dictionarytypeLs;
        }

    public void setDictionarytypeLs(Integer dictionarytypeLs) {
        this.dictionarytypeLs = dictionarytypeLs;
        }

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

public String getFieldName() {
        return fieldName;
        }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
        }

public String getFieldType() {
        return fieldType;
        }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
        }

public String getFieldState() {
        return fieldState;
        }

    public void setFieldState(String fieldState) {
        this.fieldState = fieldState;
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