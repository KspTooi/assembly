package com.ksptool.assembly.blueprint;

import com.ksptool.assembly.blueprint.collector.VelocityBlueprintCollector;
import com.ksptool.assembly.blueprint.entity.blueprint.RawBlueprint;
import com.ksptool.assembly.blueprint.utils.PathTool;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;


public class VelocityBlueprintCollectorTest {


    @Test
    public void testCollect() throws IOException {
        VelocityBlueprintCollector collector = new VelocityBlueprintCollector();

        List<RawBlueprint> blueprint = collector.collect(PathTool.getCurrentDirectory("blueprint"));

        System.out.println(blueprint);
    }


}
