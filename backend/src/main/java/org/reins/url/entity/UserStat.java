package org.reins.url.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.lionsoul.ip2region.*;
import java.io.IOException;
import java.util.*;
@Data
@AllArgsConstructor

public class UserStat {
    public long id;
    public long count;
}
