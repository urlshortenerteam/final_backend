package org.reevoo.url.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TimeDistr {
    public int time;
    public long value;

    public TimeDistr() {
        value = 0;
        time = 0;
    }
}
