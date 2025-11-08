package com.ksptool.assembly.blueprint;

import com.ksptool.assembly.blueprint.collector.MysqlCollector;
import com.ksptool.assembly.blueprint.converter.MysqlToJavaPolyConverter;
import com.ksptool.assembly.blueprint.core.AssemblyFactory;
import com.ksptool.assembly.blueprint.entity.field.PolyTable;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AssemblyFactoryTest {


    @Test
    public void testFullProcess() {

        //创建Mysql搜集器
        MysqlCollector coll = new MysqlCollector();
        coll.setUrl("jdbc:mysql://127.0.0.1:3306/endpoint_analysis_service");
        coll.setUsername("root");
        coll.setPassword("root");
        coll.setDatabase("endpoint_analysis_service");

        //创建Mysql转换器
        MysqlToJavaPolyConverter converter = new MysqlToJavaPolyConverter();

        //创建AssemblyFactory
        AssemblyFactory factory = new AssemblyFactory();
        factory.setCollector(coll);
        factory.setConverter(converter);

        //执行
        List<PolyTable> polyTables = factory.execute();
        //打印结果
        System.out.println(polyTables);

    }


}

