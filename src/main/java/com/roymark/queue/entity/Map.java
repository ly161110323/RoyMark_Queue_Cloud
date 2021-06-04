package com.roymark.queue.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@TableName(value = "br_map")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Map implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "map_hidden_id")
	private Long mapHiddenId;

    @TableField(value = "map_id")
    private String mapId;

    @TableField(value = "map_name")
    private String mapName;

    @TableField(value = "map_path")
	private String mapPath;

}
