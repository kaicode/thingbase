package com.kaicube.thingbase.bs;

import com.kaicube.thingbase.bs.EntityBusinessService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

public class EntityBusinessServiceIntegrationTest {

    private EntityBusinessService entityBusinessService;

    @Before
    public void setup() throws SQLException, ClassNotFoundException {
        this.entityBusinessService = new EntityBusinessService();
    }

    @Test
    public void testStore() throws Exception {
        String stored = entityBusinessService.store("<customer persistent=\"true\"><name>Kai</name></customer>");
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><customer id=\"1\" persistent=\"true\"><name>Kai</name></customer>", stored);

        String customer = entityBusinessService.loadAsString("customer", 1);
        Assert.assertEquals(customer, stored);
    }
}
