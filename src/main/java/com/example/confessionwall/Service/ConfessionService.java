package com.example.confessionwall.Service;

import com.example.confessionwall.Mapper.ConfessionMapper;
import com.example.confessionwall.Model.Confession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfessionService {

    @Autowired
    private ConfessionMapper confessionMapper;

    // 插入告白
    public void addConfession(Confession confession) {
        confessionMapper.insertConfession(confession);
    }

    // 获取最近的告白
    public List<Confession> getRecentConfessions(int limit) {
        return confessionMapper.getRecentConfessions(limit);
    }
}
