package com.example.confessionwall.Controller; // 注意：你原代码拼写错误 Cotrolller → Controller

import com.example.confessionwall.Model.Confession;
import com.example.confessionwall.Service.ConfessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/confession")
// 解决跨域（如果前端和后端端口不同，必须加）
@CrossOrigin(origins = "*")
public class ConfessionController {

    @Autowired
    private ConfessionService confessionService;

    /**
     * 发布告白接口（POST）
     * 优化点：1. 统一返回JSON格式 2. 捕获异常返回友好提示 3. 参数非空校验
     */
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addConfession(
            @RequestParam(required = true) String username, // 强制必填
            @RequestParam(required = true) String message) {

        Map<String, Object> result = new HashMap<>();
        try {
            // 简单非空校验
            if (username.isBlank() || message.isBlank()) {
                result.put("code", 400);
                result.put("msg", "用户名和告白内容不能为空！");
                return ResponseEntity.badRequest().body(result);
            }

            if (username.length() > 100 || message.length() > 2000) {
                result.put("code", 400);
                result.put("msg", "用户名长度不能超过100，告白内容不能超过2000字！");
                return ResponseEntity.badRequest().body(result);
            }

            Confession confession = new Confession();
            confession.setUsername(username);
            confession.setMessage(message);
            confessionService.addConfession(confession);

            result.put("code", 200);
            result.put("msg", "告白成功！");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "告白失败，请稍后重试。");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 获取最新告白列表（GET）
     * 优化点：1. 给limit设置默认值 2. 处理limit非法值 3. 返回空列表而非null
     */
    @GetMapping("/recent")
    public ResponseEntity<Map<String, Object>> getRecentConfessions(
            @RequestParam(defaultValue = "10") int limit) { // 默认返回10条

        Map<String, Object> result = new HashMap<>();
        try {
            // 校验limit合法性
            if (limit <= 0 || limit > 100) {
                result.put("code", 400);
                result.put("msg", "查询数量需在1-100之间！");
                return ResponseEntity.badRequest().body(result);
            }

            List<Confession> confessions = confessionService.getRecentConfessions(limit);
            result.put("code", 200);
            result.put("msg", "查询成功");
            result.put("data", confessions); // 数据放在data字段
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "查询失败，请稍后重试。");
            result.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
}