package com.maple.mapleinfo.domain.cube;

import com.maple.mapleinfo.utils.CubeLimitConstants;
import com.maple.mapleinfo.utils.CubeType;
import com.maple.mapleinfo.utils.Grade;
import com.maple.mapleinfo.utils.SessionKeys;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class Potential {

    private Grade grade;
    private List<Option> options;
    private Integer count;
    private CubeType type;

    private static final int RESET_COUNT = 0;

    public Map<String, Object> toSessionData(CubeType type) {
        Map<String, Object> data = new HashMap<>();

        if (type.equals(CubeType.DEFAULT)) {
            data.put(SessionKeys.DEFAULT_GRADE, this.grade);
            data.put(SessionKeys.DEFAULT_COUNT, this.count);
        } else {
            data.put(SessionKeys.ADDITIONAL_GRADE, this.grade);
            data.put(SessionKeys.ADDITIONAL_COUNT, this.count);
        }
        return data;
    }

    public Potential upgradePotential() {
        if (grade == Grade.LEGENDARY) {
            return this;
        }

        int limit = CubeLimitConstants.findLimit(grade, type);

        if (count >= limit && limit != CubeLimitConstants.LEGENDARY_LIMIT) {
            Grade newGrade = grade.next();
            return new Potential(newGrade, options, RESET_COUNT, type);
        }

        return this;
    }
}
