package backend.controller;

import backend.common.CommonResult;
import backend.service.BackendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @author charles
 * @date 2023/3/3 22:45
 */

@RequestMapping("/charles")
@RestController
public class BackendController {
    @Autowired
    private BackendService backendService;

    @RequestMapping(value = "/mixture/create", method = RequestMethod.POST)
    public CommonResult create(@RequestParam("cName") String cName, @RequestParam("filePath") String filePath, @RequestParam("ccName") String ccName, @RequestParam("keyword") String keyword) {
        try {
            String cid = backendService.uploadFile(filePath);
            byte[] invokeResult = backendService.createTrx(cName, ccName, cid, keyword);
            if (invokeResult != null)
                return CommonResult.success(cid, "createTrx success");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return CommonResult.failed("createTrx fail");
    }

    @RequestMapping(value = "/mixture/queryByCid", method = RequestMethod.GET)
    public CommonResult queryByCid(@RequestParam("cid") String cid){
        String content = backendService.queryByCid(cid);
        if (Objects.equals(content, ""))
            return CommonResult.failed("queryByCid fail, maybe wrong cid");
        return CommonResult.success(content, "queryByCid success");
    }

    @RequestMapping(value = "/mixture/queryByTimestampAndKeyword", method = RequestMethod.GET)
    public CommonResult queryByTimestampAndKeyword(@RequestParam("timestamp") String timestamp, @RequestParam("keyword") String keyword){
        String result = backendService.queryMixtureByTimestampAndKeyword(timestamp, keyword);
        if (Objects.equals(result, ""))
            return CommonResult.failed("queryByTimestampAndKeyword fail, maybe wrong args");
        return CommonResult.success(result, "queryByTimestampAndKeyword success");
    }

    @RequestMapping(value = "/gearbox/create", method = RequestMethod.POST)
    public CommonResult createGearbox(@RequestParam("cName") String cName, @RequestParam("filePath") String filePath,
                                      @RequestParam("ccName") String ccName, @RequestParam("keyword") String keyword, @RequestParam("owner")String owner) {
        try {
            String cid = backendService.uploadFile(filePath);
            byte[] invokeResult = backendService.createGearbox(cName, ccName, cid, keyword, owner);
            if (invokeResult != null)
                return CommonResult.success(cid, "createGearbox success");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return CommonResult.failed("createGearbox fail");
    }

    @RequestMapping(value = "/gearbox/queryGearboxById", method = RequestMethod.GET)
    public CommonResult queryGearboxById(@RequestParam("gearboxNumber") String gearboxNumber){
        String result = backendService.QueryGearboxById(gearboxNumber);
        if (Objects.equals(result, ""))
            return CommonResult.failed("queryGearboxById fail, maybe wrong args");
        return CommonResult.success(result, "queryGearboxById success");
    }

    @RequestMapping(value = "/gearbox/queryAllGearboxes", method = RequestMethod.GET)
    public CommonResult queryAllGearboxes(){
        String result = backendService.queryAllGearboxes();
        if (Objects.equals(result, ""))
            return CommonResult.failed("queryAllGearboxes fail, maybe wrong args");
        return CommonResult.success(result, "queryAllGearboxes success");
    }
}
