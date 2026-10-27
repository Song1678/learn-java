package com.learn.web.x01_takeout.domain.bo;

import com.learn.web.common.AddGroup;
import com.learn.web.common.EditGroup;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 分类业务对象（已写好）：新增 / 修改时接收 JSON 请求体，列表查询时接收 URL 参数（name、type）。
 */
@Data
public class CategoryBo {

    @NotNull(message = "id不能为空", groups = EditGroup.class)
    private Long id;

    @NotNull(message = "分类类型不能为空", groups = {AddGroup.class, EditGroup.class})
    @Min(value = 1, message = "分类类型只能是1或2", groups = {AddGroup.class, EditGroup.class})
    @Max(value = 2, message = "分类类型只能是1或2", groups = {AddGroup.class, EditGroup.class})
    private Integer type;

    @NotBlank(message = "分类名称不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(max = 20, message = "分类名称不能超过20个字符", groups = {AddGroup.class, EditGroup.class})
    private String name;

    @NotNull(message = "排序不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer sort;
}
