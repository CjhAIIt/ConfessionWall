package com.example.confessionwall.Mapper;

import com.example.confessionwall.Model.Confession;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper; // 新增这个注解
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper // 标记为MyBatis Mapper，让Spring扫描并创建Bean
public interface ConfessionMapper {

    @Insert("INSERT INTO confession (username, message) VALUES (#{username}, #{message})")
    void insertConfession(Confession confession);

    @Select("SELECT * FROM confession ORDER BY create_time DESC LIMIT #{limit}")
    List<Confession> getRecentConfessions(int limit);
}