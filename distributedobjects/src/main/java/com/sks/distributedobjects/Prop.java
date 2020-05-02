/**
 * Singleton class that supplies the configuration.
 */

package com.sks.distributedobjects;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public final class Prop {
    private static Properties property;
    private Prop() {}

    static {
        FileReader reader = null;
        try {
            reader = new FileReader("conf/config.prop");
            Properties prop = new Properties();
            try {
                prop.load(reader);
            } catch (IOException e1) {
                System.out.println(String.format("Prop could not read file", e1));
                System.exit(1);
            }
            property = prop;
        } catch (FileNotFoundException e2) {
            System.out.println(String.format("Prop could not load file", e2));
            System.exit(1);
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("Prop could not close file reader" + e);
                }
        }
    }

    public static Properties getProp() {
        return property;
    }
}
