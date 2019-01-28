package eu.goodlike.hls.download.m3u;

import com.google.common.collect.ImmutableList;
import eu.goodlike.hls.download.ffmpeg.FfmpegProcessor;
import eu.goodlike.hls.download.m3u.data.MediaPlaylistData;
import eu.goodlike.hls.download.m3u.data.MediaPlaylistDataFactory;
import eu.goodlike.hls.download.m3u.data.builder.MediaPlaylistBuilder;
import eu.goodlike.hls.download.m3u.data.builder.MediaPlaylistBuilderFactory;
import eu.goodlike.hls.download.m3u.parse.HlsParser;
import eu.goodlike.hls.download.m3u.parse.RawString;
import eu.goodlike.hls.download.m3u.parse.StreamPartDurationTag;
import eu.goodlike.hls.download.m3u.parse.TargetDurationTag;
import okhttp3.HttpUrl;
import org.junit.After;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

public class MediaPlaylistTest {

    private static final String PLAYLIST_NAME = "source";
    private static final String RESOLUTION = "1920x1080";
    private static final HttpUrl URL = HttpUrl.parse("https://localhost:8080/source.m3u");

    private final HlsParser hlsParser = Mockito.mock(HlsParser.class);

    private final FfmpegProcessor ffmpegProcessor = Mockito.mock(FfmpegProcessor.class);
    private final MediaPlaylistDataFactory mediaPlaylistDataFactory =
            (filename, targetDuration, mediaParts) -> new MediaPlaylistData(filename, targetDuration, mediaParts, ffmpegProcessor);
    private final MediaPlaylistBuilderFactory mediaPlaylistBuilderFactory =
            source -> new MediaPlaylistBuilder(source, mediaPlaylistDataFactory);

    private final MediaPlaylist mediaPlaylist = new MediaPlaylist(PLAYLIST_NAME, RESOLUTION, URL,
            hlsParser, mediaPlaylistBuilderFactory);

    @After
    public void tearDown() throws IOException {
        Path path = Paths.get("source.m3u");

        if (Files.exists(path))
            Files.delete(path);
    }

    @Test
    public void downloadsItself() {
        TargetDurationTag targetDurationTag = new TargetDurationTag(BigDecimal.TEN);
        StreamPartDurationTag partDurationTag = new StreamPartDurationTag(BigDecimal.ONE);
        RawString urlTag = new RawString(URL.toString());

        Mockito.when(hlsParser.parse(URL)).thenReturn(ImmutableList.of(targetDurationTag, partDurationTag, urlTag));
        Mockito.when(ffmpegProcessor.processFfmpeg("source.m3u", "source.m3u"))
                .thenReturn(CompletableFuture.completedFuture(null));

        mediaPlaylist.download().join();

        Mockito.verify(hlsParser).parse(URL);
        Mockito.verify(ffmpegProcessor).processFfmpeg("source.m3u", "source.m3u");
    }

}
