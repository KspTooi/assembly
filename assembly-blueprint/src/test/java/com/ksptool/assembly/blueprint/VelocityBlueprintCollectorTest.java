package com.ksptool.assembly.blueprint;

import com.ksptool.assembly.blueprint.collector.VelocityBlueprintCollector;
import com.ksptool.assembly.blueprint.utils.PathTool;
import org.junit.jupiter.api.Test;

import java.io.IOException;


public class VelocityBlueprintCollectorTest {


    @Test
    public void testCollect() throws IOException {
        VelocityBlueprintCollector collector = new VelocityBlueprintCollector();
        collector.setBlueprintExtension("vm");

        System.out.println(PathTool.getCurrentDirectory());


    }


}
