package com.rzdata.ps.controller.system;

import com.rzdata.annotation.Log;
import com.rzdata.annotation.RepeatSubmit;
import com.rzdata.common.constant.UserConstants;
import com.rzdata.core.controller.BaseController;
import com.rzdata.core.domain.AjaxResult;
import com.rzdata.core.page.TableDataInfo;
import com.rzdata.common.enums.BusinessType;
import com.rzdata.utils.poi.ExcelUtil;
import com.rzdata.system.model.SysConfig;
import com.rzdata.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 参数配置 信息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/config")
public class SysConfigController extends BaseController
{
    @Autowired
    private ISysConfigService configService;

    /**
     * 获取参数配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysConfig config)
    {
        return configService.selectPageConfigList(config);
    }

    @Log(title = "参数管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:config:export')")
    @GetMapping("/export")
    public void export(SysConfig config, HttpServletResponse response)
    {
        List<SysConfig> list = configService.selectConfigList(config);
		ExcelUtil.exportExcel(list, "参数数据", SysConfig.class, response);
    }

    /**
     * 根据参数编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:config:query')")
    @GetMapping(value = "/{configId}")
    public AjaxResult getInfo(@PathVariable Long configId)
    {
        return AjaxResult.success(configService.selectConfigById(configId));
    }

    /**
     * 根据参数键名查询参数值
     */
    @GetMapping(value = "/configKey/{configKey}")
    public AjaxResult getConfigKey(@PathVariable String configKey)
    {
        return AjaxResult.success(configService.selectConfigByKey(configKey));
    }

    /**
     * 新增参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:add')")
    @Log(title = "参数管理", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public AjaxResult add(@Validated @RequestBody SysConfig config)
    {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config)))
        {
            return AjaxResult.error("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setCreateBy(getUsername());
        return toAjax(configService.insertConfig(config));
    }

    /**
     * 修改参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysConfig config)
    {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config)))
        {
            return AjaxResult.error("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setUpdateBy(getUsername());
        return toAjax(configService.updateConfig(config));
    }

    /**
     * 删除参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "参数管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{configIds}")
    public AjaxResult remove(@PathVariable Long[] configIds)
    {
        configService.deleteConfigByIds(configIds);
        return success();
    }

    /**
     * 刷新参数缓存
     */
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "参数管理", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public AjaxResult refreshCache()
    {
        configService.resetConfigCache();
        return AjaxResult.success();
    }
}
