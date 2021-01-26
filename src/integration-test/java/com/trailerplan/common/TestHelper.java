package com.trailerplan.common;


import com.trailerplan.model.dto.AbstractDTO;
import com.trailerplan.model.dto.AbstractProductionDTO;
import com.trailerplan.model.entity.AbstractEntity;
import com.trailerplan.model.entity.AbstractProductionEntity;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;

public class TestHelper {

    public static final void shouldTestStandardDtoFields(AbstractDTO testingValues,
                                                         AbstractEntity expectedValues) {
        assertEquals(expectedValues.getId().longValue(), testingValues.getId().longValue());
        assertEquals(expectedValues.getName(), testingValues.getName());
        assertEquals(expectedValues.getShortName(), testingValues.getShortName());
    }

    public static void shouldTestProductionFields(AbstractProductionDTO testingValue,
                                                  AbstractProductionEntity expectedValue) {
        assertEquals(expectedValue.getCreationDate().getTime(),
                testingValue.getCreationDate().getTime());
        assertEquals(expectedValue.getModificationDate().getTime(),
                testingValue.getModificationDate().getTime());
        assertEquals(expectedValue.getVersion(), testingValue.getVersion());
    }
}
