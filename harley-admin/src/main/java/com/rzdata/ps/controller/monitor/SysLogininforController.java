package com.rzdata.ps.controller.monitor;

import com.rzdata.annotation.Log;
import com.rzdata.core.controller.BaseController;
import com.rzdata.core.domain.AjaxResult;
import com.rzdata.core.page.TableDataInfo;
import com.rzdata.common.enums.BusinessType;
import com.rzdata.utils.poi.ExcelUtil;
import com.rzdata.system.model.SysLogininfor;
import com.rzdata.system.service.ISysLogininforService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 系统访问记录
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/monitor/logininfor")
public class SysLogininforController extends BaseController
{
    @Autowired
    private ISysLogininforService logininforService;

    @PreAuthorize("@ss.hasPermi('monitor:logininfor:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysLogininfor logininfor)
    {
        return logininforService.selectPageLogininforList(logininfor);
    }

    @Log(title = "登录日志", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('monitor:logininfor:export')")
    @GetMapping("/export")
    public void export(SysLogininfor logininfor, HttpServletResponse response)
    {
        List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
		ExcelUtil.exportExcel(list, "登录日志", SysLogininfor.class, response);
    }

    @PreAuthorize("@ss.hasPermi('monitor:logininfor:remove')")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public AjaxResult remove(@PathVariable Long[] infoIds)
    {
        return toAjax(logininforService.deleteLogininforByIds(infoIds));
    }

    @PreAuthorize("@ss.hasPermi('monitor:logininfor:remove')")
    @Log(title = "登录日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public AjaxResult clean()
    {
        logininforService.cleanLogininfor();
        return AjaxResult.success();
    }
}
