package com.wtm.netty;

import com.wtm.netty.NettyLists.embedded.AbsIntegerEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 类说明：测试出站处理,可以通过修改AbsIntegerEncoder看运行结果
 */
public class AbsIntegerEncoderTest {
    @Test
    public void testEncoded() {
        //(1) 创建一个 ByteBuf，并且写入 9 个负整数
        ByteBuf buf = Unpooled.buffer();
        for (int i = 1; i < 10; i++) {
            buf.writeInt(i * -1);
        }

        //(2) 创建一个EmbeddedChannel，并安装一个要测试的 AbsIntegerEncoder
        EmbeddedChannel channel = new EmbeddedChannel(new AbsIntegerEncoder());
        //(3) 写入 ByteBuf，并断言调用 readOutbound()方法将会产生数据
        assertTrue(channel.writeOutbound(buf));
        //(4) 将该 Channel 标记为已完成状态
        assertTrue(channel.finish());

        // read bytes
        //(5) 读取所产生的消息，并断言它们包含了对应的绝对值
        for (int i = 1; i < 10; i++) {
           // assertEquals(i,channel.readOutbound());
        }
        assertNull(channel.readOutbound());
    }



}
