package club.javafan.blog.web.controller.admin;

import club.javafan.blog.common.Result.ResponseResult;
import club.javafan.blog.common.util.PageQueryUtil;
import club.javafan.blog.common.util.PageResult;
import club.javafan.blog.domain.BlogLink;
import club.javafan.blog.service.LinkService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author 不会敲代码的小白(博客)
 * @date 2019/12/28 21:30
 * @desc 友情链接设置
 */
@Controller
@RequestMapping("/admin")
public class LinkController {

    @Resource
    private LinkService linkService;

    @GetMapping("/links")
    public String linkPage(HttpServletRequest request) {
        request.setAttribute("path", "links");
        return "admin/link";
    }

    @GetMapping("/links/list")
    @ResponseBody
    public ResponseResult list(@RequestParam Integer page,@RequestParam Integer limit) {
        if (Objects.isNull(page) || Objects.isNull(limit)){
            return ResponseResult.failResult("参数错误！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(page,limit);
        PageResult blogLinkPage = linkService.getBlogLinkPage(pageUtil);
        return ResponseResult.successResult().setData(blogLinkPage);
    }

    /**
     * 友链添加
     */
    @RequestMapping(value = "/links/save", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult save(@RequestParam("linkType") Integer linkType,
                       @RequestParam("linkName") String linkName,
                       @RequestParam("linkUrl") String linkUrl,
                       @RequestParam("linkRank") Integer linkRank,
                       @RequestParam("linkDescription") String linkDescription) {
        if (linkType == null || linkType < 0 || linkRank == null || linkRank < 0 || StringUtils.isEmpty(linkName) || StringUtils.isEmpty(linkName) || StringUtils.isEmpty(linkUrl) || StringUtils.isEmpty(linkDescription)) {
            return ResponseResult.failResult("参数异常！");
        }
        BlogLink link = new BlogLink();
        link.setLinkType(linkType.byteValue());
        link.setLinkRank(linkRank);
        link.setLinkName(linkName);
        link.setLinkUrl(linkUrl);
        link.setLinkDescription(linkDescription);
        Boolean aBoolean = linkService.saveLink(link);
        return ResponseResult.successResult().setData(aBoolean);
    }

    /**
     * 详情
     */
    @GetMapping("/links/info/{id}")
    @ResponseBody
    public ResponseResult info(@PathVariable("id") Integer id) {
        BlogLink link = linkService.selectById(id);
        return ResponseResult.successResult().setData(link);
    }

    /**
     * 友链修改
     */
    @RequestMapping(value = "/links/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult update(@RequestParam("linkId") Integer linkId,
                       @RequestParam("linkType") Integer linkType,
                       @RequestParam("linkName") String linkName,
                       @RequestParam("linkUrl") String linkUrl,
                       @RequestParam("linkRank") Integer linkRank,
                       @RequestParam("linkDescription") String linkDescription) {
        BlogLink tempLink = linkService.selectById(linkId);
        if (tempLink == null) {
            return ResponseResult.failResult("无数据！");
        }
        if (linkType == null || linkType < 0 || linkRank == null || linkRank < 0 || StringUtils.isEmpty(linkName) || StringUtils.isEmpty(linkName) || StringUtils.isEmpty(linkUrl) || StringUtils.isEmpty(linkDescription)) {
            return ResponseResult.failResult("参数异常！");
        }
        tempLink.setLinkType(linkType.byteValue());
        tempLink.setLinkRank(linkRank);
        tempLink.setLinkName(linkName);
        tempLink.setLinkUrl(linkUrl);
        tempLink.setLinkDescription(linkDescription);
        return ResponseResult.successResult().setData(tempLink);
    }

    /**
     * 友链删除
     */
    @RequestMapping(value = "/links/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult delete(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResponseResult.failResult("参数异常！");
        }
        if (linkService.deleteBatch(ids)) {
            return ResponseResult.successResult("成功！");
        } else {
            return ResponseResult.failResult("删除失败");
        }
    }

}