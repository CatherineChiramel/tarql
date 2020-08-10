package org.deri.tarql;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamSourceExtend extends InputStreamSource {


    @Override
    public InputStream open() throws IOException {
        return System.in;
    }
}
