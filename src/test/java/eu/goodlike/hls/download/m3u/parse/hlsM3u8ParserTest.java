package eu.goodlike.hls.download.m3u.parse;

import okhttp3.HttpUrl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class hlsM3u8ParserTest {

    private static final String URL_STR = "http://c13.prod.playlists.ihrhls.com/1713/playlist.m3u8";
    private static final HttpUrl URL = HttpUrl.parse(URL_STR);

    hlsM3u8Parser hlsM3u8Parser;

    @Before
    public void setUp() throws Exception {
        // hlsM3u8Parser = new hlsM3u8Parser()
    }

    @Test
    public void readFromInputStream() {
    }

    @Test
    public void isParsed() {
    }

    @Test
    public void parse() {
    }
}