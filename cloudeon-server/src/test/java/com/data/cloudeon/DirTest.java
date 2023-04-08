package com.data.cloudeon;

import com.data.cloudeon.utils.DirectoryScanner;
import org.junit.jupiter.api.Test;

import java.io.File;

public class DirTest {
    @Test
    public void scan() {
        DirectoryScanner.scanDirectory(new File("/Volumes/Samsung_T5/opensource/e-mapreduce/cloudeon-stack/UDH-1.0.0/monitor/render"),"/Volumes/Samsung_T5/opensource/e-mapreduce/cloudeon-stack/UDH-1.0.0/monitor/render");
    }
}
