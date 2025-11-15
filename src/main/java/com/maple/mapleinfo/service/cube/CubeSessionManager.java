package com.maple.mapleinfo.service.cube;

import com.maple.mapleinfo.domain.cube.Potential;
import com.maple.mapleinfo.dto.PotentialDto;
import com.maple.mapleinfo.utils.CubeType;
import com.maple.mapleinfo.utils.Grade;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.maple.mapleinfo.utils.SessionKeys.*;

@Component
public class CubeSessionManager {

    private static final int INITIAL_COUNT = 0;

    public PotentialDto getPotentialFromSession(HttpSession session, CubeType type) {
        String gradeKey = getGradeKey(type);
        String countKey = getCountKey(type);

        Grade currentGrade = (Grade) session.getAttribute(gradeKey);
        Integer count = (Integer) session.getAttribute(countKey);

        if (currentGrade == null) currentGrade = Grade.RARE;
        if (count == null) count = INITIAL_COUNT;

        return new PotentialDto(currentGrade, count, type);
    }

    private static String getGradeKey(CubeType type) {
        if (type == CubeType.DEFAULT) {
            return DEFAULT_GRADE;
        }

        return ADDITIONAL_GRADE;
    }

    private static String getCountKey(CubeType type) {
        if (type == CubeType.DEFAULT) {
            return DEFAULT_COUNT;
        }

        return ADDITIONAL_COUNT;
    }

    public void updateSession(HttpSession session, Potential potential) {
        CubeType type = potential.getType();
        Map<String, Object> sessionData = potential.toSessionData(type);

        for (Map.Entry<String, Object> entry : sessionData.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            session.setAttribute(key, value);
        }
    }

    public void initializeSession(HttpSession session) {
        session.setAttribute(DEFAULT_GRADE, Grade.RARE);
        session.setAttribute(ADDITIONAL_GRADE, Grade.RARE);
        session.setAttribute(DEFAULT_COUNT, INITIAL_COUNT);
        session.setAttribute(ADDITIONAL_COUNT, INITIAL_COUNT);
    }
}
