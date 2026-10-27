package com.learn.web.x01_takeout.domain.bo;

import com.learn.web.common.AddGroup;
import com.learn.web.common.EditGroup;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜品业务对象（已写好）：新增 / 修改时接收 JSON 请求体，列表查询时接收 URL 参数（name、categoryId、status）。
 * <pre>
 * {"name": "小笼包", "categoryId": 3, "price": 18.00, "description": "皮薄馅大",
 *  "flavors": [{"name": "辣度", "value": "不辣,微辣"}]}
 * </pre>
 */
@Data
public class DishBo {

    @NotNull(message = "id不能为空", groups = EditGroup.class)
    private Long id;

    @NotBlank(message = "菜品名称不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(max = 32, message = "菜品名称不能超过32个字符", groups = {AddGroup.class, EditGroup.class})
    private String name;

    @NotNull(message = "分类不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long categoryId;

    @NotNull(message = "价格不能为空", groups = {AddGroup.class, EditGroup.class})
    @DecimalMin(value = "0.01", message = "价格必须大于0", groups = {AddGroup.class, EditGroup.class})
    private BigDecimal price;

    @Size(max = 255, message = "描述不能超过255个字符", groups = {AddGroup.class, EditGroup.class})
    private String description;

    /** 只在列表查询时用 */
    private Integer status;

    /** 口味列表。@Valid 表示列表里的每个对象也要校验 */
    @Valid
    private List<DishFlavorBo> flavors = new ArrayList<>();

    @Data
    public static class DishFlavorBo {

        @NotBlank(message = "口味名称不能为空", groups = {AddGroup.class, EditGroup.class})
        private String name;

        @NotBlank(message = "口味选项不能为空", groups = {AddGroup.class, EditGroup.class})
        private String value;
    }
}
